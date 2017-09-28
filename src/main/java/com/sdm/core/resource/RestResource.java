/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.resource;

import com.sdm.core.hibernate.dao.AuditDAO;
import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.core.hibernate.entity.DefaultEntity;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.core.response.model.ListModel;
import com.sdm.core.response.model.MessageModel;
import com.sdm.core.response.model.Pagination;
import com.sdm.core.ui.UIProperty;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;

/**
 *
 * @author Htoonlin
 * @param <T>
 * @param <PK>
 */
public abstract class RestResource<T extends DefaultEntity, PK extends Serializable> extends DefaultResource
        implements IRestResource<T, PK> {

    protected abstract Logger getLogger();

    protected abstract RestDAO getDAO();

    @Context
    protected HttpServletRequest SERVLET_REQUEST;

    @PreDestroy
    protected void onDestroy() {
        if (getDAO() != null) {
            getDAO().closeSession();
        }
    }

    protected Class<T> getEntityClass() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class<T> entityClass = (Class<T>) type.getActualTypeArguments()[0];
        return entityClass;
    }

    @Override
    public IBaseResponse getNamedQueries() {
        DefaultResponse response = this.validateCache();
        // Cache validation
        if (response != null) {
            return response;
        }

        try {
            T instance = getEntityClass().newInstance();
            response = new DefaultResponse<>(instance.getQueries());
            response.setCode(HttpStatus.SC_OK);
            response.setStatus(ResponseType.SUCCESS);
            // Set Cache Header Info
            response.setHeaders(this.buildCache());
            return response;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new WebApplicationException(e.getLocalizedMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public IBaseResponse postQuery(String queryName, Map<String, Object> params) {
        DefaultResponse response = this.validateCache();
        // Cache validation
        if (response != null) {
            return response;
        }

        List<HashMap<String, Object>> data = (List<HashMap<String, Object>>) getDAO().fetchByName(queryName, params);
        ListModel content = new ListModel<>(data);
        response = new DefaultResponse<>(HttpStatus.SC_OK, ResponseType.SUCCESS, content);

        response.setHeaders(this.buildCache());
        return response;
    }

    @Override
    public IBaseResponse getAll() {
        DefaultResponse response = this.validateCache();
        // Cache validation
        if (response != null) {
            return response;
        }

        try {
            List<T> data = (List<T>) getDAO().fetchAll();
            ListModel<T> content = new ListModel<T>(data);
            response = new DefaultResponse<>(HttpStatus.SC_OK, ResponseType.SUCCESS, content);
            response.setHeaders(this.buildCache());
            return response;
        } catch (Exception e) {
            getLogger().error(e);
            throw e;
        }
    }

    @Override
    public IBaseResponse getPaging(String filter, int pageId, int pageSize, String sortString) {
        DefaultResponse response = this.validateCache();
        // Cache validation
        if (response != null) {
            return response;
        }

        try {
            long total = getDAO().getTotal(filter);
            List<T> data = (List<T>) getDAO().paging(filter, pageId, pageSize, sortString);

            if (data == null) {
                MessageModel message = new MessageModel(204, "No Data", "There is no data for your query string.");
                return new DefaultResponse<>(message);
            }

            Pagination<T> content = new Pagination<T>(data, total, pageId, pageSize);

            // Generate HAL Links
            content.genreateLinks(this.getClass());

            response = new DefaultResponse<>(HttpStatus.SC_OK, ResponseType.SUCCESS, content);

            response.setHeaders(this.buildCache());
            return response;
        } catch (Exception e) {
            getLogger().error(e);
            throw e;
        }
    }

    @Override
    public IBaseResponse getById(PK id) {
        DefaultResponse response = this.validateCache();
        // Cache validation
        if (response != null) {
            return response;
        }

        T data = getDAO().fetchById(id);
        if (data == null) {
            MessageModel message = new MessageModel(204, "No Data", "There is no data for your request.");
            return new DefaultResponse<>(message);
        }
        response = new DefaultResponse<T>(HttpStatus.SC_OK, ResponseType.SUCCESS, data);
        response.setHeaders(this.buildCache());
        return response;
    }

    @Override
    public IBaseResponse create(@Valid T request) {
        try {
            T entity = getDAO().insert(request, true);
            this.modifiedResource();
            return new DefaultResponse<T>(201, ResponseType.SUCCESS, entity);
        } catch (Exception e) {
            getLogger().error(e);
            throw e;
        }
    }

    @Override
    public IBaseResponse multiCreate(@Valid List<T> request) {
        try {
            List<T> processedList = new ArrayList<>();
            getDAO().beginTransaction();
            for (T entity : request) {
                T inserted = getDAO().insert(entity, false);
                processedList.add(inserted);
            }
            getDAO().commitTransaction();
            this.modifiedResource();

            ListModel<T> content = new ListModel<>(processedList);
            return new DefaultResponse(201, ResponseType.SUCCESS, content);
        } catch (Exception e) {
            getDAO().rollbackTransaction();
            getLogger().error(e);
            throw e;
        }
    }

    @Override
    public IBaseResponse update(@Valid T request, PK id) {
        try {
            T dbEntity = getDAO().fetchById(id);
            if (dbEntity == null) {
                MessageModel message = new MessageModel(204, "No Data", "There is no data for your request.");
                return new DefaultResponse<>(message);
            }
            T entity = getDAO().update(request, true);
            this.modifiedResource();
            return new DefaultResponse<T>(202, ResponseType.SUCCESS, entity);
        } catch (Exception e) {
            getLogger().error(e);
            throw e;
        }
    }

    @Override
    public IBaseResponse multiUpdate(@Valid List<T> request) {
        try {
            List<T> processedList = new ArrayList<>();
            getDAO().beginTransaction();
            for (T entity : request) {
                T updated = getDAO().update(entity, false);
                processedList.add(updated);
            }
            getDAO().commitTransaction();
            this.modifiedResource();

            ListModel<T> content = new ListModel<>(processedList);
            return new DefaultResponse(202, ResponseType.SUCCESS, content);
        } catch (Exception e) {
            getDAO().rollbackTransaction();
            getLogger().error(e);
            throw e;
        }
    }

    @Override
    public IBaseResponse remove(PK id) {
        try {
            MessageModel message = new MessageModel(204, "No Data", "There is no data for your request.");
            T entity = getDAO().fetchById(id);
            if (entity == null) {
                return new DefaultResponse<>(message);
            }

            getDAO().delete(entity, true);
            this.modifiedResource();

            message = new MessageModel(202, "Deleted", "We deleted the record with your request successfully.");
            return new DefaultResponse<>(202, ResponseType.SUCCESS, message);
        } catch (Exception e) {
            getLogger().error(e);
            throw e;
        }
    }

    @Override
    public IBaseResponse multiRemove(Set<PK> ids) {
        try {
            List<T> processedList = new ArrayList<>();
            getDAO().beginTransaction();
            for (PK id : ids) {
                T removeEntity = getDAO().fetchById(id);
                if (removeEntity == null) {
                    continue;
                }
                getDAO().delete(removeEntity, false);
                processedList.add(removeEntity);
            }
            getDAO().commitTransaction();
            this.modifiedResource();

            ListModel<T> content = new ListModel<>(processedList);
            return new DefaultResponse(202, ResponseType.SUCCESS, content);
        } catch (Exception e) {
            getLogger().error(e);
            throw e;
        }
    }

    @Override
    public IBaseResponse getStructure() {
        DefaultResponse response = this.validateCache();
        // Cache validation
        if (response != null) {
            return response;
        }

        try {
            T instance = getEntityClass().newInstance();
            List<UIProperty> properties = instance.getStructure();
            ListModel<UIProperty> content = new ListModel<UIProperty>(properties);

            response = new DefaultResponse<>(HttpStatus.SC_OK, ResponseType.SUCCESS, content);
            // Cache will hold a year of entity structure
            response.setHeaders(this.buildCache(3600 * 24 * 365));
            return response;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new WebApplicationException(e.getLocalizedMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public IBaseResponse getNewDataByVersion(long version) {
        DefaultResponse response = this.validateCache();
        // Cache validation
        if (response != null) {
            return response;
        }

        try {
            AuditDAO auditDAO = new AuditDAO(getDAO().getSession());
            ListModel<HashMap<String, Object>> content = new ListModel(auditDAO.getNewDataByVersion(getEntityClass(), version));

            response = new DefaultResponse<>(HttpStatus.SC_OK, ResponseType.SUCCESS, content);

            response.setHeaders(this.buildCache());
            return response;
        } catch (Exception e) {
            getLogger().error(e);
            throw e;
        }
    }

    @Override
    public IBaseResponse getByVersion(PK id, long version) {
        DefaultResponse response = this.validateCache();
        // Cache validation
        if (response != null) {
            return response;
        }

        AuditDAO auditDAO = new AuditDAO(getDAO().getSession());
        HashMap<String, Object> data = auditDAO.getDataByVersion(getEntityClass(), id, version);

        if (data == null) {
            MessageModel message = new MessageModel(204, "No Data", "There is no data for your request.");
            return new DefaultResponse<>(message);
        }
        response = new DefaultResponse<>(HttpStatus.SC_OK, ResponseType.SUCCESS, data);
        response.setHeaders(this.buildCache());
        return response;

    }

    @Override
    public IBaseResponse getVersions(PK id, int pageId, int pageSize) {
        DefaultResponse response = this.validateCache();
        // Cache validation
        if (response != null) {
            return response;
        }

        try {
            AuditDAO auditDAO = new AuditDAO(getDAO().getSession());
            long total = auditDAO.getTotal(getEntityClass(), id);
            List<HashMap<String, Object>> data = (List<HashMap<String, Object>>) auditDAO.getVersions(getEntityClass(), id, pageId, pageSize);
            Pagination<HashMap<String, Object>> content = new Pagination<>(data, total, pageId, pageSize);

            // Generate HAL Links
            content.genreateLinks(this.getClass());

            response = new DefaultResponse<>(HttpStatus.SC_OK, ResponseType.SUCCESS, content);

            response.setHeaders(this.buildCache());
            return response;
        } catch (Exception e) {
            getLogger().error(e);
            throw e;
        }
    }

}
