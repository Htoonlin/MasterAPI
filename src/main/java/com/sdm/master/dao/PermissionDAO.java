/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.dao;

import com.sdm.core.hibernate.audit.IUserListener;
import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.master.entity.PermissionEntity;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *
 * @author Htoonlin
 */
public class PermissionDAO extends RestDAO {

    private static final Logger LOG = Logger.getLogger(PermissionDAO.class.getName());

    public PermissionDAO(IUserListener listener) {
        super(PermissionEntity.class.getName(), listener);
    }

    public PermissionDAO(Session session, IUserListener listener) {
        super(session, PermissionEntity.class.getName(), listener);
    }

    @Deprecated
    public List<PermissionEntity> fetchByRole(int roleId) throws SQLException {
        Map<String, Object> params = new HashMap<>();
        params.put("roleId", roleId);
        return (List<PermissionEntity>) this.fetchByNamedQuery("PermissionEntity.GET_BY_ROLE", params);
    }

    @Deprecated
    public boolean checkRole(int roleId, String resourceClass, String resourceMethod, String httpMethod) {
        Map<String, Object> params = new HashMap<>();
        params.put("roleId", roleId);
        params.put("class", resourceClass);
        params.put("method", resourceMethod);
        params.put("request", httpMethod);
        try {
            PermissionEntity entity = this.fetchOneByNamedQuery("PermissionEntity.CHECK_ROLE", params);
            return (entity != null) && (entity.getRoleId() == roleId)
                    && (entity.getResourceClass().equals(resourceClass))
                    && (entity.getResourceMethod().equals(resourceMethod)
                    && (entity.getRequestMethod().equalsIgnoreCase(httpMethod)));
        } catch (Exception e) {
            LOG.error(e);
        }
        return false;
    }
}
