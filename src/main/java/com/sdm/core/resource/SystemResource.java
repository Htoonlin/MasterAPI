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
import com.sdm.core.exception.InvalidRequestException;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.ResponseType;
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
		DefaultResponse response = this.validateCache();
		if (response != null) {
			return response;
		}
		response = new DefaultResponse(new MessageModel(200, "Welcome!", "Never give up to be a warrior."));
		response.setHeaders(this.buildCache());
		return response;
	}
	
	@GET
	@Path("settings")
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse getAllSetting() {
		DefaultResponse response = this.validateCache();
		// Cache validation
		if (response != null) {
			return response;
		}
		response = new DefaultResponse<>(200, ResponseType.SUCCESS, Setting.getInstance().getProperties());
		response.setHeaders(this.buildCache());
		return response;
	}

	@POST
	@Path("settings")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse updateAllSetting(HashMap<String, String> request) {
		InvalidRequestException invalidRequest = new InvalidRequestException();
		for (String key : request.keySet()) {
			String value = request.get(key);
			if (key.toLowerCase().startsWith("com.sdm.path")) {
				invalidRequest.addError(key, "Can't modified this property <" + key + ">", value);
				continue;
			}
			Setting.getInstance().changeSetting(key.toLowerCase(), value);
		}
		if (invalidRequest.getErrors().size() > 0) {
			throw invalidRequest;
		}

		Setting.getInstance().save();
		this.modifiedResource();

		return new DefaultResponse<>(200, ResponseType.SUCCESS, Setting.getInstance().getProperties());
	}
}
