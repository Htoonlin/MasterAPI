/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.core.resource.RestResource;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.ListResponse;
import com.sdm.core.response.MessageResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.master.dao.PermissionDAO;
import com.sdm.master.entity.PermissionEntity;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import org.apache.log4j.Logger;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Htoonlin
 */
@Path("permission")
public class PermissionResource extends RestResource<PermissionEntity, Long> {

    private static final Logger LOG = Logger.getLogger(PermissionResource.class.getName());

    private PermissionDAO mainDAO;

    @PostConstruct
    protected void init() {
        if (this.mainDAO == null) {
            mainDAO = new PermissionDAO(getUserId());
        }
    }

    @Override
    protected RestDAO getDAO() {
        return this.mainDAO;
    }

    @POST
    @Path("/multi")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse multiPermissions(List<PermissionEntity> permissionList) throws Exception {
        try {
            mainDAO.beginTransaction();
            for (PermissionEntity permission : permissionList) {
                mainDAO.insert(permission, false);
            }
            mainDAO.commitTransaction();

            return new MessageResponse(202, ResponseType.SUCCESS, "We updated the record with your request successfully.");
        } catch (Exception e) {
            mainDAO.rollbackTransaction();
            LOG.error(e);
            throw e;
        }
    }

    @GET
    @Path("/role/{roleId:\\d+}")
    public IBaseResponse getPermissionsByRole(@PathParam("roleId") int roleId) throws Exception {
        try {
            List<PermissionEntity> permissions = mainDAO.fetchByRole(roleId);
            return new DefaultResponse(new ListResponse(permissions));
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }

    @Override
    protected Logger getLogger() {
        return PermissionResource.LOG;
    }
}
