/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.filter;

import com.sdm.core.Globalizer;
import com.sdm.core.Setting;
import com.sdm.core.di.IAccessManager;
import com.sdm.core.response.ResponseType;
import com.sdm.core.request.AuthorizeRequest;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.MessageResponse;
import com.sdm.core.util.SecurityInstance;
import eu.bitwalker.useragentutils.UserAgent;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.apache.log4j.Logger;

/**
 *
 * @author Htoonlin
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticaionFilter implements ContainerRequestFilter {

    private static final Logger logger = Logger.getLogger(AuthenticaionFilter.class);
    public final String FAILED_COUNT = "AUTHORIZATION_FAILED_COUNT";

    @Context
    private ResourceInfo resourceInfo;

    @Inject
    private HttpSession httpSession;

    @Inject
    private IAccessManager accessManager;

    private static final String AUTHORIZATION = "Authorization";
    
    private int getFailed(){
        try{
            return (int) httpSession.getAttribute(FAILED_COUNT);
        }catch(Exception e){
            return 0;
        }
    }
    
    private int blockTime(){
        int seconds = httpSession.getMaxInactiveInterval() * 2;
        return (seconds / 60);
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();
        if (!method.isAnnotationPresent(PermitAll.class)) {
            if (getFailed() >= Setting.getInstance().AUTH_FAILED_COUNT) {
                requestContext.abortWith(buildResponse(403, "Block", 
                        "Sorry! Server blocked your request. You need to wait " + blockTime() + " minutes."));
                return;
            }

            if (method.isAnnotationPresent(DenyAll.class)) {
                requestContext.abortWith(buildResponse(403));
                return;
            }

            final MultivaluedMap<String, String> headers = requestContext.getHeaders();
            final List<String> authorization = headers.get(AUTHORIZATION);
            final List<String> userAgents = headers.get("user-agent");

            if (authorization == null || authorization.isEmpty()
                    || userAgents == null || userAgents.isEmpty()) {
                requestContext.abortWith(buildResponse(401));
                return;
            }

            if (accessManager == null) {
                requestContext.abortWith(buildResponse(500,
                        "SERVER_ERROR", "AccessManager Interface is null or invalid."));
                return;
            }

            try {
                //String jsonAuthorization = Globalizer.decrypt(authorization.get(0).replaceFirst("Basic", ""));
                String jsonAuthorization = SecurityInstance.base64Decode(authorization.get(0).replaceFirst("Basic", ""));
                AuthorizeRequest tokenAuthorize = Globalizer.jsonMapper().readValue(jsonAuthorization, AuthorizeRequest.class);
                UserAgent userAgent = UserAgent.parseUserAgentString(userAgents.get(0));
                tokenAuthorize.setDeviceOS(userAgent.getOperatingSystem().getName());

                if (!accessManager.validateToken(tokenAuthorize)) {
                    requestContext.abortWith(buildResponse(403));
                    return;
                }

                if (!accessManager.checkPermission(tokenAuthorize, method,
                        requestContext.getMethod())) {
                    requestContext.abortWith(buildResponse(403));
                    return;
                }

                this.saveUserId(tokenAuthorize.getUserId());
            } catch (Exception e) {
                logger.error(e);
                requestContext.abortWith(buildResponse(500, "SERVER_ERROR", e.getLocalizedMessage()));
            }
        }
    }

    private Response buildResponse(int code) {
        String title = "";
        String description = "";
        httpSession.setAttribute(FAILED_COUNT, getFailed() + 1);
        switch (code) {
            case 401:
                title = "UNAUTHROIZED";
                description = "Hmmm! Your authorization is failed. If you are trying to hack server, don't do it again.";
                break;
            case 403:
                title = "ACCESS_FORBIDDEN";
                description = "Oppp! Something wrong or user doesn't have access to the resource.";
                break;
        }
        return buildResponse(code, title, description);
    }

    private Response buildResponse(int code, String title, String description) {
        MessageResponse message = new MessageResponse(code, ResponseType.ERROR, title, description);
        return Response.status(code).entity(new DefaultResponse(message)).build();
    }

    private void saveUserId(long userId) {
        this.httpSession.setAttribute(Globalizer.SESSION_USER_ID, userId);
    }
}
