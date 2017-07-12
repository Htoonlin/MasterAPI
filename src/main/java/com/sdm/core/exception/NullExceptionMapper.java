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
import javax.ws.rs.ext.Provider;

import com.sdm.core.Setting;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.model.Message;

/**
 *
 * @author Htoonlin
 */
@Provider
public class NullExceptionMapper implements ExceptionMapper<NullPointerException> {

	@Override
	public Response toResponse(NullPointerException exception) {
		Message message = new Message(500, NullPointerException.class.getName(), exception.getMessage());
		String env = Setting.getInstance().get(Setting.SYSTEM_ENV, "beta");
		if (env.equalsIgnoreCase("dev")) {
			Map<String, Object> debug = new HashMap<>();
			debug.put("StackTrace", exception.getStackTrace());
			debug.put("Suppressed", exception.getSuppressed());
			message.setTrace(debug);
		}

		return Response.serverError().entity(new DefaultResponse<>(message)).type(MediaType.APPLICATION_JSON).build();
	}

}
