package com.sdm.core.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdm.core.Globalizer;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class JsonResolver implements ContextResolver<ObjectMapper> {

    private final ObjectMapper jsonMapper = Globalizer.jsonMapper();

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return this.jsonMapper;
    }

}
