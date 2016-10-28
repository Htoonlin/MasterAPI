/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.database.dao;

import com.sdm.core.Globalizer;
import com.sdm.core.database.entity.ITimestampEntity;
import com.sdm.core.database.entity.ILogEntity;
import com.sdm.core.response.PaginationResponse;
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

    private String fetchHQL() throws Exception {
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

    public PaginationResponse pagination(String searchProperty, String filter, int size, int page, String sortString) throws Exception {
        Map<String, String> sortMaps = new HashMap<>();
        try {
            if (page <= 0) {
                page = 1;
            }
            int start = size * (page - 1);
            String hqlCount = " SELECT count(*) " + fetchHQL();
            String hqlString = fetchHQL();
            if (filter.length() > 0) {
                String filtering = " AND " + searchProperty + " LIKE :search";
                hqlString += filtering;
                hqlCount += filtering;
            }
            if (sortString.length() > 0) {
                String[] sorts = sortString.split(",");
                if (sorts.length > 0) {
                    hqlString += " ORDER BY ";
                }
                for (String sort : sorts) {
                    String[] sortParams = sort.trim().split(":", 2);
                    if (sortParams.length >= 2 && sortParams[1].equalsIgnoreCase("desc")) {
                        hqlString += sortParams[0] + " " + sortParams[1];
                        sortMaps.put(sortParams[0], sortParams[1]);
                    } else {
                        hqlString += sortParams[0];
                        sortMaps.put(sortParams[0], "asc");
                    }
                    hqlString += ",";
                }
                if (hqlString.endsWith(",")) {
                    hqlString = hqlString.substring(0, hqlString.length() - 1);
                }
            }

            Map<String, Object> params = new HashMap<>();
            if (filter.length() > 0) {
                params.put("search", "%" + filter + "%");
            }
            List<T> queryList = this.createQuery(hqlString, params, size, start).getResultList();
            long total = (long) this.createQuery(hqlCount, params).getSingleResult();
            if (queryList != null && queryList.size() > 0) {
                return new PaginationResponse(total, page, size, sortMaps, queryList);
            }
        } catch (Exception e) {
            throw e;
        }
        return new PaginationResponse(0, page, size, sortMaps, new ArrayList<>());
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
