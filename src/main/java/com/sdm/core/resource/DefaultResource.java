/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.resource;

import com.sdm.Constants;
import com.sdm.core.Setting;
import com.sdm.core.hibernate.audit.IAuthListener;
import com.sdm.core.hibernate.entity.AuthInfo;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.core.response.model.ListModel;
import com.sdm.core.response.model.MessageModel;
import com.sdm.core.response.model.RouteModel;
import com.sdm.core.response.model.RouteParamModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.model.Invocable;
import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.ResourceMethod;

/**
 *
 * @author Htoonlin
 */
public class DefaultResource implements IBaseResource, IAuthListener {

    private static final Logger LOG = Logger.getLogger(DefaultResource.class.getName());

    protected Logger getLogger() {
        return Logger.getLogger(this.getClass());
    }
    
    @Context
    private UriInfo uriInfo;

    @Context
    private Request request;

    /**
     * Caching Start
     */
    private static String eTag = UUID.randomUUID().toString();
    private static Date lastModified = new Date();

    protected void modifiedResource() {
        eTag = UUID.randomUUID().toString();
        lastModified = new Date();
    }

    protected DefaultResponse<MessageModel> validateCache() {
        return this.validateCache(Setting.getInstance().getInt(Setting.CC_MAX_AGE));
    }

    protected DefaultResponse<MessageModel> validateCache(int cacheAge) {
        ResponseBuilder builder = request.evaluatePreconditions(lastModified);
        if (builder != null) {
            MessageModel message = new MessageModel(HttpStatus.SC_NOT_MODIFIED, "NO_CHANGE",
                    "There is no any changes for your request.");
            DefaultResponse<MessageModel> response = new DefaultResponse<>(HttpStatus.SC_NOT_MODIFIED,
                    ResponseType.INFO, message);
            response.setHeaders(this.buildCache(cacheAge));
            return response;
        }
        return null;
    }

    protected Map<String, Object> buildCache() {
        return this.buildCache(Setting.getInstance().getInt(Setting.CC_MAX_AGE));
    }

    protected Map<String, Object> buildCache(int cacheAge) {
        // Build CacheControl
        CacheControl cc = new CacheControl();
        cc.setMaxAge(cacheAge);
        cc.setNoStore(false);
        cc.setPrivate(true);
        cc.setNoTransform(false);

        // Build Expire
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.SECOND, cacheAge);

        Map<String, Object> cacheHeader = new HashMap<>();
        cacheHeader.put(HttpHeaders.CACHE_CONTROL, cc);
        cacheHeader.put(HttpHeaders.EXPIRES, cal.getTime());
        cacheHeader.put(HttpHeaders.ETAG, new EntityTag(eTag));
        cacheHeader.put(HttpHeaders.LAST_MODIFIED, lastModified);
        return cacheHeader;
    }
    
     /**
     * Caching End
     */
    
    /**
     * Auth Info
     * @return AuthInfo
     */
    @Override
    public AuthInfo getAuthInfo(){
        ContainerRequest container = (ContainerRequest) request;
        if(container.getPropertyNames().contains(Constants.REQUEST_TOKEN)){
            try{
                return (AuthInfo) container.getProperty(Constants.REQUEST_TOKEN);
            }catch(Exception ex){
                LOG.error("There is no authorization token.");
            }
        }
        
        return null;
    }
    
    protected long getCurrentUserId(){
        return getAuthInfo().getUserId();
    }

    @Override
    public UriInfo getUriInfo() {
        return uriInfo;
    }

    @Override
    public void setUriInfo(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    protected List<RouteModel> collectRoute(Resource resource, String basePath, Class clsResource) {
        List<RouteModel> routeList = new ArrayList<>();
        String parentPath = "";
        for (ResourceMethod method : resource.getResourceMethods()) {
            Invocable invocable = method.getInvocable();

            // Skip Routes
            if (invocable.getHandlingMethod().getName().equalsIgnoreCase("getRoutes")) {
                continue;
            }

            RouteModel route = new RouteModel();
            // Set Resource Class
            if (clsResource != null) {
                route.setResourceClass(clsResource.getName());
            } else {
                route.setResourceClass(this.getClass().getName());
            }

            // Set Resource Method
            route.setResourceMethod(invocable.getHandlingMethod().getName());

            // Set Path
            if (!basePath.endsWith("/")) {
                basePath += "/";
            }
            parentPath = (basePath + resource.getPath()).replaceAll("//", "/");
            route.setPath(parentPath);

            // Set HTTP Method
            route.setMethod(method.getHttpMethod());

            // Set Response Type
            route.setResponseType(invocable.getRawResponseType().getSimpleName());

            //Set Params
            for (Parameter param : invocable.getParameters()) {
                String name = param.getSourceName();
                RouteParamModel paramModel = new RouteParamModel(param.getDefaultValue(), param.getRawType().getSimpleName());

                if (param.getSource() == Parameter.Source.QUERY) {
                    route.addQueryParam(param.getSourceName(), paramModel);
                } else {
                    paramModel.setParamType(param.getSource().name());
                    if (name == null) {
                        name = paramModel.getType();
                    }
                    route.addOtherParam(name, paramModel);
                }
            }

            routeList.add(route);
        }

        for (Resource childResource : resource.getChildResources()) {
            routeList.addAll(collectRoute(childResource, parentPath, clsResource));
        }

        return routeList;
    }

    @Override
    public IBaseResponse getRoutes() {
        try {
            Resource resource = Resource.from(this.getClass());
            if (resource == null) {
                throw new NullPointerException("There is no data for your request.");
            }
            ListModel<RouteModel> content = new ListModel<>();
            content.setData(collectRoute(resource, "/", null));
            return new DefaultResponse<>(200, ResponseType.SUCCESS, content);
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }
}
