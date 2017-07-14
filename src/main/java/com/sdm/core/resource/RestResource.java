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

import javax.annotation.PreDestroy;

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
	public IBaseResponse getNamedQueries() throws Exception {
		DefaultResponse response = this.validateCache();
		// Cache validation
		if (response != null) {
			return response;
		}

		T instance = getEntityClass().newInstance();
		response = new DefaultResponse<>(instance.getQueries());
		// Set Cache Header Info
		response.setHeaders(this.buildCache());
		return response;
	}

	@Override
	public IBaseResponse postQuery(String queryName, Map<String, Object> params) throws Exception {
		DefaultResponse response = this.validateCache();
		// Cache validation
		if (response != null) {
			return response;
		}

		List<HashMap<String, Object>> data = (List<HashMap<String, Object>>) getDAO().fetchByName(queryName, params);
		ListModel content = new ListModel<>(data);
		response = new DefaultResponse<>(content);
		response.setHeaders(this.buildCache());
		return response;
	}

	@Override
	public IBaseResponse getAll() throws Exception {
		DefaultResponse response = this.validateCache();
		// Cache validation
		if (response != null) {
			return response;
		}

		try {
			List<T> data = (List<T>) getDAO().fetchAll();
			ListModel<T> content = new ListModel<T>(data);
			response = new DefaultResponse<>(content);
			response.setHeaders(this.buildCache());
			return response;
		} catch (Exception e) {
			getLogger().error(e);
			throw e;
		}
	}

	@Override
	public IBaseResponse getPaging(String filter, int pageId, int pageSize, String sortString) throws Exception {
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

			response = new DefaultResponse<>(content);
			response.setHeaders(this.buildCache());
			return response;
		} catch (Exception e) {
			getLogger().error(e);
			throw e;
		}
	}

	@Override
	public IBaseResponse getById(PK id) throws Exception {
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
		response = new DefaultResponse<T>(data);
		response.setHeaders(this.buildCache());
		return response;
	}

	@Override
	public IBaseResponse create(T request) throws Exception {
		try {
			if (!request.isValid()) {
				return new ErrorResponse(request.getErrors());
			}
			T entity = getDAO().insert(request, true);
			this.modifiedResource();
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
			this.modifiedResource();
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
			this.modifiedResource();

			message = new MessageModel(202, "Deleted", "We deleted the record with your request successfully.");
			return new DefaultResponse<>(202, ResponseType.SUCCESS, message);
		} catch (Exception e) {
			getLogger().error(e);
			throw e;
		}
	}

	@Override
	public IBaseResponse getStructure() throws Exception {
		DefaultResponse response = this.validateCache();
		// Cache validation
		if (response != null) {
			return response;
		}

		T instance = getEntityClass().newInstance();
		List<UIProperty> properties = instance.getStructure();
		ListModel<UIProperty> content = new ListModel<UIProperty>(properties);

		response = new DefaultResponse<>(content);
		// Cache will hold a week of entity structure
		response.setHeaders(this.buildCache(3600 * 24 * 7));
		return response;
	}
}
