package com.sdm.core.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.sdm.core.Setting;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.MessageResponse;
import com.sdm.core.response.ResponseType;

@Path("system")
public class SystemResource extends DefaultResource {
	private static final Logger LOG = Logger.getLogger(SystemResource.class);

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
	@Produces
	public IBaseResponse updateAllSetting(HashMap<String, String> request) {
		HashMap<String, String> response = new HashMap<>();
		Properties props = Setting.getInstance().getProperties();
		for (String key : props.stringPropertyNames()) {
			String value = request.get(key);
			response.put(key.toLowerCase(), value);
		}
		Setting.getInstance().save();
		
		return new DefaultResponse<HashMap<String, String>>(response);
	}
}
