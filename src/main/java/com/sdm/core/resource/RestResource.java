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
import com.sdm.core.response.model.PaginationModel;
import com.sdm.core.ui.UIProperty;
import com.sdm.core.util.MyanmarFontManager;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PreDestroy;
import javax.persistence.Entity;
import javax.validation.Valid;
import javax.ws.rs.WebApplicationException;
import org.apache.commons.httpclient.HttpStatus;

/**
 *
 * @author Htoonlin
 * @param <T>
 * @param <PK>
 */
public class RestResource<T extends DefaultEntity, PK extends Serializable> extends DefaultResource
        implements IRestResource<T, PK> {

    private RestDAO mainDAO;

    public RestResource() {
        this.mainDAO = new RestDAO(this.getEntityClass().getName(), this);
    }

    protected RestDAO getDAO() {
        return mainDAO;
    }

    protected void setDAO(RestDAO mainDAO) {
        this.mainDAO = mainDAO;
    }

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

    protected T checkData(PK id) {
        T entity = getDAO().fetchById(id);
        if (entity == null) {
            throw new NullPointerException("There is no data for your request.");
        }

        return entity;
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

        String entityName = getEntityClass().getName();
        Entity entityAnno = getEntityClass().getAnnotation(Entity.class);
        if (entityAnno != null) {
            entityName = entityAnno.name();
        }

        queryName = entityName + "." + queryName.toUpperCase();

        List<HashMap<String, Object>> data = (List<HashMap<String, Object>>) getDAO().fetchByNamedQuery(queryName, params);
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
            //Change Filter Text To Unicode For Searching...
            if (MyanmarFontManager.isMyanmar(filter) && MyanmarFontManager.isZawgyi(filter)) {
                filter = MyanmarFontManager.toUnicode(filter);
            }

            long total = getDAO().getTotal(filter);
            List<T> data = (List<T>) getDAO().paging(filter, pageId, pageSize, sortString);

            if (data == null) {
                throw new NullPointerException("There is no data for your query string.");
            }

            PaginationModel<T> content = new PaginationModel<T>(data, total, pageId, pageSize);

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

        T data = this.checkData(id);

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
        T dbEntity = this.checkData(id);

        try {
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
        T entity = this.checkData(id);

        try {
            getDAO().delete(entity, true);
            this.modifiedResource();

            MessageModel message = new MessageModel(202, "Deleted", "We deleted the record with your request successfully.");
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
            throw new NullPointerException("There is no data for your request.");
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
            PaginationModel<HashMap<String, Object>> content = new PaginationModel<>(data, total, pageId, pageSize);

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
