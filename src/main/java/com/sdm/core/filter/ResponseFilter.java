/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import com.sdm.core.Globalizer;
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

		if (requestContext.getMethod().equalsIgnoreCase("GET")) {
			// Set Cache Control
			MultivaluedMap<String, Object> headers = responseContext.getHeaders();
			headers.putSingle(HttpHeaders.CACHE_CONTROL, Globalizer.getCacheControl());
		}

		Object entity = responseContext.getEntity();
		if (entity instanceof IBaseResponse) {
			IBaseResponse baseEntity = (IBaseResponse) entity;
			if (baseEntity.getCode() == 204) {
				responseContext.setStatus(200);
			} else {
				responseContext.setStatus(baseEntity.getCode());
			}
		}
	}
}
