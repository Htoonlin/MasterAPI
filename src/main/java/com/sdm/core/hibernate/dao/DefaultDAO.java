/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.hibernate.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sdm.core.hibernate.HibernateConnector;
import com.sdm.core.hibernate.audit.AuditStorage;

/**
 *
 * @author Htoonlin
 */
public class DefaultDAO {

    private final Session mainSession;
    protected final int USER_ID;

    public DefaultDAO(int userId) {
        this(HibernateConnector.getFactory().openSession(), userId);
    }

    public DefaultDAO(Session session, int userId) {
        this.USER_ID = userId;
        this.mainSession = session;
    }

    public Session getSession() {
        if (this.mainSession == null || !this.mainSession.isOpen()) {
            return HibernateConnector.getFactory().getCurrentSession();
        }
        return this.mainSession;
    }

    public int getUserId() {
        return this.USER_ID;
    }

    public void closeSession() {
        if (this.mainSession.isOpen()) {
            this.mainSession.close();
        }
        AuditStorage.INSTANCE.clean();
    }

    public void beginTransaction() {
        AuditStorage.INSTANCE.set(this.USER_ID);
        this.getSession().beginTransaction();
    }

    public void commitTransaction() {
        Transaction transaction = this.getSession().getTransaction();
        if (transaction != null) {
            transaction.commit();
        }

        this.closeSession();
    }

    public void rollbackTransaction() {
        Transaction transaction = this.getSession().getTransaction();
        if (transaction != null) {
            transaction.rollback();
        }

        this.closeSession();
    }

    public Query createQuery(String hqlString, Map<String, Object> params) {
        Query query = this.getSession().createQuery(hqlString);
        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                query.setParameter(param.getKey(), param.getValue());
            }
        }
        return query;
    }

    public Query createQueryByName(String queryName, Map<String, Object> params) {
        Query query = this.getSession().createNamedQuery(queryName);
        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                query.setParameter(param.getKey(), param.getValue());
            }
        }
        return query;
    }

    public Query createQuery(String hqlString, Map<String, Object> params, int size) {
        Query query = this.createQuery(hqlString, params);
        return query.setMaxResults(size);
    }

    public Query createQuery(String hqlString, Map<String, Object> params, int size, int start) {
        Query query = this.createQuery(hqlString, params);
        return query.setFirstResult(start).setMaxResults(size);
    }

    public List<?> fetchByName(String queryName, Map<String, Object> params) {
        List<?> queryList = this.createQueryByName(queryName, params).getResultList();
        if (queryList != null && queryList.size() > 0) {
            return queryList;
        }

        return new ArrayList<>();
    }

    public List<?> fetch(String hqlString, Map<String, Object> params) {
        List<?> queryList = this.createQuery(hqlString, params).getResultList();
        if (queryList != null && queryList.size() > 0) {
            return queryList;
        }

        return new ArrayList<>();
    }

    public <T extends Serializable> T fetchOneByName(String queryName, Map<String, Object> params) {
        List<T> results = this.createQueryByName(queryName, params).getResultList();
        if (results == null || results.size() < 1) {
            return null;
        }
        return (T) results.get(0);
    }

    public <T extends Serializable> T fetchOne(String hqlString, Map<String, Object> params) {
        List<T> results = this.createQuery(hqlString, params).getResultList();
        if (results == null || results.size() < 1) {
            return null;
        }
        return (T) results.get(0);
    }

    public void executeByName(String queryName, Map<String, Object> params) {
        this.createQueryByName(queryName, params).executeUpdate();
    }

    public void execute(String hqlString, Map<String, Object> params) {
        this.createQuery(hqlString, params).executeUpdate();
    }
}
