/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.hibernate.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *
 * @author Htoonlin
 */
public class RestDAO extends DefaultDAO {

    private static final Logger LOG = Logger.getLogger(RestDAO.class.getName());

    public static final String ID = "id";
    public static final String GLOBAL_FILTER = "search";

    protected final String ENTITY_NAME;

    public RestDAO(String entityName, int userId) {
        super(userId);
        this.ENTITY_NAME = entityName;
    }

    public RestDAO(Session session, String entityName, int userId) {
        super(session, userId);
        this.ENTITY_NAME = entityName;
    }

    protected String fetchHQL() {
        return "FROM " + this.ENTITY_NAME + " WHERE 1 = 1";
    }
    
    public String buildGlobalFilter(String keyword, Map<String, Object> params, boolean isCaseSensitive){
        if(keyword == null || keyword.length() <= 0 || params == null){
            return "";
        }
        
        params.put("filter", "%" + keyword + "%");
        String hql = " AND LOWER(" + GLOBAL_FILTER + " LIKE LOWER(:filter)";
        if(isCaseSensitive){
            hql = " AND " + GLOBAL_FILTER + " LIKE :filter";
        }
        return hql;
    }

    public long getTotal(String filter) {
        String hql = "SELECT COUNT(*) " + fetchHQL();
        HashMap<String, Object> params = new HashMap<>();
        this.buildGlobalFilter(filter, params, false);

        return (long) this.createQuery(hql, params).getSingleResult();
    }

    public List<?> paging(String filter, int pageId, int pageSize, String sortString) {
        String hql = fetchHQL();

        // Init Filter
        HashMap<String, Object> params = new HashMap<>();
        this.buildGlobalFilter(filter, params, false);

        // Build SortMap
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

        // Calculate Start Index
        if (pageId <= 0) {
            pageId = 1;
        }
        int start = pageSize * (pageId - 1);

        // Retrieve Data
        return this.createQuery(hql, params, pageSize, start).getResultList();
    }

    public List<?> fetchAll() {
        List<?> queryList = this.fetch(fetchHQL(), null);
        if (queryList != null && queryList.size() > 0) {
            return queryList;
        }
        return new ArrayList<>();
    }

    public <T extends Serializable> T fetchById(Serializable id) {
        try {
            T result = (T) getSession().get(ENTITY_NAME, id);
            return result;
        } catch (SecurityException | IllegalArgumentException e) {
            LOG.error(e);
            throw e;
        }
    }

    public <T extends Serializable> T insert(T entity, boolean autoCommit) {
        try {
            if (autoCommit) {
                beginTransaction();
            }
            getSession().save(entity);
            if (autoCommit) {
                commitTransaction();
            }
            return entity;
        } catch (SecurityException | IllegalArgumentException e) {
            if (autoCommit) {
                rollbackTransaction();
            }
            LOG.error(e);
            throw e;
        }
    }

    public Map<String, Object> insert(Map<String, Object> entity, boolean autoCommit) {
        try {
            if (autoCommit) {
                beginTransaction();
            }
            Serializable id = getSession().save(this.ENTITY_NAME, entity);
            entity.put(ID, id);
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

    public <T extends Serializable> T update(T entity, boolean autoCommit) {
        try {
            if (autoCommit) {
                beginTransaction();
            }
            entity = (T) getSession().merge(entity);
            getSession().saveOrUpdate(entity);
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

    public Map<String, Object> update(Map<String, Object> entity, boolean autoCommit) {
        try {

            if (autoCommit) {
                beginTransaction();
            }
            entity = (HashMap<String, Object>) getSession().merge(this.ENTITY_NAME, entity);
            getSession().saveOrUpdate(this.ENTITY_NAME, entity);
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

    public <T extends Serializable> void delete(T entity, boolean autoCommit) {
        try {
            if (autoCommit) {
                beginTransaction();
            }

            getSession().delete(this.ENTITY_NAME, entity);

            if (autoCommit) {
                commitTransaction();
            }
        } catch (Exception e) {
            if (autoCommit) {
                rollbackTransaction();
            }
            LOG.error(e);
        }
    }

}
