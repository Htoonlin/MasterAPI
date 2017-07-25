package com.sdm.facebook.resource;

import javax.annotation.security.PermitAll;
import javax.enterprise.inject.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sdm.core.Globalizer;
import com.sdm.core.Setting;
import com.sdm.core.resource.DefaultResource;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.facebook.model.type.MessageType;
import com.sdm.facebook.model.webhook.BaseMessage;
import com.sdm.facebook.model.webhook.MessengerEntry;

@Path("messenger")
public class MessengerResource extends DefaultResource {

	private static final Logger LOG = Logger.getLogger(MessengerResource.class);

	private static final String HUB_MODE = "subscribe";
	private static final String PAGE_OBJECT = "page";

	@GET
	@Path("token")
	public IBaseResponse getToken() {
		String verifyToken = Setting.getInstance().get(Setting.FB_MESSENGER_TOKEN);
		return new DefaultResponse<String>(200, ResponseType.SUCCESS, verifyToken);
	}

	@GET
	@Path("token/generate")
	public IBaseResponse generateToken(@DefaultValue("false") @QueryParam("save") boolean isSave) {
		String verifyToken = Globalizer.generateToken(64);
		if (isSave) {
			Setting.getInstance().changeSetting(Setting.FB_MESSENGER_TOKEN, verifyToken);
			Setting.getInstance().save();
		}
		return new DefaultResponse<String>(200, ResponseType.SUCCESS, verifyToken);
	}

	@PermitAll
	@GET
	public Response activateMessenger(@QueryParam("hub.mode") String mode, @QueryParam("hub.verify_token") String token,
			@QueryParam("hub.challenge") String challenge) {
		String verifyToken = Setting.getInstance().get(Setting.FB_MESSENGER_TOKEN);
		LOG.info("Verified by : " + token);
		if (mode.equals(HUB_MODE) && token.equals(verifyToken)) {
			LOG.info("Facebook messenger platform verification success <" + challenge + ">.");
			return Response.ok().entity(challenge).build();
		}

		LOG.warn("Facebook messenger platform verification failed.");
		return Response.status(403).build();
	}

	@PermitAll
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response receiveMessage(String receive) {
		LOG.info("Received from Facebook => " + receive);
		JSONObject request = new JSONObject(receive);
		if (request.has("object") && request.getString("object").equalsIgnoreCase(PAGE_OBJECT)) {
			if(request.has("entry")) {
				JSONArray entries = request.getJSONArray("entry");
				for(int i = 0; i < entries.length(); i++) {
					MessengerEntry entry = new MessengerEntry();
					entry.setJson(entries.getJSONObject(i));
				}
			}
		}else {
			LOG.warn("Invalid request or object type");
		}
		return Response.ok().build();
	}
}
