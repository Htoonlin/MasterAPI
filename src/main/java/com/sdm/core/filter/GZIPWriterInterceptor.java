package com.sdm.core.filter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

@Provider
public class GZIPWriterInterceptor implements WriterInterceptor {

	@Override
	public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
		MultivaluedMap<String, Object> headers = context.getHeaders();
		headers.putSingle(HttpHeaders.CONTENT_ENCODING, "gzip");

		final OutputStream output = context.getOutputStream();
		context.setOutputStream(new GZIPOutputStream(output));
		context.proceed();
	}

}
