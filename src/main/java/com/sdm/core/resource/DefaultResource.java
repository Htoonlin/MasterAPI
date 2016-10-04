/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.resource;

import com.sdm.core.Globalizer;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.ListResponse;
import com.sdm.core.response.MessageResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.core.response.RouteResponse;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.glassfish.jersey.server.model.Invocable;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.ResourceMethod;
import org.apache.log4j.Logger;

/**
 *
 * @author Htoonlin
 */
public class DefaultResource implements IBaseResource{

    private static final Logger logger = Logger.getLogger(DefaultResource.class.getName());

    @Context
    private UriInfo uriInfo;

    @Inject
    private HttpSession httpSession;
    
    @Override
    public HttpSession getHttpSession(){
        return this.httpSession;
    }
    
    @Override
    public int getUserId() {
        try {
            return (int) this.getHttpSession().getAttribute(Globalizer.SESSION_USER_ID);
        } catch (Exception e) {
            logger.error(e);
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

    protected List<RouteResponse> collectRoute(Resource resource, String basePath) {
        List<RouteResponse> routeList = new ArrayList<>();
        String parentPath = "";
        for (ResourceMethod method : resource.getResourceMethods()) {
            RouteResponse route = new RouteResponse();          
            Invocable invocable = method.getInvocable();
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

            //Set Request List
            List<String> requestList = new ArrayList<>();
            for (MediaType consumedType : method.getConsumedTypes()) {
                if (!requestList.contains(consumedType.toString())) {
                    requestList.add(consumedType.toString());
                }
            }
            route.setRequest(requestList);

            //Set Response List
            List<String> responseList = new ArrayList<>();
            for (MediaType producedType : method.getProducedTypes()) {
                if (!responseList.contains(producedType.toString())) {
                    responseList.add(producedType.toString());
                }
            }
            route.setResponse(responseList);
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
                return new DefaultResponse(new MessageResponse(204, ResponseType.WARNING,
                        "NO_DATA", "There is no data for your request."));
            }
            List<RouteResponse> routeList = collectRoute(resource, "/");
            return new DefaultResponse(new ListResponse(routeList));
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }
}
