package com.sdm.core.exception;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.sdm.core.Setting;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.core.response.model.MessageModel;

public abstract class DefaultExceptionMapper<T extends Throwable> implements ExceptionMapper<T> {

	protected Response buildResponse(int httpStatus, T exception) {
		return this.buildResponse(httpStatus, exception.hashCode(), exception.getClass().getSimpleName(),
				exception.getLocalizedMessage(), exception);
	}

	protected Response buildResponse(int httpStatus, int errorCode, String title, String description, T exception) {
		MessageModel message = new MessageModel(errorCode, title, description);
		String env = Setting.getInstance().get(Setting.SYSTEM_ENV, "beta");
		if (env.equalsIgnoreCase("dev")) {
			Map<String, Object> debug = new HashMap<>();
			debug.put("StackTrace", exception.getStackTrace());
			debug.put("Suppressed", exception.getSuppressed());
			message.setTrace(debug);
		}

		DefaultResponse response = new DefaultResponse<>(httpStatus, ResponseType.EXCEPTION, message);
		return Response.status(httpStatus).entity(response).type(MediaType.APPLICATION_JSON).build();
	}
}
