/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.hibernate.dao;

import com.sdm.core.Globalizer;
import com.sdm.core.hibernate.entity.ITimestampEntity;
import com.sdm.core.hibernate.entity.ILogEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;

/**
 *
 * @author Htoonlin
 * @param <T>
 */
public class RestDAO<T extends Serializable> extends DefaultDAO<T> {

    public static final char SYNC_INSERT = 'I';
    public static final char SYNC_UPDATE = 'U';
    public static final char SYNC_REMOVE = 'R';

    protected boolean useLog;
    protected boolean useTimeStamp;
    private final HttpSession httpSession;

    protected int getUserId() {
        try {
            return (int) httpSession.getAttribute(Globalizer.SESSION_USER_ID);
        } catch (Exception e) {
            return 0;
        }
    }

    public RestDAO(Class<T> entityClass, HttpSession httpSession) {
        super(entityClass);
        this.httpSession = httpSession;
        this.validateEntity();
    }

    public RestDAO(Session session, Class<T> entityClass, HttpSession httpSession) {
        super(session, entityClass);
        this.httpSession = httpSession;
        this.validateEntity();
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

    private void validateEntity() {
        this.useLog = ILogEntity.class.isAssignableFrom(entityClass);
        this.useTimeStamp = ITimestampEntity.class.isAssignableFrom(entityClass);
    }

    public String fetchHQL() throws Exception {
        String hql = "FROM " + this.entityClass.getSimpleName();
        if (useTimeStamp) {
            hql += " WHERE deletedAt IS NULL ";
        } else {
            hql += " WHERE 1 = 1";
        }

        return hql;
    }

    public List<T> fetchNewData(Date lastSync, char status) throws Exception {
        String filter = " WHERE 1 = 1";
        switch (status) {
            case SYNC_INSERT:
                filter += " AND (createdAt >= :lastSync) AND (deletedAt IS NULL)";
                break;
            case SYNC_UPDATE:
                filter += " AND (modifiedAt >= :lastSync) AND (version > 1) "
                        + "AND (modifiedAt != createdAt) AND (deletedAt IS NULL)";
                break;
            case SYNC_REMOVE:
                filter += " AND (deletedAt >= :lastSync)";
                break;
            default:
                return null;
        }
        String hql = "FROM " + this.entityClass.getSimpleName() + filter;
        Map<String, Object> params = new HashMap<>();
        params.put("lastSync", lastSync);
        return this.createQuery(hql, params).getResultList();
    }

    public List<T> fetchAll() throws Exception {
        try {
            List queryList = mainSession.createQuery(fetchHQL(), super.entityClass).getResultList();
            if (queryList != null && queryList.size() > 0) {
                return (List<T>) queryList;
            }
        } catch (Exception e) {
            throw e;
        }
        return new ArrayList();
    }

    public T fetchById(Serializable id) {
        try {
            T result = mainSession.get(this.entityClass, id);
            if (result == null) {
                return null;
            } else if (useTimeStamp && ((ITimestampEntity) result).getDeletedAt() != null) {
                return null;
            }
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    public T insert(T entity, boolean autoCommit) throws Exception {
        try {
            if (useLog) {
                ((ILogEntity) entity).setCreatedBy(getUserId());
                ((ILogEntity) entity).setVersion(1);
            }
            if (useTimeStamp) {
                ((ITimestampEntity) entity).setCreatedAt(new Date());
            }
            if (autoCommit) {
                beginTransaction();
            }
            mainSession.save(entity);
            if (autoCommit) {
                commitTransaction();
            }
            return entity;
        } catch (Exception e) {
            if (autoCommit) {
                rollbackTransaction();
            }
            throw e;
        }
    }

    public T update(T entity, boolean autoCommit) throws Exception {
        try {
            if (useLog) {
                ((ILogEntity) entity).setModifiedBy(getUserId());
            }
            if (useTimeStamp) {
                ((ITimestampEntity) entity).setModifiedAt(new Date());
            }
            if (autoCommit) {
                beginTransaction();
            }
            entity = (T) mainSession.merge(entity);
            mainSession.update(entity);
            if (autoCommit) {
                commitTransaction();
            }
            return entity;
        } catch (Exception e) {
            if (autoCommit) {
                rollbackTransaction();
            }
            throw e;
        }
    }

    public void delete(T entity, boolean autoCommit) throws Exception {
        try {
            if (useTimeStamp) {
                ((ITimestampEntity) entity).setDeletedAt(new Date());
                this.update(entity, autoCommit);
            } else {
                if (autoCommit) {
                    beginTransaction();
                }
                mainSession.delete(entity);
                if (autoCommit) {
                    commitTransaction();
                }
            }
        } catch (Exception e) {
            if (autoCommit) {
                rollbackTransaction();
            }
            throw e;
        }
    }
}