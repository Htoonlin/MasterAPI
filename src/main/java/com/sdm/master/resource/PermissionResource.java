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
import com.sdm.core.response.model.RouteInfo;
import com.sdm.master.dao.PermissionDAO;
import com.sdm.master.entity.PermissionEntity;

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
		HashMap<String, List<RouteInfo>> resources = new HashMap<>();
		for (Class clsResource : config.getClasses()) {
			Resource resource = Resource.from(clsResource);
			if (resource != null) {
				String resourceName = clsResource.getName();
				List<RouteInfo> routes = collectRoute(resource, "/");
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

	@Override
	protected Logger getLogger() {
		return PermissionResource.LOG;
	}
}
