/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.glassfish.jersey.server.model.Resource;

import com.sdm.ApplicationConfig;
import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.core.resource.RestResource;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.core.response.model.ListModel;
import com.sdm.core.response.model.RouteModel;
import com.sdm.master.dao.PermissionDAO;
import com.sdm.master.entity.PermissionEntity;
import com.sdm.master.request.PermissionRouteRequest;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;

/**
 *
 * @author Htoonlin
 */
@Path("permissions")
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

    @GET
    @Path("/routes")
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse getAllRoutes() {
        ApplicationConfig config = new ApplicationConfig();
        HashMap<String, List<RouteModel>> resources = new HashMap<>();
        for (Class clsResource : config.getClasses()) {
            Resource resource = Resource.from(clsResource);
            if (resource != null) {
                String resourceName = clsResource.getName();
                List<RouteModel> routes = collectRoute(resource, "/");
                resources.put(resourceName, routes);
            }
        }
        return new DefaultResponse(200, ResponseType.SUCCESS, resources);
    }

    @GET
    @Path("/role/{roleId:\\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse getPermissionsByRole(@PathParam("roleId") int roleId) throws Exception {
        DefaultResponse response = this.validateCache();
        if (response != null) {
            return response;
        }

        try {
            List<PermissionEntity> permissions = mainDAO.fetchByRole(roleId);
            ListModel<PermissionEntity> content = new ListModel<PermissionEntity>(permissions);
            response = new DefaultResponse<>(content);
            response.setHeaders(this.buildCache());
            return response;
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }

    @POST
    @Path("/modify")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse PermissionByList(List<PermissionRouteRequest> permissions) {
        DefaultResponse response = this.validateCache();
        if (response != null) {
            return response;
        }

        try {
            List<PermissionEntity> processedList = new ArrayList<>();
            getDAO().beginTransaction();

            for (PermissionRouteRequest pr : permissions) {
                PermissionEntity p = pr.getPermission();
                PermissionEntity dbPermission = getDAO().fetchById(p.getId());

                //Insert Old Data Null And Checked true 
                if (dbPermission == null && pr.getChecked()) {
                    PermissionEntity inserted = getDAO().insert(p, false);
                    processedList.add(inserted);

                } //Delete Old Data Is not Null And Checked false
                else if (dbPermission != null && !pr.getChecked()) {
                    getDAO().delete(dbPermission, false);
                }
            }

            getDAO().commitTransaction();
            this.modifiedResource();

            ListModel<PermissionEntity> content = new ListModel<>(processedList);
            return new DefaultResponse(201, ResponseType.SUCCESS, content);
        } catch (Exception e) {
            getDAO().rollbackTransaction();
            LOG.error(e);
            throw e;
        }
    }

    @Override
    protected Logger getLogger() {
        return PermissionResource.LOG;
    }
}
