/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdm.core.Globalizer;
import com.sdm.core.database.HibernateConnector;
import com.sdm.core.database.dao.RestDAO;
import com.sdm.core.database.entity.RestEntity;
import com.sdm.core.request.QueryRequest;
import com.sdm.core.request.SyncRequest;
import com.sdm.core.request.query.Alias;
import com.sdm.core.request.query.Condition;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.ErrorResponse;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.ListResponse;
import com.sdm.core.response.MessageResponse;
import com.sdm.core.response.PaginationResponse;
import com.sdm.core.response.PropertiesResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.core.response.SyncResponse;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.Id;
import org.apache.log4j.Logger;
import org.hibernate.Metamodel;
import javax.persistence.metamodel.Attribute;

/**
 *
 * @author Htoonlin
 * @param <T>
 * @param <PK>
 */
public class RestResource<T extends RestEntity, PK extends Serializable>
        extends DefaultResource
        implements IRestResource<T, PK>, IUtilResource<T> {

    protected static final Logger LOG = Logger.getLogger(RestResource.class.getName());

    protected RestDAO<T> mainDAO;
    private final Class<T> currentEntityClass;

    public RestResource() {
        try {
            ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
            this.currentEntityClass = (Class<T>) type.getActualTypeArguments()[0];
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }

    @PostConstruct
    private void init() {
        this.mainDAO = new RestDAO<T>(currentEntityClass, getHttpSession());
        this.onLoad();
    }

    protected void onLoad() {
        LOG.info("Call onload after init");
    }

    @Override
    public DefaultResponse getAll() throws Exception {
        try {
            List<T> data = mainDAO.fetchAll();
            return new DefaultResponse(new ListResponse(data));
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }

    @Override
    public DefaultResponse getPaging(String filter, int pageId, int pageSize, String sort) throws Exception {
        try {
            PaginationResponse response = mainDAO.pagination("search", filter, pageSize, pageId, sort);
            return new DefaultResponse(response);
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }

    @Override
    public DefaultResponse getById(PK id) throws Exception {
        T data = mainDAO.fetchById(id);
        if (data == null) {
            return new DefaultResponse(new MessageResponse(204, ResponseType.WARNING,
                    "NO_DATA", "There is no data for your request."));
        }
        return new DefaultResponse(data);
    }

    @Override
    public IBaseResponse create(T request) throws Exception {
        try {
            if (!request.isValid()) {
                return new ErrorResponse(request.getErrors());
            }
            mainDAO.insert(request, true);
            MessageResponse message = new MessageResponse(201, ResponseType.SUCCESS,
                    "CREATED", "We created new record on your request successfully.");
            return new DefaultResponse(message);
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }

    @Override
    public IBaseResponse update(T request, PK id) throws Exception {
        try {
            if (!request.isValid()) {
                return new ErrorResponse(request.getErrors());
            }
            T entity = mainDAO.fetchById(id);
            if (entity == null || !entity.equals(request)) {
                return new DefaultResponse(new MessageResponse(204, ResponseType.WARNING,
                        "NO_DATA", "There is no data for your request."));
            }
            request.setVersion(entity.getVersion() + 1);
            mainDAO.update(request, true);
            MessageResponse message = new MessageResponse(202, ResponseType.SUCCESS,
                    "UPDATED", "We updated the record with your request successfully.");
            return new DefaultResponse(message);
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }

    @Override
    public DefaultResponse remove(PK id) throws Exception {
        try {
            T entity = mainDAO.fetchById(id);
            if (entity == null) {
                return new DefaultResponse(new MessageResponse(204, ResponseType.WARNING,
                        "NO_DATA", "There is no data for your request."));
            }
            mainDAO.delete(entity, true);
            MessageResponse message = new MessageResponse(202, ResponseType.SUCCESS,
                    "DELETED", "We deleted the record with your request successfully.");
            return new DefaultResponse(message);
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }

    @Override
    public DefaultResponse getStructure() {
        Metamodel metaModel = HibernateConnector.getFactory().getMetamodel();
        List<PropertiesResponse> properties = new ArrayList<>();
        for (Attribute<T, ?> attribute : metaModel.entity(currentEntityClass).getDeclaredAttributes()) {
            Field field = null;
            //Get Field Info
            try {
                field = currentEntityClass.getDeclaredField(attribute.getName());
                if (field == null) {
                    continue;
                }

                if (field.getAnnotation(JsonIgnore.class) != null) {
                    continue;
                }
            } catch (NoSuchFieldException | SecurityException e) {
                LOG.error(e);
                continue;
            }

            PropertiesResponse property = new PropertiesResponse();
            property.setRequestName(Globalizer.camelToLowerUnderScore(attribute.getName()));
            property.setName(attribute.getName());
            property.setType(attribute.getJavaType().getSimpleName());
            if (field.getAnnotation(Id.class) != null) {
                property.setPrimaryKey(true);
            }
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                property.setDbName(column.name());
                property.setDbType(column.columnDefinition());
                if (column.nullable()) {
                    property.setNullable(column.nullable());
                }
            }

            if (attribute.getPersistentAttributeType() != Attribute.PersistentAttributeType.BASIC) {
                property.setSpecial(attribute.getPersistentAttributeType().name());
            }
            properties.add(property);
        }
        return new DefaultResponse(new ListResponse(properties));
    }

    @Override
    public IBaseResponse syncData(SyncRequest<T> request) throws Exception {
        try {
            if (!request.isValid()) {
                return new ErrorResponse(request.getErrors());
            }

            mainDAO.beginTransaction();
            SyncResponse<T> response = new SyncResponse<>();
            //Retrieve new/update/remove data from server
            response.setInsert(mainDAO.fetchNewData(new Date(request.getLastSync()), RestDAO.SYNC_INSERT));
            response.setUpdate(mainDAO.fetchNewData(new Date(request.getLastSync()), RestDAO.SYNC_UPDATE));
            response.setRemove(mainDAO.fetchNewData(new Date(request.getLastSync()), RestDAO.SYNC_REMOVE));

            //Delete Data by Client Request
            if (request.getRemove() != null) {
                for (T rEntity : request.getRemove()) {
                    mainDAO.delete(rEntity, false);

                    //Clean insert list by Remove Entity
                    cleanSyncList(response.getInsert(), rEntity);

                    //Clean update list by Remove Entity
                    cleanSyncList(response.getUpdate(), rEntity);
                }
            }

            //Update Data from Client to Server
            if (request.getUpdate() != null) {
                for (T uEntity : request.getUpdate()) {
                    if (uEntity.getModifiedAt() == null) {
                        uEntity.setModifiedAt(new Date());
                    }
                    //Check DB Data
                    T dbEntity = mainDAO.fetchById(uEntity.getId());
                    if (dbEntity != null
                            && dbEntity.getModifiedAt().before(uEntity.getModifiedAt())) {
                        uEntity.setVersion(dbEntity.getVersion() + 1);
                        mainDAO.update(uEntity, false);

                        //Clean insert list by Update Entity
                        cleanSyncList(response.getInsert(), uEntity);

                        //Clean update list by Update Entity
                        cleanSyncList(response.getUpdate(), uEntity);
                    }
                }
            }

            //Insert Data from Client to Server
            if (request.getInsert() != null) {
                for (T iEntity : request.getInsert()) {
                    T responseEntity = mainDAO.insert(iEntity, false);
                    //Reponse Inserted ID to Update
                    response.addInsert(responseEntity);
                }
            }
            mainDAO.commitTransaction();

            return new DefaultResponse(response);
        } catch (Exception e) {
            LOG.error(e);
            mainDAO.rollbackTransaction();
            throw e;
        }
    }

    private void cleanSyncList(List<T> sourceList, T filter) {
        for (T source : sourceList) {
            if (filter.getModifiedAt() == null) {
                filter.setModifiedAt(new Date());
            }
            if (source.equals(filter)
                    && source.getModifiedAt().before(filter.getModifiedAt())) {
                sourceList.remove(source);
                break;
            }
        }
    }

    @Override
    public IBaseResponse queryData(QueryRequest request) throws Exception {
        try {
            if (!request.isValid()) {
                return new ErrorResponse(request.getErrors());
            }
            //Init Query String
            String query = "FROM " + currentEntityClass.getSimpleName() + " WHERE deletedAt IS NULL";

            //Add Selected Column
            List<String> resultColumns = new ArrayList<>();
            if (request.getColumns() != null && request.getColumns().size() > 0) {
                String columns = "";
                for (Alias column : request.getColumns()) {
                    if (column.getAlias() != null && column.getAlias().length() > 0) {
                        columns += column.getName() + " AS " + column.getAlias() + ",";
                        resultColumns.add(column.getAlias());
                    } else {
                        columns += column.getName() + ",";
                        resultColumns.add(column.getName());
                    }
                }
                if (columns.endsWith(",")) {
                    columns = columns.substring(0, columns.length() - 1);
                }
                query = "SELECT " + columns + " " + query;
            }

            //Build Where Condition
            Map<String, Object> params = new HashMap<>();
            if (request.getQuery() != null) {
                int i = 0;
                for (Condition condition : request.getQuery()) {
                    query += " " + condition.getLogic().getValue();
                    query += " " + condition.getColumn();
                    query += " " + condition.getExpression().getValue();
                    query += " :param_" + i;
                    params.put("param_" + i, condition.getValue());
                    i++;
                }
            }

            //Build Sort
            if (request.getSort() != null && request.getSort().size() > 0) {
                query += " ORDER BY";
                for (String string : request.getSort().keySet()) {
                    query += " " + string + " " + request.getSort().get(string).toString() + ",";
                }
                if (query.endsWith(",")) {
                    query = query.substring(0, query.length() - 1);
                }
            }

            //Retrieve Data
            List data = mainDAO.createQuery(query, params, request.getSize(), request.getStart()).getResultList();
            if (data == null) {
                return new DefaultResponse(new MessageResponse(204, ResponseType.WARNING,
                        "NO_DATA", "There is no data for your query string."));
            }

            //Build Data
            if (resultColumns.size() > 0) {
                List<Map<String, Object>> output = new ArrayList<>();
                List<Object[]> recordList = (List<Object[]>) data;
                for (Object[] record : recordList) {
                    Map<String, Object> dataWithCol = new HashMap<>();
                    for (int i = 0; i < record.length; i++) {
                        dataWithCol.put(resultColumns.get(i), record[i]);
                    }
                    output.add(dataWithCol);
                }
                return new DefaultResponse(new ListResponse(output));
            }

            return new DefaultResponse(new ListResponse((List<T>) data));
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }
}
