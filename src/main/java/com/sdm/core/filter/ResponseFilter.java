/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.filter;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import com.sdm.core.response.IBaseResponse;

/**
 *
 * @author Htoonlin
 */
@Provider
@Priority(Priorities.ENTITY_CODER)
public class ResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        Object entity = responseContext.getEntity();
        if (entity instanceof IBaseResponse) {
            IBaseResponse baseEntity = (IBaseResponse) entity;
            // Add user headers
            Map<String, Object> userHeaders = baseEntity.getHeaders();
            if (userHeaders != null) {
                MultivaluedMap<String, Object> headers = responseContext.getHeaders();
                for (String key : userHeaders.keySet()) {
                    headers.putSingle(key, userHeaders.get(key));
                }
            }

            // Skip Http staus code 204 to 200 because 204 can't response any data.
            int httpStatus = baseEntity.getCode();
            if (httpStatus == 204) {
                responseContext.setStatus(200);
            } else {
                responseContext.setStatus(httpStatus);
            }
        }

    }
}
