/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.resource;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.log4j.Logger;

import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.core.hibernate.entity.DefaultEntity;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.ErrorResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.ListResponse;
import com.sdm.core.response.MessageResponse;
import com.sdm.core.response.PaginationResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.core.response.model.UIProperty;

/**
 *
 * @author Htoonlin
 * @param <T>
 * @param <PK>
 */
public abstract class RestResource<T extends DefaultEntity, PK extends Serializable>
        extends DefaultResource implements IRestResource<T, PK> {

    protected abstract Logger getLogger();

    protected abstract RestDAO getDAO();

    protected Class<T> getEntityClass() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class<T> entityClass = (Class<T>) type.getActualTypeArguments()[0];
		return entityClass;
    }

    @Override
    public IBaseResponse getAll() throws Exception {
        try {
            List<T> data = getDAO().fetchAll();
            ListResponse<T> response = new ListResponse<T>(data);
            return response;
        } catch (Exception e) {
            getLogger().error(e);
            throw e;
        }
    }

    @Override
    public IBaseResponse getPaging(String filter, int pageId, int pageSize, String sortString) throws Exception {
        try {
            long total = getDAO().getTotal(filter);
            List<T> data = getDAO().paging(filter, pageId, pageSize, sortString);

            if (data == null) {
                return new MessageResponse(204, ResponseType.WARNING, "There is no data for your query string.");
            }

            PaginationResponse<T> response = new PaginationResponse<T>(data, total, pageId, pageSize);

            return response;
        } catch (Exception e) {
            getLogger().error(e);
            throw e;
        }
    }

    @Override
    public IBaseResponse getById(PK id) throws Exception {
        T data = getDAO().fetchById(id);
        if (data == null) {
            return new MessageResponse(204, ResponseType.WARNING, "There is no data for your request.");
        }
        return new DefaultResponse<T>(data);
    }

    @Override
    public IBaseResponse create(T request) throws Exception {
        try {
            if (!request.isValid()) {
                return new ErrorResponse(request.getErrors());
            }

            T entity = getDAO().insert(request, true);
            return new DefaultResponse<T>(entity);
        } catch (Exception e) {
            getLogger().error(e);
            throw e;
        }
    }

    @Override
    public IBaseResponse update(T request, PK id) throws Exception {
        try {
            if (!request.isValid()) {
                return new ErrorResponse(request.getErrors());
            }
            T entity = getDAO().update(request, true);
            return new DefaultResponse<T>(entity);
        } catch (Exception e) {
            getLogger().error(e);
            throw e;
        }
    }

    @Override
    public IBaseResponse remove(PK id) throws Exception {
        try {
            T entity = getDAO().fetchById(id);
            if (entity == null) {
                return new MessageResponse(204, ResponseType.WARNING, "There is no data for your request.");
            }

            getDAO().delete(id, true);
            return new MessageResponse(202, ResponseType.SUCCESS, "We deleted the record with your request successfully.");
        } catch (Exception e) {
            getLogger().error(e);
            throw e;
        }
    }

    @Override
    public IBaseResponse getStructure() throws Exception {
        T instance = getEntityClass().newInstance();
        List<UIProperty> properties = instance.getStructure();
        ListResponse<UIProperty> response = new ListResponse<UIProperty>(properties);
        return response;
    }

    /*
    @Override
    public IBaseResponse syncData(SyncRequest request) throws Exception {
        try {
            if (!request.isValid()) {
                return new ErrorResponse(request.getErrors());
            }

            getDAO().beginTransaction();
            SyncResponse response = new SyncResponse();
            //Retrieve new/update/remove data from server
            List<Map> responseInsertList = getDAO().fetchNewData(new Date(request.getLastSync()), Sync.INSERT);
            List<Map> responseUpdateList = getDAO().fetchNewData(new Date(request.getLastSync()), Sync.UPDATE);
            List<Map> responseRemoveList = getDAO().fetchNewData(new Date(request.getLastSync()), Sync.REMOVE);
            response.setInsert(responseInsertList);
            response.setUpdate(responseUpdateList);
            response.setRemove(responseRemoveList);

            //Delete Data by Client Request
            if (request.getRemove() != null) {
                List<Map<String, Object>> removeList = (List<Map<String, Object>>) request.getRemove();
                for (Map<String, Object> rEntity : removeList) {
                    if (rEntity.containsKey(DefaultProperty.ID)) {
                        PK removeId = (PK) rEntity.get(DefaultProperty.ID);
                        getDAO().delete(removeId, false);

                        //Clean insert list by Remove Entity
                        cleanSyncList(response.getInsert(), rEntity);

                        //Clean update list by Remove Entity
                        cleanSyncList(response.getUpdate(), rEntity);
                    }
                }
            }

            //Update Data from Client to Server
            if (request.getUpdate() != null) {
                List<Map<String, Object>> updateList = (List<Map<String, Object>>) request.getUpdate();
                for (Map uEntity : updateList) {
                    if (!uEntity.containsKey(DefaultProperty.MODIFIED_AT)) {
                        uEntity.put(DefaultProperty.MODIFIED_AT, new Date());
                    }
                    //Check DB Data
                    Map dbEntity = getDAO().fetchById((PK) uEntity.get(DefaultProperty.ID));
                    if (dbEntity == null) {
                        continue;
                    }

                    Date dbDate = (Date) dbEntity.get(DefaultProperty.MODIFIED_AT);
                    Date reqDate = (Date) uEntity.get(DefaultProperty.MODIFIED_AT);
                    if (dbDate.before(reqDate)) {
                        getDAO().update(uEntity, false);

                        //Clean insert list by Update Entity
                        cleanSyncList(response.getInsert(), uEntity);

                        //Clean update list by Update Entity
                        cleanSyncList(response.getUpdate(), uEntity);
                    }
                }
            }

            //Insert Data from Client to Server
            if (request.getInsert() != null) {
                List<Map> insertList = (List<Map>) request.getInsert();
                for (Map iEntity : insertList) {
                    Map responseEntity = getDAO().insert(iEntity, false);
                    //Reponse Inserted ID to Update
                    responseInsertList.add(responseEntity);
                }
                response.setInsert(insertList);
            }
            getDAO().commitTransaction();

            return new DefaultResponse(response);
        } catch (Exception e) {
            getLogger().error(e);
            getDAO().rollbackTransaction();
            throw e;
        }
    }

    private void cleanSyncList(List<Map> sourceList, Map filter) {
        for (Map source : sourceList) {
            if (!filter.containsKey(DefaultProperty.MODIFIED_AT)) {
                filter.put(DefaultProperty.MODIFIED_AT, new Date());
            }
            PK sourceId = (PK) source.get(DefaultProperty.ID);
            PK filterId = (PK) filter.get(DefaultProperty.ID);
            Date sourceModifiedAt = (Date) source.get(DefaultProperty.MODIFIED_AT);
            Date filterModifiedAt = (Date) filter.get(DefaultProperty.MODIFIED_AT);

            if (sourceId.equals(filterId) && sourceModifiedAt.before(filterModifiedAt)) {
                sourceList.remove(source);
                break;
            }
        }
    }*/
}
