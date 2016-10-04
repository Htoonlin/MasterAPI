/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.dao;

import com.sdm.core.database.dao.RestDAO;
import com.sdm.master.entity.PermissionEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;

/**
 *
 * @author Htoonlin
 */
public class PermissionDAO extends RestDAO<PermissionEntity> {
    private static final Logger LOG = Logger.getLogger(PermissionDAO.class.getName());

    private final String GET_BY_ROLE = "FROM PermissionEntity p WHERE p.roleId = :roleId AND p.deletedAt IS NULL";
    private final String CHECK_ROLE = "FROM PermissionEntity p WHERE p.roleId = :roleId AND p.resourceClass = :class "
            + "AND p.resoureMethod = :method AND p.requestMethod like :request AND p.deletedAt IS NULL";

    public PermissionDAO(HttpSession httpSession) {
        super(PermissionEntity.class, httpSession);
    }

    public PermissionDAO(Session session, HttpSession httpSession) {
        super(session, PermissionEntity.class, httpSession);
    }

    public List<PermissionEntity> fetchByRole(int roleId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("roleId", roleId);
        return this.fetch(GET_BY_ROLE, params);
    }

    public boolean checkRole(int roleId, String resourceClass, String resourceMethod, String httpMethod) {
        Map<String, Object> params = new HashMap<>();
        params.put("roleId", roleId);
        params.put("class", resourceClass);
        params.put("method", resourceMethod);
        params.put("request", httpMethod);
        try {
            PermissionEntity entity = this.fetchOne(CHECK_ROLE, params);
            return (entity != null) && (entity.getRoleId() == roleId) 
                    && (entity.getResourceClass().equals(resourceClass))
                    && (entity.getResourceMethod().equals(resourceMethod)
                    && (entity.getRequestMethod().equalsIgnoreCase(httpMethod)));
        }catch(Exception e){
            LOG.error(e);
        }
        return false;
    }
}
