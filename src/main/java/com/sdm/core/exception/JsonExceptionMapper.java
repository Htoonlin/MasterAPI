/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.exception;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sdm.core.Setting;
import com.sdm.core.response.MessageResponse;
import com.sdm.core.response.ResponseType;

/**
 *
 * @author Htoonlin
 */
public class JsonExceptionMapper implements ExceptionMapper<JsonProcessingException> {

	@Override
	public Response toResponse(JsonProcessingException exception) {
		MessageResponse message = new MessageResponse(400, ResponseType.ERROR, exception.getOriginalMessage());
		String env = Setting.getInstance().get(Setting.SYSTEM_ENV, "beta");
		if (env.equalsIgnoreCase("dev")) {
			Map<String, Object> debug = new HashMap<>();
			debug.put("StackTrace", exception.getStackTrace());
			debug.put("Suppressed", exception.getSuppressed());
			message.setDebug(debug);
		}

		return Response.status(400).entity(message).type(MediaType.APPLICATION_JSON).build();
	}

}
