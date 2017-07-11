package com.sdm.core.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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

	/**
	 * Step 1: It will generate setting.properties file.
	 * 
	 * @param props
	 * @return
	 */
	private IBaseResponse initSetting(Map<String, String> props) {
		return null;
	}

	/**
	 * Step 2: It will generate hibernate.cfg.xml file.
	 * 
	 * @return
	 */
	private IBaseResponse initHibernate() {
		return null;
	}

	/**
	 * Step 3: It will create root user account with ID => 1.
	 * 
	 * @return
	 */
	private IBaseResponse createRoot() {
		return null;
	}

	@PermitAll
	@GET
	@Path("setup/{step}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public IBaseResponse setup(@PathParam("step") int step) {
		return new MessageResponse(200, ResponseType.SUCCESS, "Success.");
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
}
