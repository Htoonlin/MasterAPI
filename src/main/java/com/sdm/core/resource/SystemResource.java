/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.resource;

import java.util.HashMap;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sdm.core.Setting;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.ErrorResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.model.MessageModel;

/**
 *
 * @author Htoonlin
 */
@Path("/")
public class SystemResource extends DefaultResource {

	@PermitAll
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse welcome() throws Exception {
		MessageModel message = new MessageModel(200, "Welcome!", "Never give up to be a warrior.");
		return new DefaultResponse(message);
	}

	@GET
	@Path("setting")
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse getAllSetting() {
		DefaultResponse response = this.validateCache();
		// Cache validation
		if (response != null) {
			return response;
		}
		response = new DefaultResponse<>(Setting.getInstance().getProperties());
		response.setHeaders(this.buildCache());
		return response;
	}

	@POST
	@Path("setting")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse updateAllSetting(HashMap<String, String> request) {
		boolean isValid = true;
		ErrorResponse errors = new ErrorResponse();
		for (String key : request.keySet()) {
			if (key.toLowerCase().startsWith("com.sdm.path")) {
				errors.addError(key, "Can't modified this property <" + key + ">");
				isValid = false;
				continue;
			}
			String value = request.get(key);
			Setting.getInstance().changeSetting(key.toLowerCase(), value);
		}
		if (!isValid) {
			return errors;
		}

		Setting.getInstance().save();
		this.modifiedResource();

		return new DefaultResponse<>(Setting.getInstance().getProperties());
	}
}
