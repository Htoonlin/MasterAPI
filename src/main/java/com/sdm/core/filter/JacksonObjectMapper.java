/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.filter;

import javax.ws.rs.ext.ContextResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdm.core.Globalizer;

/**
 *
 * @author Htoonlin
 */
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
