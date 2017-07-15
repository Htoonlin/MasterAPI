package com.sdm.core.exception;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.glassfish.jersey.message.internal.MessageBodyProviderNotFoundException;

import com.sdm.core.Setting;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.model.MessageModel;

public class MessageBodyProviderNotFoundMapper implements ExceptionMapper<MessageBodyProviderNotFoundException> {

	@Override
	public Response toResponse(MessageBodyProviderNotFoundException exception) {
		MessageModel message = new MessageModel(500, IllegalStateException.class.getName(), exception.getMessage());
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
