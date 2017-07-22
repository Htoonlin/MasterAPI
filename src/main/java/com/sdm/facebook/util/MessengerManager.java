/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.facebook.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sdm.core.Globalizer;
import com.sdm.facebook.model.PageResponse;
import com.sdm.facebook.model.messenger.Message;
import com.sdm.facebook.model.messenger.Recipient;

/**
 *
 * @author Htoonlin
 */
public class MessengerManager {
	private static final Logger LOG = Logger.getLogger(MessengerManager.class.getName());

	private final String FACEBOOK_API = "https://graph.facebook.com/v2.6/";
	private final String MESSAGE_API = FACEBOOK_API + "me/messages?access_token=";
	private final String PAGE_ACCESS_TOKEN;

	public MessengerManager(String token) {
		this.PAGE_ACCESS_TOKEN = token;
	}

	private Response getRequest(String url) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url);
		return target.request().accept(MediaType.APPLICATION_JSON).get(Response.class);
	}

	private Response postRequest(String url, String data) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url);
		return target.request().accept(MediaType.APPLICATION_JSON).post(Entity.json(data), Response.class);
	}

	private void processResponse(Response response, IFacebookListener listener) {
		if (listener == null) {
			LOG.info("Response Message : " + response.readEntity(String.class));
			return;
		}

		if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
			String successString = response.readEntity(String.class);
			LOG.info("Send Message : " + successString);
			listener.success(response.getStatus(), successString);
		} else {
			String errorString = response.readEntity(String.class);
			LOG.warn("Send Message : " + errorString);
			listener.failed(response.getStatus(), errorString);
		}
	}

	public PageResponse getPageInfo() {
		String url = FACEBOOK_API + "me?access_token=" + PAGE_ACCESS_TOKEN;
		Response response = this.getRequest(url);
		if (response.getStatus() == 200) {
			String responseString = response.readEntity(String.class);
			try {
				return Globalizer.jsonMapper().readValue(responseString, PageResponse.class);
			} catch (IOException exception) {
				LOG.error("Invalid Response : " + responseString);
			}
		}
		return null;
	}

	public void sendMessage(Recipient recipient, Message message, IFacebookListener listener) {
		try {
			Map<String, Object> sendMessage = new HashMap<>();
			sendMessage.put("recipient", recipient);
			sendMessage.put("message", message);
			String requestString = Globalizer.jsonMapper().writeValueAsString(sendMessage);
			Response response = postRequest(MESSAGE_API + PAGE_ACCESS_TOKEN, requestString);
			processResponse(response, listener);
		} catch (JsonProcessingException ex) {
			LOG.error(ex);
		}
	}

	public void sendJsonTemplate(String recipientId, String jsonFileURL, IFacebookListener listener) throws IOException, URISyntaxException {
		byte[] jsonData = Files.readAllBytes(Paths.get(new URI(jsonFileURL)));
		JSONObject payload = new JSONObject(new String(jsonData, Charset.forName("UTF-8")));

		// Build recipient
		JSONObject recipient = new JSONObject();
		recipient.put("id", recipientId);

		// Build Template
		JSONObject template = new JSONObject();
		template.put("type", "template");
		template.put("payload", payload);

		// Build message
		JSONObject message = new JSONObject();
		message.put("attachment", template);

		// Build data
		JSONObject data = new JSONObject();
		data.put("recipient", recipient);
		data.put("message", message);

		Response response = this.postRequest(MESSAGE_API + PAGE_ACCESS_TOKEN, data.toString());
		processResponse(response, listener);
	}
}
