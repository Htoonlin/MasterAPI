/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.hibernate.dao;

import com.sdm.core.hibernate.HibernateConnector;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Htoonlin
 * @param <T>
 */
public class DefaultDAO<T extends Serializable> implements IBaseDAO<T>{
    protected Session mainSession;
    protected final Class<T> entityClass;

    public DefaultDAO(Class<T> entityClass) {
        this(HibernateConnector.getFactory().openSession(), entityClass);
    }

    public DefaultDAO(Session session, Class<T> entityClass) {
        mainSession = session;
        this.entityClass = entityClass;      
    }

    @Override
    public Session getSession() {
        return mainSession;
    }

    @Override
    public void closeSession() {
        if (mainSession.isOpen()) {
            mainSession.close();
        }
    }

    @Override
    public void beginTransaction() {
        if (mainSession == null || !mainSession.isOpen()) {
            mainSession = HibernateConnector.getFactory().openSession();
        }
        mainSession.beginTransaction();
    }

    @Override
    public void commitTransaction() {
        Transaction transaction = mainSession.getTransaction();
        if (transaction != null) {
            transaction.commit();
        }

        this.closeSession();
    }

    @Override
    public void rollbackTransaction() {
        Transaction transaction = mainSession.getTransaction();
        if (transaction != null) {
            transaction.rollback();
        }

        this.closeSession();
    }

    @Override
    public Query createQuery(String hqlString, Map<String, Object> params) {
        Query query = mainSession.createQuery(hqlString);        
        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                query.setParameter(param.getKey(), param.getValue());
            }
        }
        return query;
    }

    @Override
    public Query createQuery(String hqlString, Map<String, Object> params, int size) {
        Query query = this.createQuery(hqlString, params);
        return query.setMaxResults(size);
    }

    @Override
    public Query createQuery(String hqlString, Map<String, Object> params, int size, int start) {
        Query query = this.createQuery(hqlString, params);
        return query.setFirstResult(start).setMaxResults(size);
    }
    
    @Override
    public List<T> fetch(String hqlString, Map<String, Object> params) throws Exception {
        try {
            List<T> queryList = this.createQuery(hqlString, params).getResultList();
            if (queryList != null && queryList.size() > 0) {
                return (List<T>) queryList;
            }
        } catch (Exception e) {
            throw e;
        }

        return new ArrayList();
    }

    @Override
    public T fetchOne(String hqlString, Map<String, Object> params) throws Exception {
        try {
            List<T> results = this.createQuery(hqlString, params).getResultList();
            if (results == null || results.size() < 1) {
                return null;
            }
            return results.get(0);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void execute(String hqlString, Map<String, Object> params) {
        try {
            this.createQuery(hqlString, params).executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        this.closeSession();
        super.finalize();
    }
}
