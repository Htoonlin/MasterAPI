/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import java.util.HashMap;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.sdm.core.Setting;
import com.sdm.core.resource.DefaultResource;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.MessageResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.core.util.MyanmarFontManager;
import com.sdm.master.util.GeoIPManager;

/**
 *
 * @author Htoonlin
 */
@Path("/")
public class GeneralResource extends DefaultResource {

	private static final Logger LOG = Logger.getLogger(ProfileResource.class.getName());

	@PostConstruct
	public void onLoad() {
		LOG.info("Welcome...");
	}

	@PermitAll
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse welcome() throws Exception {
		return new MessageResponse(200, ResponseType.SUCCESS,
				"Welcome! from sundew API. Never give up to be a warrior!");
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

	@PermitAll
	@GET
	@Path("ip/{address}")
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse checkIP(@Context HttpServletRequest request,
			@DefaultValue("") @PathParam("address") String address) throws Exception {
		GeoIPManager ipManager = new GeoIPManager();
		if (address.isEmpty()) {
			address = request.getRemoteAddr();
		}
		return new DefaultResponse<HashMap<String, Object>>(ipManager.lookupInfo(address));
	}

	@PermitAll
	@GET
	@Path("lang")
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse langConverter(@QueryParam("input") String input) throws Exception {
		MessageResponse message = new MessageResponse(200, ResponseType.INFO, "No! It is not myanmar font.");
		if (MyanmarFontManager.isMyanmar(input)) {
			String msgString = "Yes! It is myanmar";
			if (MyanmarFontManager.isUnicode(input)) {
				msgString += " unicode font.";
			} else if (MyanmarFontManager.isZawgyi(input)) {
				msgString += " zawgyi font.";
			}
			message = new MessageResponse(200, ResponseType.INFO, msgString);
		}
		return message;
	}
}
