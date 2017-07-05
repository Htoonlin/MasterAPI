/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.hibernate.dao;

import com.sdm.core.hibernate.HibernateConnector;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import javax.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Htoonlin
 */
public class DefaultDAO {

    private static final Logger LOG = Logger.getLogger(DefaultDAO.class.getName());
    private final Session mainSession;

    public DefaultDAO() {
        this(HibernateConnector.getFactory().openSession());
    }

    public DefaultDAO(Session session) {
        this.mainSession = session;
    }

    public Session getSession() {
        if (this.mainSession == null || !this.mainSession.isOpen()) {
            return HibernateConnector.getFactory().getCurrentSession();
        }

        return this.mainSession;
    }

    public void closeSession() {
        this.mainSession.close();
    }

    public void beginTransaction() {
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

    public List fetchByName(String queryName, Map<String, Object> params) {
        List queryList = this.createQueryByName(queryName, params).getResultList();
        if (queryList != null && queryList.size() > 0) {
            return queryList;
        }

        return new ArrayList();
    }

    public List fetch(String hqlString, Map<String, Object> params) {
        List queryList = this.createQuery(hqlString, params).getResultList();
        if (queryList != null && queryList.size() > 0) {
            return queryList;
        }

        return new ArrayList();
    }

    public <T> T fetchOneByName(String queryName, Map<String, Object> params) {
        List results = this.createQueryByName(queryName, params).getResultList();
        if (results == null || results.size() < 1) {
            return null;
        }
        return (T) results.get(0);
    }

    public <T> T fetchOne(String hqlString, Map<String, Object> params) {
        List results = this.createQuery(hqlString, params).getResultList();
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
