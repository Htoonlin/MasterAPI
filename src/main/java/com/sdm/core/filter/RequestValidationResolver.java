package com.sdm.core.filter;

import javax.ws.rs.ext.ContextResolver;

import org.glassfish.jersey.server.validation.ValidationConfig;

public class RequestValidationResolver implements ContextResolver<ValidationConfig> {

	@Override
	public ValidationConfig getContext(Class<?> type) {
		final ValidationConfig config = new ValidationConfig();
		return config;
	}

}
