/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.hibernate.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author htoonlin
 */
public interface IRestDAO {

    List fetchAll() throws Exception;

    <T extends Serializable> T fetchEntityById(Serializable id) throws Exception;

    HashMap fetchById(Serializable id);

    List fetchNewData(Date lastSync, Sync status) throws Exception;

    long getTotal(String filter);

    <T extends Serializable> T insert(T entity, boolean autoCommit) throws Exception;

    Map<String, Object> insert(Map<String, Object> entity, boolean autoCommit) throws Exception;

    List paging(String filter, int pageId, int pageSize, String sortString);

    <T extends Serializable> T update(T entity, boolean autoCommit) throws Exception;

    Map<String, Object> update(Map<String, Object> entity, boolean autoCommit) throws Exception;

    void delete(Serializable id, boolean autoCommit) throws Exception;
}
