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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import com.sdm.core.Setting;

/**
 *
 * @author Htoonlin
 */
@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class CORSFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		MultivaluedMap<String, Object> headers = responseContext.getHeaders();
		headers.add("Access-Control-Allow-Origin", Setting.getInstance().get(Setting.CORS_ORIGIN, "*"));
		headers.add("Access-Control-Allow-Headers",
				Setting.getInstance().get(Setting.CORS_HEADERS, "authorization, content-type"));
		headers.add("Access-Control-Allow-Credentials", true);
		headers.add("Access-Control-Allow-Methods",
				Setting.getInstance().get(Setting.CORS_METHODS, "GET,POST,PUT,DELETE,OPTIONS"));
		headers.add("Access-Control-Max-Age", Setting.getInstance().getInt(Setting.CORS_MAX_AGE, "36000"));
		headers.add("Access-Control-Expose-Headers", "xsrf-token");
		if ("OPTIONS".equals(requestContext.getMethod())) {
			responseContext.setStatus(200);
		}
	}

}
