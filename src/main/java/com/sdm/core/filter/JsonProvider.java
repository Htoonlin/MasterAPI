package com.sdm.core.filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.httpclient.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sdm.core.Globalizer;
import com.sdm.core.exception.InvalidRequestException;
import com.sdm.core.request.IBaseRequest;

@Provider
public class JsonProvider<I extends IBaseRequest, O extends Serializable>
		implements MessageBodyReader<I>, MessageBodyWriter<O> {
	private final ObjectMapper jsonMapper = Globalizer.jsonMapper();

	@Override
	public boolean isReadable(Class<?> reqClass, Type reqType, Annotation[] annotations, MediaType mediaType) {
		boolean canRead = IBaseRequest.class.isAssignableFrom(reqClass);
		if(!canRead) {
			throw new WebApplicationException("Invalid request. Pls check your request parameter.", HttpStatus.SC_BAD_REQUEST);			
		}
		return canRead;
	}

	@Override
	public I readFrom(Class<I> reqClass, Type reqType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> dataMap, InputStream dataStream)
			throws IOException, WebApplicationException {
		I request = jsonMapper.readValue(dataStream, reqClass);
		if (!request.isValid()) {			
			throw new InvalidRequestException(request.getErrors());
		}
		return request;
	}

	@Override
	public long getSize(O entity, Class<?> resClass, Type resType, Annotation[] annotation, MediaType mediaType) {
		return -1;
	}

	@Override
	public boolean isWriteable(Class<?> resClass, Type resType, Annotation[] annotation, MediaType mediaType) {
		boolean canWrite = Serializable.class.isAssignableFrom(resClass);
		if(!canWrite) {
			throw new WebApplicationException("Invalid response data. Pls contact to system administrator.", HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
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
