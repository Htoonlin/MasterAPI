package com.sdm.core.filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.httpclient.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sdm.core.Globalizer;
import com.sdm.core.request.IBaseRequest;
import com.sdm.core.response.IBaseResponse;

@Provider
public class JsonProvider<I extends IBaseRequest, O extends IBaseResponse>
		implements MessageBodyReader<I>, MessageBodyWriter<O> {
	private final ObjectMapper jsonMapper = Globalizer.jsonMapper();

	@Override
	public boolean isReadable(Class<?> reqClass, Type reqType, Annotation[] annotations, MediaType mediaType) {
		boolean canRead = IBaseRequest.class.isAssignableFrom(reqClass);
		return canRead;
	}

	@Override
	public I readFrom(Class<I> reqClass, Type reqType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> dataMap, InputStream dataStream)
			throws IOException, WebApplicationException {
		I request = jsonMapper.readValue(dataStream, reqClass);
		if (!request.isValid()) {
			Response response = Response.status(HttpStatus.SC_BAD_REQUEST).entity(request.getErrors()).build();
			throw new WebApplicationException(response);
		}
		return request;
	}

	@Override
	public long getSize(O entity, Class<?> resClass, Type resType, Annotation[] annotation, MediaType mediaType) {
		return -1;
	}

	@Override
	public boolean isWriteable(Class<?> resClass, Type resType, Annotation[] annotation, MediaType mediaType) {
		boolean canWrite = IBaseResponse.class.isAssignableFrom(resClass);
		return canWrite;
	}

	@Override
	public void writeTo(O entity, Class<?> resClass, Type resType, Annotation[] annotation, MediaType mediaType,
			MultivaluedMap<String, Object> dataMap, OutputStream outputStream)
			throws IOException, WebApplicationException {
		ObjectWriter writer = jsonMapper.writerFor(resClass);
		writer.writeValue(outputStream, entity);
	}

}
