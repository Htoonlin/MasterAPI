/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.database.dao;

import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import org.hibernate.Session;

/**
 *
 * @author Htoonlin
 * @param <T>
 */
public interface IBaseDAO<T> {
    Session getSession();
    void closeSession();
    
    void beginTransaction();

    void commitTransaction();

    void rollbackTransaction();

    Query createQuery(String hqlString, Map<String, Object> params);

    Query createQuery(String hqlString, Map<String, Object> params, int size);

    Query createQuery(String hqlString, Map<String, Object> params, int size, int start);
    
    List<T> fetch(String hqlString, Map<String, Object> params) throws Exception;

    T fetchOne(String hqlString, Map<String, Object> params) throws Exception;

    public void execute(String hqlString, Map<String, Object> params);
}
