/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.hibernate.dao;

import com.sdm.core.hibernate.HibernateConnector;
import com.sdm.core.hibernate.audit.AuditStorage;
import com.sdm.core.hibernate.audit.IAuthListener;
import com.sdm.core.hibernate.entity.AuthInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Htoonlin
 */
public class DefaultDAO {

    private static final Logger LOG = Logger.getLogger(DefaultDAO.class.getName());
    
    private Session mainSession;
    private IAuthListener dataListener;

    public DefaultDAO(IAuthListener listener) {
        this(HibernateConnector.getFactory().openSession(), listener);
    }

    public DefaultDAO(Session session, IAuthListener listener) {
        this.mainSession = session;
        this.dataListener = listener;
    }

    public Session getSession() {
        if (this.mainSession == null || !this.mainSession.isOpen()) {
            this.mainSession = HibernateConnector.getFactory().openSession();
        }
        return this.mainSession;
    }

    public void closeSession() {
        if (this.mainSession.isOpen()) {
            this.mainSession.close();
        }
        AuditStorage.INSTANCE.clean();
    }

    public void beginTransaction() {
        if (this.dataListener != null) {
            AuthInfo authInfo = this.dataListener.getAuthInfo();
            AuditStorage.INSTANCE.set(authInfo);            
        }
        LOG.info("Begin transaction");
        this.getSession().beginTransaction();
    }

    public void commitTransaction() {
        Transaction transaction = this.getSession().getTransaction();
        if (transaction != null) {
            transaction.commit();
        }
        LOG.info("Commint transaction");
        this.closeSession();
    }

    public void rollbackTransaction() {
        Transaction transaction = this.getSession().getTransaction();
        if (transaction != null) {
            transaction.rollback();
        }
        LOG.info("Rollback transaction");
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

    public Query createQueryByName(String queryName, Map<String, Object> params, int size, int start) {
        Query query = this.getSession().createNamedQuery(queryName);
        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                query.setParameter(param.getKey(), param.getValue());
            }
        }
        return query.setFirstResult(start).setMaxResults(size);
    }

    public Query createQueryByName(String queryName, Map<String, Object> params, int size) {
        Query query = this.getSession().createNamedQuery(queryName);
        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                query.setParameter(param.getKey(), param.getValue());
            }
        }
        return query.setMaxResults(size);
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

    public List<?> fetchByNamedQuery(String queryName, Map<String, Object> params, int size, int start) {
        List<?> queryList = this.createQueryByName(queryName, params, size, start).getResultList();
        if (queryList != null && queryList.size() > 0) {
            return queryList;
        }

        return new ArrayList<>();
    }

    public List<?> fetchByNamedQuery(String queryName, Map<String, Object> params, int size) {
        List<?> queryList = this.createQueryByName(queryName, params, size).getResultList();
        if (queryList != null && queryList.size() > 0) {
            return queryList;
        }

        return new ArrayList<>();
    }

    public List<?> fetchByNamedQuery(String queryName, Map<String, Object> params) {
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

    public <T extends Serializable> T fetchOneByNamedQuery(String queryName, Map<String, Object> params) {
        List<T> results = this.createQueryByName(queryName, params).setMaxResults(1).getResultList();
        if (results == null || results.size() < 1) {
            return null;
        }
        return (T) results.get(0);
    }

    public <T extends Serializable> T fetchOne(String hqlString, Map<String, Object> params) {
        List<T> results = this.createQuery(hqlString, params).setMaxResults(1).getResultList();
        if (results == null || results.size() < 1) {
            return null;
        }
        return (T) results.get(0);
    }

    
    protected void executeByNamedQuery(String queryName, Map<String, Object> params, boolean autoCommit) {
        if(autoCommit){
            beginTransaction();
        }
        this.createQueryByName(queryName, params).executeUpdate();
        
        if(autoCommit){
            commitTransaction();
        }
    }

    public void execute(String hqlString, Map<String, Object> params, boolean autoCommit) {
        if(autoCommit){
            beginTransaction();
        }
        
        this.createQuery(hqlString, params).executeUpdate();
        
        if(autoCommit){
            commitTransaction();
        }
    }
}
