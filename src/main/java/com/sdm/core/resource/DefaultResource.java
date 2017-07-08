/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.glassfish.jersey.server.model.Invocable;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.ResourceMethod;

import com.sdm.core.Globalizer;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.ListResponse;
import com.sdm.core.response.MessageResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.core.response.model.RouteInfo;

/**
 *
 * @author Htoonlin
 */
public class DefaultResource implements IBaseResource {

    private static final Logger LOG = Logger.getLogger(DefaultResource.class.getName());

    @Context
    private UriInfo uriInfo;

    @Inject
    private HttpSession httpSession;

    @Override
    public HttpSession getHttpSession() {
        return this.httpSession;
    }

    @Override
    public long getUserId() {
        try {
            return (long) this.getHttpSession().getAttribute(Globalizer.SESSION_USER_ID);
        } catch (Exception e) {
            LOG.error("There is no session. <" + e.getLocalizedMessage() + ">");
            return 0;
        }
    }

    @Override
    public UriInfo getUriInfo() {
        return uriInfo;
    }

    @Override
    public void setUriInfo(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    public String getBaseURI() {
        String[] baseURI = getUriInfo().getAbsolutePath().toString().split("/api/", 2);
        if (baseURI.length > 1) {
            return baseURI[0] + "/";
        }
        return getUriInfo().getAbsolutePath().toString();
    }

    protected List<RouteInfo> collectRoute(Resource resource, String basePath) {
        List<RouteInfo> routeList = new ArrayList<>();
        String parentPath = "";
        for (ResourceMethod method : resource.getResourceMethods()) {
            Invocable invocable = method.getInvocable();

            //Skip Routes
            if (invocable.getHandlingMethod().getName().equalsIgnoreCase("getRoutes")) {
                continue;
            }

            RouteInfo route = new RouteInfo();
            //Set Resource Class
            route.setResourceClass(this.getClass().getName());

            //Set Resource Method            
            route.setResourceMethod(invocable.getHandlingMethod().getName());

            //Set Path
            if (!basePath.endsWith("/")) {
                basePath += "/";
            }
            parentPath = (basePath + resource.getPath()).replaceAll("//", "/");
            route.setPath(parentPath);

            //Set HTTP Method
            route.setMethod(method.getHttpMethod());

            routeList.add(route);
        }

        for (Resource childResource : resource.getChildResources()) {
            routeList.addAll(collectRoute(childResource, parentPath));
        }

        return routeList;
    }

    @Override
    public IBaseResponse getRoutes() {
        try {
            Resource resource = Resource.from(this.getClass());
            if (resource == null) {
                return new MessageResponse(204, ResponseType.WARNING,
                        "There is no data for your request.");
            }
            List<RouteInfo> routeList = collectRoute(resource, "/");
            ListResponse<RouteInfo> response = new ListResponse<RouteInfo>(routeList);
			return response;
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }
}
