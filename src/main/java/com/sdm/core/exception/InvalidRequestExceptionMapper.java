package com.sdm.core.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.commons.httpclient.HttpStatus;

import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.ResponseType;

@Provider
public class InvalidRequestExceptionMapper extends DefaultExceptionMapper<InvalidRequestException> {

	@Override
	public Response toResponse(InvalidRequestException exception) {
		DefaultResponse response = new DefaultResponse<>(HttpStatus.SC_BAD_REQUEST, ResponseType.CLIENT_ERROR,
				exception.getErrors());
		return Response.status(HttpStatus.SC_BAD_REQUEST).entity(response).type(MediaType.APPLICATION_JSON).build();
	}

}
