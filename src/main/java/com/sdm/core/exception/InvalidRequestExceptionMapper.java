package com.sdm.core.exception;

import java.util.HashMap;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.commons.httpclient.HttpStatus;

import com.sdm.core.Setting;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.ResponseType;

public class InvalidRequestExceptionMapper implements ExceptionMapper<InvalidRequestException> {

	@Override
	public Response toResponse(InvalidRequestException exception) {
		HashMap<String, Object> content = new HashMap<>();
		content.put("errors", exception.getErrors());

		String env = Setting.getInstance().get(Setting.SYSTEM_ENV, "beta");
		if (env.equalsIgnoreCase("dev")) {
			content.put("StackTrace", exception.getStackTrace());
			content.put("Suppressed", exception.getSuppressed());
		}
		DefaultResponse response = new DefaultResponse<>(HttpStatus.SC_BAD_REQUEST, ResponseType.WARNING, content);

		return Response.status(HttpStatus.SC_BAD_REQUEST).entity(response).type(MediaType.APPLICATION_JSON).build();
	}

}
