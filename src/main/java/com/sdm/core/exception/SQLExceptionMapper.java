/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.exception;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.sdm.core.Setting;
import com.sdm.core.response.MessageResponse;
import com.sdm.core.response.ResponseType;

/**
 *
 * @author Htoonlin
 */
@Provider
public class SQLExceptionMapper implements ExceptionMapper<SQLException> {

	@Override
	public Response toResponse(SQLException exception) {
		MessageResponse message = new MessageResponse(500, ResponseType.ERROR, exception.getMessage());
		String env = Setting.getInstance().get(Setting.SYSTEM_ENV, "beta");
		if (env.equalsIgnoreCase("dev"))  {
			Map<String, Object> debug = new HashMap<>();
			debug.put("StackTrace", exception.getStackTrace());
			debug.put("Suppressed", exception.getSuppressed());
			message.setDebug(debug);
		}

		return Response.serverError().entity(message).type(MediaType.APPLICATION_JSON).build();
	}

}
