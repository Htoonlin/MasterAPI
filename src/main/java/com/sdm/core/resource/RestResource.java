/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.resource;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.core.hibernate.entity.DefaultEntity;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.ErrorResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.core.response.model.ListModel;
import com.sdm.core.response.model.MessageModel;
import com.sdm.core.response.model.Pagination;
import com.sdm.core.response.model.UIProperty;

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

	protected Class<T> getEntityClass() {
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		Class<T> entityClass = (Class<T>) type.getActualTypeArguments()[0];
		return entityClass;
	}

	@Override
	public IBaseResponse getNamedQueries() throws Exception {
		T instance = getEntityClass().newInstance();
		return new DefaultResponse<>(instance.getQueries());
	}

	@Override
	public IBaseResponse postQuery(String queryName, Map<String, Object> params) throws Exception {
		List<HashMap<String, Object>> data = (List<HashMap<String, Object>>) getDAO().fetchByName(queryName, params);
		ListModel content = new ListModel<>(data);
		return new DefaultResponse<>(content);
	}

	@Override
	public IBaseResponse getAll() throws Exception {
		try {
			List<T> data = (List<T>) getDAO().fetchAll();
			ListModel<T> content = new ListModel<T>(data);
			return new DefaultResponse<>(content);
		} catch (Exception e) {
			getLogger().error(e);
			throw e;
		}
	}

	@Override
	public IBaseResponse getPaging(String filter, int pageId, int pageSize, String sortString) throws Exception {
		try {
			long total = getDAO().getTotal(filter);
			List<T> data = (List<T>) getDAO().paging(filter, pageId, pageSize, sortString);

			if (data == null) {
				MessageModel message = new MessageModel(204, "No Data", "There is no data for your query string.");
				return new DefaultResponse<>(message);
			}

			Pagination<T> content = new Pagination<T>(data, total, pageId, pageSize);

			return new DefaultResponse<>(content);
		} catch (Exception e) {
			getLogger().error(e);
			throw e;
		}
	}

	@Override
	public IBaseResponse getById(PK id) throws Exception {
		T data = getDAO().fetchById(id);
		if (data == null) {
			MessageModel message = new MessageModel(204, "No Data", "There is no data for your request.");
			return new DefaultResponse<>(message);
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
			return new DefaultResponse<T>(201, ResponseType.SUCCESS, entity);
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

			T dbEntity = getDAO().fetchById(id);
			if (dbEntity == null) {
				MessageModel message = new MessageModel(204, "No Data", "There is no data for your request.");
				return new DefaultResponse<>(message);
			}

			T entity = getDAO().update(request, true);
			return new DefaultResponse<T>(202, ResponseType.SUCCESS, entity);
		} catch (Exception e) {
			getLogger().error(e);
			throw e;
		}
	}

	@Override
	public IBaseResponse remove(PK id) throws Exception {
		try {
			MessageModel message = new MessageModel(204, "No Data", "There is no data for your request.");
			T entity = getDAO().fetchById(id);
			if (entity == null) {
				return new DefaultResponse<>(message);
			}

			getDAO().delete(entity, true);
			message = new MessageModel(202, "Deleted", "We deleted the record with your request successfully.");
			return new DefaultResponse<>(202, ResponseType.SUCCESS, message);
		} catch (Exception e) {
			getLogger().error(e);
			throw e;
		}
	}

	@Override
	public IBaseResponse getStructure() throws Exception {
		T instance = getEntityClass().newInstance();
		List<UIProperty> properties = instance.getStructure();
		ListModel<UIProperty> content = new ListModel<UIProperty>(properties);
		return new DefaultResponse<>(content);
	}

	/*
	 * @Override public IBaseResponse syncData(SyncRequest request) throws Exception
	 * { try { if (!request.isValid()) { return new
	 * ErrorResponse(request.getErrors()); }
	 * 
	 * getDAO().beginTransaction(); SyncResponse response = new SyncResponse();
	 * //Retrieve new/update/remove data from server List<Map> responseInsertList =
	 * getDAO().fetchNewData(new Date(request.getLastSync()), Sync.INSERT);
	 * List<Map> responseUpdateList = getDAO().fetchNewData(new
	 * Date(request.getLastSync()), Sync.UPDATE); List<Map> responseRemoveList =
	 * getDAO().fetchNewData(new Date(request.getLastSync()), Sync.REMOVE);
	 * response.setInsert(responseInsertList);
	 * response.setUpdate(responseUpdateList);
	 * response.setRemove(responseRemoveList);
	 * 
	 * //Delete Data by Client Request if (request.getRemove() != null) {
	 * List<Map<String, Object>> removeList = (List<Map<String, Object>>)
	 * request.getRemove(); for (Map<String, Object> rEntity : removeList) { if
	 * (rEntity.containsKey(DefaultProperty.ID)) { PK removeId = (PK)
	 * rEntity.get(DefaultProperty.ID); getDAO().delete(removeId, false);
	 * 
	 * //Clean insert list by Remove Entity cleanSyncList(response.getInsert(),
	 * rEntity);
	 * 
	 * //Clean update list by Remove Entity cleanSyncList(response.getUpdate(),
	 * rEntity); } } }
	 * 
	 * //Update Data from Client to Server if (request.getUpdate() != null) {
	 * List<Map<String, Object>> updateList = (List<Map<String, Object>>)
	 * request.getUpdate(); for (Map uEntity : updateList) { if
	 * (!uEntity.containsKey(DefaultProperty.MODIFIED_AT)) {
	 * uEntity.put(DefaultProperty.MODIFIED_AT, new Date()); } //Check DB Data Map
	 * dbEntity = getDAO().fetchById((PK) uEntity.get(DefaultProperty.ID)); if
	 * (dbEntity == null) { continue; }
	 * 
	 * Date dbDate = (Date) dbEntity.get(DefaultProperty.MODIFIED_AT); Date reqDate
	 * = (Date) uEntity.get(DefaultProperty.MODIFIED_AT); if
	 * (dbDate.before(reqDate)) { getDAO().update(uEntity, false);
	 * 
	 * //Clean insert list by Update Entity cleanSyncList(response.getInsert(),
	 * uEntity);
	 * 
	 * //Clean update list by Update Entity cleanSyncList(response.getUpdate(),
	 * uEntity); } } }
	 * 
	 * //Insert Data from Client to Server if (request.getInsert() != null) {
	 * List<Map> insertList = (List<Map>) request.getInsert(); for (Map iEntity :
	 * insertList) { Map responseEntity = getDAO().insert(iEntity, false); //Reponse
	 * Inserted ID to Update responseInsertList.add(responseEntity); }
	 * response.setInsert(insertList); } getDAO().commitTransaction();
	 * 
	 * return new DefaultResponse(response); } catch (Exception e) {
	 * getLogger().error(e); getDAO().rollbackTransaction(); throw e; } }
	 * 
	 * private void cleanSyncList(List<Map> sourceList, Map filter) { for (Map
	 * source : sourceList) { if (!filter.containsKey(DefaultProperty.MODIFIED_AT))
	 * { filter.put(DefaultProperty.MODIFIED_AT, new Date()); } PK sourceId = (PK)
	 * source.get(DefaultProperty.ID); PK filterId = (PK)
	 * filter.get(DefaultProperty.ID); Date sourceModifiedAt = (Date)
	 * source.get(DefaultProperty.MODIFIED_AT); Date filterModifiedAt = (Date)
	 * filter.get(DefaultProperty.MODIFIED_AT);
	 * 
	 * if (sourceId.equals(filterId) && sourceModifiedAt.before(filterModifiedAt)) {
	 * sourceList.remove(source); break; } } }
	 */
}
