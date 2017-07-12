/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.filter;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdm.core.Globalizer;

/**
 *
 * @author Htoonlin
 */
@Provider
@Priority(Priorities.ENTITY_CODER)
public class JacksonObjectMapper implements ContextResolver<ObjectMapper> {

	final ObjectMapper defaultObjectMapper;

	public JacksonObjectMapper() {
		defaultObjectMapper = Globalizer.jsonMapper();
	}

	@Override
	public ObjectMapper getContext(final Class<?> type) {
		return defaultObjectMapper;
	}

}
