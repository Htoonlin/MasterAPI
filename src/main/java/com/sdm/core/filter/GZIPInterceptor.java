package com.sdm.core.filter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

@Provider
@Priority(Priorities.ENTITY_CODER)
public class GZIPInterceptor implements WriterInterceptor, ReaderInterceptor {

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        MultivaluedMap<String, Object> headers = context.getHeaders();
        headers.putSingle(HttpHeaders.CONTENT_ENCODING, "gzip");

        final OutputStream output = context.getOutputStream();
        context.setOutputStream(new GZIPOutputStream(output));
        context.proceed();
    }

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        String encoding = context.getHeaders().getFirst("Content-Encoding");
        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
            GZIPInputStream input = new GZIPInputStream(context.getInputStream());
            context.setInputStream(input);
        }
        return context.proceed();
    }

}
