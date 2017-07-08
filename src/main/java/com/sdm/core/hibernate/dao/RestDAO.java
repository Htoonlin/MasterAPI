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

	protected final long USER_ID;

	public RestDAO(String entityName, long userId) {
		super();
		this.ENTITY_NAME = entityName;
		this.USER_ID = userId;
	}

	public RestDAO(Session session, String entityName, long userId) {
		super(session);
		this.ENTITY_NAME = entityName;
		this.USER_ID = userId;
	}

	protected String fetchHQL() {
		return "FROM " + this.ENTITY_NAME + " WHERE 1 = 1";
	}

	protected String filterHQL(String filter, Map<String, Object> params) {
		String query = fetchHQL();
		if (filter != null && filter.length() > 0) {
			params = new HashMap<>();
			params.put("filter", "%" + filter + "%");
			query += " AND " + GLOBAL_FILTER + " LIKE :filter";
		}
		return query;
	}

	public long getTotal(String filter) {
		Map<String, Object> params = new HashMap<>();
		String hql = "SELECT COUNT(*) " + this.filterHQL(filter, params);
		return (long) this.createQuery(hql, params).getSingleResult();
	}

	public List paging(String filter, int pageId, int pageSize, String sortString) {
		Map<String, Object> params = new HashMap<>();
		String hql = this.filterHQL(filter, params);

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

	public List fetchAll() {
		List queryList = this.fetch(fetchHQL(), null);
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

	public void delete(Serializable id, boolean autoCommit) {
		try {
			Map<String, Object> entity = this.fetchById(id);
			if (entity == null) {
				throw new Exception("There is no data to remove.");
			}
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
