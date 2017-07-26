package com.sdm.facebook.service;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import com.sdm.facebook.model.GraphResponse;
import com.sdm.facebook.model.MessageBuilder;
import com.sdm.facebook.model.attachment.GeneralAttachment;
import com.sdm.facebook.util.GraphManager;

public class MessengerService extends GraphManager {
	public MessengerService(String accessToken) {
		super(accessToken);
	}

	public GraphResponse sendText(String recipientId, String message) {
		MessageBuilder builder = new MessageBuilder();
		return this.send(builder.setRecipientId(recipientId).setText(message).buildMessage());
	}

	public GraphResponse sendFile(String recipientId, GeneralAttachment attachment) {
		MessageBuilder builder = new MessageBuilder();
		return this.send(builder.setRecipientId(recipientId).setFile(attachment, false).buildMessage());
	}

	public GraphResponse sendTemplate(String recipientId, JSONObject templatePayload) {
		MessageBuilder builder = new MessageBuilder();
		return this.send(builder.setRecipientId(recipientId).setTemplate(templatePayload).buildMessage());
	}

	public GraphResponse send(MessageBuilder builder) {
		return this.send(builder.buildMessage());
	}

	public GraphResponse send(JSONObject entity) {
		return this.setPath("me").setPath("messages")
				.postRequest(Entity.entity(entity.toString(), MediaType.APPLICATION_JSON));
	}
}
