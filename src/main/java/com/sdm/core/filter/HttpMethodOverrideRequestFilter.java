package com.sdm.core.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class HttpMethodOverrideRequestFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String methodOverride = requestContext.getHeaderString("X-Http-Method-Override");
		if (methodOverride != null) {
			requestContext.setMethod(methodOverride);
		}
	}

}
