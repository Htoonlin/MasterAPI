/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.resource;

import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.core.hibernate.entity.RestEntity;
import com.sdm.core.request.SyncRequest;
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
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;

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
    public IBaseResponse getAll() throws Exception {
        try {
            List<T> data = mainDAO.fetchAll();
            return new DefaultResponse(new ListResponse(data));
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }

    @Override
    public IBaseResponse getPaging(String filter, int pageId, int pageSize, String sortString) throws Exception {
        try {
            //Init Query
            String query = "FROM " + currentEntityClass.getSimpleName() + " WHERE deletedAt IS NULL";

            //Build Filter
            Map<String, Object> params = new HashMap<>();
            if (filter != null && filter.length() > 0) {
                query += " AND search LIKE :filter";
                params.put("filter", "%" + filter + "%");
            }

            //Retrieve Record Count
            String countQuery = "SELECT COUNT(*) " + query;
            long total = (long) mainDAO.createQuery(countQuery, params).getSingleResult();

            //Build SortMap
            if (sortString.length() > 0) {
                query += " ORDER BY ";
                String[] sorts = sortString.split(",");
                for (String sort : sorts) {
                    String[] sortParams = sort.trim().split(":", 2);
                    if (sortParams.length >= 2 && sortParams[1].equalsIgnoreCase("desc")) {
                        query += " " + sortParams[0] + " DESC";
                    } else {
                        query += " " + sortParams[0] + " ASC";
                    }
                }
            }

            //Calculate Start Index
            if (pageId <= 0) {
                pageId = 1;
            }
            int start = pageSize * (pageId - 1);

            //Retrieve Data
            List<T> data = mainDAO.createQuery(query, params, pageSize, start).getResultList();
            if (data == null) {
                return new DefaultResponse(new MessageResponse(204, ResponseType.WARNING,
                        "NO_DATA", "There is no data for your query string."));
            }
           
            PaginationResponse response = new PaginationResponse(data, total, pageId, pageSize);

            return new DefaultResponse(response);
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }

    @Override
    public IBaseResponse getById(PK id) throws Exception {
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
    public IBaseResponse remove(PK id) throws Exception {
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
    public DefaultResponse getStructure() throws Exception {
        T instance = currentEntityClass.newInstance();
        List<PropertiesResponse> properties = instance.getStructure();
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

    /*
    @Override
    public IBaseResponse queryData(QueryRequest request) throws Exception {
        try {
            if (!request.isValid()) {
                return new ErrorResponse(request.getErrors());
            }
            //Init Query String
            String query = "FROM " + currentEntityClass.getSimpleName() + " WHERE deletedAt IS NULL";

            //Build Where Condition
            Map<String, Object> params = new HashMap<>();
            if (request.getQuery() != null) {
                int i = 0;
                for (Condition condition : request.getQuery()) {
                    query += " " + condition.getLogic().getValue();
                    query += " " + condition.getColumn();
                    query += " " + condition.getExpression().getValue();
                    if (condition.getExpression() == Expression.IN || condition.getExpression() == Expression.NIN) {
                        query += " (:param_" + i + ")";
                        Set<String> values = new HashSet<>(Arrays.asList(condition.getValue().toString().split(",")));
                        params.put("param_" + i, values);
                    } else {
                        query += " :param_" + i;
                        params.put("param_" + i, condition.getValue());
                    }
                    i++;
                }
            }

            String countQuery = "SELECT count(*) " + query;

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

            //Calculate Start Index
            if (request.getPage() <= 0) {
                request.setPage(1);
            }
            int start = request.getSize() * (request.getPage() - 1);

            //Retrieve Data
            List data = mainDAO.createQuery(query, params, request.getSize(), start).getResultList();
            long total = (long) mainDAO.createQuery(countQuery, params).getSingleResult();
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
                data = output;
            }

            PaginationResponse response = new PaginationResponse(total, request.getPage(),
                    request.getSize(), request.getQuery(), request.getSort(), data);
            return new DefaultResponse(response);
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }*/
}
