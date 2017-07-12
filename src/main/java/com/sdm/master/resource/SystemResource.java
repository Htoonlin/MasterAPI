/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import java.util.HashMap;
import java.util.Properties;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sdm.core.Setting;
import com.sdm.core.resource.DefaultResource;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.model.Message;

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
		Message message = new Message(200, "Welcome!", "Never give up to be a warrior.");
		return new DefaultResponse(message);
	}

	@GET
	@Path("setting")
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse getAllSetting() {
		HashMap<String, String> response = new HashMap<>();
		Properties props = Setting.getInstance().getProperties();
		for (String key : props.stringPropertyNames()) {
			String value = props.getProperty(key, "");
			response.put(key.toLowerCase(), value);
		}
		return new DefaultResponse<HashMap<String, String>>(response);
	}

	@POST
	@Path("setting")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse updateAllSetting(HashMap<String, String> request) {
		HashMap<String, String> response = new HashMap<>();
		Properties props = Setting.getInstance().getProperties();
		for (String key : props.stringPropertyNames()) {
			String value = request.get(key);
			if (value != null) {
				Setting.getInstance().changeSetting(key.toLowerCase(), value);
				response.put(key.toLowerCase(), value);
			} else {
				response.put(key.toLowerCase(), props.getProperty(key.toLowerCase()));
			}
		}
		Setting.getInstance().save();
		return new DefaultResponse<HashMap<String, String>>(response);
	}
}
