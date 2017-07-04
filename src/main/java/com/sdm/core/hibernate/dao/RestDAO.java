/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.hibernate.dao;

import com.sdm.core.hibernate.DefaultProperty;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *
 * @author Htoonlin
 */
public abstract class RestDAO extends DefaultDAO implements IRestDAO {

    private static final Logger LOG = Logger.getLogger(RestDAO.class.getName());

    protected abstract String getEntityName();

    protected abstract boolean useVersion();

    protected abstract boolean useLog();

    protected abstract boolean useTimeStamp();

    protected abstract boolean useSoftDelete();

    protected final long USER_ID;

    public RestDAO(long userId) {
        super();
        this.USER_ID = userId;
    }

    public RestDAO(Session session, long userId) {
        super(session);
        this.USER_ID = userId;
    }

    protected String fetchHQL() {
        String hql = "FROM " + this.getEntityName();
        if (useSoftDelete()) {
            hql += " WHERE deletedAt IS NULL ";
        } else {
            hql += " WHERE 1 = 1";
        }

        return hql;
    }

    private boolean isSyncable() {
        return this.useVersion() && this.useLog() && this.useTimeStamp() && this.useSoftDelete();
    }

    @Override
    public List fetchNewData(Date lastSync, Sync status) throws Exception {
        if (!this.isSyncable()) {
            throw new Exception("This entity is not allow synchroniztion process.");
        }

        String filter = " WHERE 1 = 1";
        switch (status) {
            case INSERT:
                filter += " AND (createdAt >= :lastSync) AND (deletedAt IS NULL)";
                break;
            case UPDATE:
                filter += " AND (modifiedAt >= :lastSync) AND (version > 1) "
                        + "AND (modifiedAt != createdAt) AND (deletedAt IS NULL)";
                break;
            case REMOVE:
                filter += " AND (deletedAt >= :lastSync)";
                break;
            default:
                return null;
        }
        String hql = "FROM " + this.getEntityName() + filter;
        Map<String, Object> params = new HashMap<>();
        params.put("lastSync", lastSync);
        return this.createQuery(hql, params).getResultList();
    }

    protected String filterHQL(String filter) {
        String query = fetchHQL();
        if (filter != null && filter.length() > 0) {
            query += " AND " + DefaultProperty.GLOBAL_FILTER + " LIKE :filter";
        }
        return query;
    }

    @Override
    public long getTotal(String filter) {
        String hql = "SELECT COUNT(*) " + this.filterHQL(filter);
        Map<String, Object> params = new HashMap<>();
        params.put("filter", "%" + filter + "%");
        return (long) this.createQuery(hql, params).getSingleResult();
    }

    @Override
    public List paging(String filter, int pageId, int pageSize, String sortString) {
        String hql = this.filterHQL(filter);
        Map<String, Object> params = new HashMap<>();
        params.put("filter", "%" + filter + "%");

        //Build SortMap
        if (sortString.length() > 0) {
            hql += " ORDER BY ";
            String[] sorts = sortString.split(",");
            for (String sort : sorts) {
                String[] sortParams = sort.trim().split(":", 2);
                if (sortParams.length >= 2 && sortParams[1].equalsIgnoreCase("desc")) {
                    hql += " " + sortParams[0] + " DESC";
                } else {
                    hql += " " + sortParams[0] + " ASC";
                }
            }
        }

        //Calculate Start Index
        if (pageId <= 0) {
            pageId = 1;
        }
        int start = pageSize * (pageId - 1);

        //Retrieve Data
        return this.createQuery(hql, params, pageSize, start).getResultList();
    }

    @Override
    public List fetchAll() throws Exception {
        try {
            List queryList = this.fetch(fetchHQL(), null);
            if (queryList != null && queryList.size() > 0) {
                return (List) queryList;
            }
        } catch (Exception e) {
            throw e;
        }
        return new ArrayList();
    }

    @Override
    public <T extends Serializable> T fetchEntityById(Serializable id) throws Exception {
        try {
            T result = (T) getSession().get(this.getEntityName(), id);
            if (result != null && useSoftDelete()) {
                //get DeletedAt
                Field deletedAtField = result.getClass().getDeclaredField(DefaultProperty.DELETED_AT);
                deletedAtField.setAccessible(true);
                if (deletedAtField.get(DefaultProperty.DELETED_AT) != null) {
                    return null;
                }
            }
            return result;
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            LOG.error(e);
            throw e;
        }
    }

    @Override
    public HashMap fetchById(Serializable id) {
        HashMap result = (HashMap) getSession().get(this.getEntityName(), id);
        if (result != null && useSoftDelete()) {
            if (result.get(DefaultProperty.DELETED_AT) != null) {
                return null;
            }
        }
        return result;
    }

    private <T extends Serializable> void setValueByName(T entity, String name, Object value)
            throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
        Field field = entity.getClass().getDeclaredField(name);
        if (field != null) {
            field.setAccessible(true);
            field.set(entity.getClass(), value);
        }
    }

    @Override
    public <T extends Serializable> T insert(T entity, boolean autoCommit) throws Exception {
        try {
            if (useLog()) {
                //Set CreatedBy
                this.setValueByName(entity, DefaultProperty.CREATED_BY, USER_ID);
                //Set ModifiedBy
                this.setValueByName(entity, DefaultProperty.MODIFIED_BY, USER_ID);
            }
            if (useTimeStamp()) {
                Date currentDate = new Date();

                //Set CreatedAt
                this.setValueByName(entity, DefaultProperty.CREATE_AT, currentDate);

                //Set ModifiedAt
                this.setValueByName(entity, DefaultProperty.MODIFIED_AT, currentDate);
            }
            if (useVersion()) {
                //Set Version
                this.setValueByName(entity, DefaultProperty.VERSION, 1L);
            }

            if (autoCommit) {
                beginTransaction();
            }
            entity = (T) getSession().save(entity);
            if (autoCommit) {
                commitTransaction();
            }
            return entity;
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            if (autoCommit) {
                rollbackTransaction();
            }
            LOG.error(e);
            throw e;
        }
    }

    @Override
    public Map<String, Object> insert(Map<String, Object> entity, boolean autoCommit) throws Exception {
        try {
            if (useLog()) {
                entity.put(DefaultProperty.CREATED_BY, USER_ID);
                entity.put(DefaultProperty.MODIFIED_BY, USER_ID);
            }
            if (useTimeStamp()) {
                Date currentDate = new Date();
                entity.put(DefaultProperty.CREATE_AT, currentDate);
                entity.put(DefaultProperty.MODIFIED_AT, currentDate);
            }
            if (useVersion()) {
                entity.put(DefaultProperty.VERSION, 1l);
            }

            if (autoCommit) {
                beginTransaction();
            }
            Serializable id = getSession().save(this.getEntityName(), entity);
            entity.put(DefaultProperty.ID, id);
            if (autoCommit) {
                commitTransaction();
            }
            return entity;
        } catch (Exception e) {
            if (autoCommit) {
                rollbackTransaction();
            }
            LOG.error(e);
            throw e;
        }
    }

    private boolean checkDataVersion(Map<String, Object> entity, Map<String, Object> dbEntity) {
        if (!useVersion()) {
            return true;
        }
        if (entity.containsKey(DefaultProperty.VERSION) && dbEntity.containsKey(DefaultProperty.VERSION)) {
            long reqVersion = (long) entity.get(DefaultProperty.VERSION);
            long dbVersion = (long) dbEntity.get(DefaultProperty.VERSION);

            return reqVersion == dbVersion;
        }
        return false;
    }

    @Override
    public <T extends Serializable> T update(T entity, boolean autoCommit) throws Exception {
        try {
            if (useVersion()) {
                //get Version
                Field versionField = entity.getClass().getDeclaredField(DefaultProperty.VERSION);
                versionField.setAccessible(true);
                long currentVersion = versionField.getLong(entity.getClass());

                //get ID
                Field idField = entity.getClass().getDeclaredField(DefaultProperty.ID);
                idField.setAccessible(true);
                Serializable id = (Serializable) idField.get(entity.getClass());

                Map<String, Object> existingEntity = fetchById(id);
                if (existingEntity == null
                        || !existingEntity.getOrDefault(DefaultProperty.VERSION, 0L).equals(currentVersion)) {
                    throw new Exception("Opps! Your data is not same with current data.");
                }

                this.setValueByName(entity, DefaultProperty.VERSION, currentVersion + 1);
            }

            if (useLog()) {
                this.setValueByName(entity, DefaultProperty.MODIFIED_BY, USER_ID);
            }

            if (useTimeStamp()) {
                this.setValueByName(entity, DefaultProperty.MODIFIED_AT, new Date());
            }

            if (autoCommit) {
                beginTransaction();
            }
            entity = (T) getSession().merge(entity);
            getSession().update(entity);
            if (autoCommit) {
                commitTransaction();
            }
            return entity;
        } catch (Exception e) {
            if (autoCommit) {
                rollbackTransaction();
            }
            LOG.error(e);
            throw e;
        }
    }

    @Override
    public Map<String, Object> update(Map<String, Object> entity, boolean autoCommit) throws Exception {
        try {
            if (!entity.containsKey(DefaultProperty.ID)) {
                throw new Exception("There is no <id> field to update entity.");
            }
            if (useVersion()) {
                Serializable id = (Serializable) entity.get(DefaultProperty.ID);
                Map<String, Object> existingEntity = fetchById(id);
                if (existingEntity == null || !checkDataVersion(entity, existingEntity)) {
                    throw new Exception("Opps! Your data is not same with current data.");
                }
                long version = (long) existingEntity.get(DefaultProperty.VERSION);
                entity.put(DefaultProperty.VERSION, version + 1);
            }

            if (useLog()) {
                entity.put(DefaultProperty.MODIFIED_BY, USER_ID);
            }
            if (useTimeStamp()) {
                entity.put(DefaultProperty.MODIFIED_AT, new Date());
            }
            if (autoCommit) {
                beginTransaction();
            }
            getSession().saveOrUpdate(this.getEntityName(), entity);
            if (autoCommit) {
                commitTransaction();
            }
            return entity;
        } catch (Exception e) {
            if (autoCommit) {
                rollbackTransaction();
            }
            LOG.error(e);
            throw e;
        }
    }

    @Override
    public void delete(Serializable id, boolean autoCommit) throws Exception {
        try {
            Map<String, Object> entity = this.fetchById(id);
            if (entity == null) {
                throw new Exception("There is no data to remove.");
            }
            if (autoCommit) {
                beginTransaction();
            }

            if (useTimeStamp()) {
                entity.put(DefaultProperty.DELETED_AT, new Date());
                getSession().saveOrUpdate(this.getEntityName(), entity);
            } else {
                getSession().delete(this.getEntityName(), entity);

            }

            if (autoCommit) {
                commitTransaction();
            }
        } catch (Exception e) {
            if (autoCommit) {
                rollbackTransaction();
            }
            LOG.error(e);
            throw e;
        }
    }
}
