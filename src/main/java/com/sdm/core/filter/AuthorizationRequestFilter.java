/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.filter;

import com.sdm.Constants;
import com.sdm.core.Setting;
import com.sdm.core.di.IAccessManager;
import com.sdm.core.resource.UserAllowed;
import com.sdm.core.response.model.MessageModel;
import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.List;
import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;

/**
 *
 * @author Htoonlin
 */
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationRequestFilter implements ContainerRequestFilter {

    private static final Logger LOG = Logger.getLogger(AuthorizationRequestFilter.class);
    
    @Context
    private ResourceInfo resourceInfo;

    @Inject
    private IAccessManager accessManager;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();
        Class<?> resourceClass = resourceInfo.getResourceClass();
        
        //Skip Data Permission if Resource Permission is public or there is no AccessManager
        if (resourceClass.isAnnotationPresent(PermitAll.class) || accessManager == null) {
            //Auth Success
            requestContext.setProperty(Constants.REQUEST_USER, 0);
            return;
        }

        if (!method.isAnnotationPresent(PermitAll.class)) {
            if (method.isAnnotationPresent(DenyAll.class)) {
                //Deny all Auth
                requestContext.abortWith(errorResponse(403));
                return;
            }

            final MultivaluedMap<String, String> headers = requestContext.getHeaders();
            final List<String> authorization = headers.get(HttpHeaders.AUTHORIZATION);
            final List<String> userAgents = headers.get(HttpHeaders.USER_AGENT);

            if (authorization == null || authorization.isEmpty() || userAgents == null || userAgents.isEmpty()) {
                //There is not Auth
                requestContext.abortWith(errorResponse(401));
                return;
            }

            try {
                // Clean Token
                String settingKey = Setting.getInstance().get(Setting.JWT_KEY);
                String tokenString = authorization.get(0).substring(Constants.AUTH_TYPE.length()).trim();
                byte[] jwtKey = Base64.getDecoder().decode(settingKey);

                try {
                    Claims authorizeToken = Jwts.parser().setSigningKey(jwtKey).requireIssuer(userAgents.get(0))
                            .parseClaimsJws(tokenString).getBody();

                    if (!accessManager.validateToken(authorizeToken)) {
                        //Auth Failed 
                        requestContext.abortWith(errorResponse(403));
                        return;
                    }
                    
                    int userId = Integer.parseInt(authorizeToken.getSubject().substring(Constants.USER_PREFIX.length()).trim());
                    
                    //Check @UserAllowed in Class
                    UserAllowed userAllowed = resourceClass.getAnnotation(UserAllowed.class);
                    if(userAllowed != null && userAllowed.value()){
                        //Skip Permission for User Allowed Class
                        requestContext.setProperty(Constants.REQUEST_USER, userId);
                        return;
                    }
                    
                    //Check @UserAllowed in Method
                    userAllowed = resourceClass.getAnnotation(UserAllowed.class);
                    if(userAllowed != null && userAllowed.value()){
                        //Skip Permission for User Allowed Method
                        requestContext.setProperty(Constants.REQUEST_USER, userId);
                        return;
                    }
                    
                    //Check permission from DB
                    if(!accessManager.checkPermission(authorizeToken, method, requestContext.getMethod(), resourceClass)){
                        //Validate dynamic Permission failed
                        requestContext.abortWith(errorResponse(403));
                        return;
                    }
                    
                    requestContext.setProperty(Constants.REQUEST_USER, userId);
                    
                } catch (ClaimJwtException ex) {
                    requestContext.abortWith(buildResponse(403, ex.getLocalizedMessage()));
                }

            } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
                LOG.error(e);
                requestContext.abortWith(buildResponse(500, e.getLocalizedMessage()));
            }
        }
    }

    private Response errorResponse(int code) {
        String description = "";
        switch (code) {
            case 401:
                description = "Hmmm! Your authorization is failed. If you are trying to hack server, don't do it again.";
                break;
            case 403:
                description = "Oppp! Something wrong or user doesn't have access to the resource.";
                break;
        }
        return buildResponse(code, description);
    }

    private Response buildResponse(int code, String description) {
        MessageModel message = new MessageModel(code, HttpStatus.getStatusText(code), description);
        return Response.status(code).entity(message).build();
    }
}
