package com.sdm.core.filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
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
import com.sdm.core.Setting;
import com.sdm.core.exception.InvalidRequestException;
import com.sdm.core.request.IBaseRequest;
import com.sdm.core.response.IBaseResponse;

@Provider
public class JsonProvider<I extends IBaseRequest, O extends IBaseResponse>
		implements MessageBodyReader<I>, MessageBodyWriter<O> {
	private final ObjectMapper jsonMapper = Globalizer.jsonMapper();

	private Map<String, String> validateRequest(Class<I> reqClass, I request) {
		Map<String, String> errors = new HashMap<>();

		// Timestamp validation
		String env = Setting.getInstance().get(Setting.SYSTEM_ENV, "beta");
		if (!env.equalsIgnoreCase("dev")) {
			if (request.getTimestamp() == null || !Globalizer.validTimeStamp(request.getTimestamp())) {
				errors.put("timestamp", "Invalid timestamp.");
			}
		}

		// Hibernate bean validation
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<I>> violoationSet = validator.validate(request);
		for (ConstraintViolation<I> v : violoationSet) {
			String propertyName = Globalizer.camelToLowerUnderScore(v.getPropertyPath().toString());
			errors.put(propertyName, v.getMessage());
		}

		// Validate DB input
		for (Field field : reqClass.getDeclaredFields()) {
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				// Need to develop DB validation here
			}
		}

		return errors;
	}

	@Override
	public boolean isReadable(Class<?> reqClass, Type reqType, Annotation[] annotations, MediaType mediaType) {
		boolean canRead = IBaseRequest.class.isAssignableFrom(reqClass);
		if (!canRead) {
			throw new WebApplicationException("Invalid request. Pls check your request parameter.",
					HttpStatus.SC_BAD_REQUEST);
		}
		return canRead;
	}

	@Override
	public I readFrom(Class<I> reqClass, Type reqType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> dataMap, InputStream dataStream)
			throws IOException, WebApplicationException {
		I request = jsonMapper.readValue(dataStream, reqClass);
		Map<String, String> errors = this.validateRequest(reqClass, request);
		if (errors.size() > 0) {
			throw new InvalidRequestException(errors);
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
		if (!canWrite) {
			throw new WebApplicationException("Invalid response data. Pls contact to system administrator.",
					HttpStatus.SC_INTERNAL_SERVER_ERROR);
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
