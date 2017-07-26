package com.sdm.facebook.model.webhook;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sdm.facebook.model.FacebookSerialize;
import com.sdm.facebook.model.attachment.GeneralAttachment;
import com.sdm.facebook.model.attachment.LocationAttachment;

public class TextMessage implements FacebookSerialize {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8444508403359187865L;
	/**
	 * Message ID
	 */
	private String messageId;
	/**
	 * Text of message
	 */
	private String text;
	/**
	 * Optional custom data provided by the sending app
	 */
	private String quickReply;
	/**
	 * Array containing attachment data
	 */
	private List<FacebookSerialize> attachments;

	/**
	 * Indicates the message sent from the page itself
	 */
	private boolean echo;
	/**
	 * ID of the app from which the message was sent
	 */
	private String appId;
	/**
	 * Custom string passed to the Send API as the metadata field
	 */
	private String metadata;

	public TextMessage() {
	}

	@Override
	public JSONObject serialize() {
		JSONObject message = new JSONObject();
		if (this.text != null && this.text.length() > 0) {
			message.put("text", this.text);
		}

		if (this.messageId != null && this.messageId.length() > 0) {
			message.put("mid", this.text);
		}

		if (this.quickReply != null && this.quickReply.length() > 0) {
			message.put("quick_reply", new JSONObject().put("payload", this.quickReply));
		}

		if (this.attachments != null && this.attachments.size() > 0) {
			JSONArray attachments = new JSONArray();
			for (FacebookSerialize attachment : this.attachments) {
				attachments.put(attachment.serialize());
			}
			message.put("attachments", this.attachments);
		}

		return message;
	}

	@Override
	public void deserialize(JSONObject value) {
		if (value.has("mid")) {
			this.messageId = value.getString("mid");
		}

		if (value.has("text")) {
			this.text = value.getString("text");
		}

		if (value.has("quick_reply") && value.getJSONObject("quick_reply").has("payload")) {
			this.quickReply = value.getJSONObject("quick_reply").getString("payload");
		}

		if (value.has("attachments")) {
			JSONArray attachArray = value.getJSONArray("attachments");
			for (int i = 0; i < attachArray.length(); i++) {
				JSONObject attachment = attachArray.getJSONObject(i);
				if (attachment.getString("type").equalsIgnoreCase("location")) {
					LocationAttachment location = new LocationAttachment();
					location.deserialize(attachment);
					this.addAttachment(location);
				} else {
					GeneralAttachment attach = new GeneralAttachment();
					attach.deserialize(attachment);
					this.addAttachment(attach);
				}
			}
		}

		if (value.has("is_echo")) {
			this.echo = value.getBoolean("is_echo");
		}

		if (value.has("app_id")) {
			this.appId = value.getString("app_id");
		}

		if (value.has("metadata")) {
			this.metadata = value.getString("metadata");
		}
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getQuickReply() {
		return quickReply;
	}

	public void setQuickReply(String quickReply) {
		this.quickReply = quickReply;
	}

	public void addAttachment(FacebookSerialize attachment) {
		if (this.attachments == null) {
			this.attachments = new ArrayList<>();
		}
		this.attachments.add(attachment);
	}

	public List<FacebookSerialize> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<FacebookSerialize> attachments) {
		this.attachments = attachments;
	}

	public boolean isEcho() {
		return echo;
	}

	public void setEcho(boolean echo) {
		this.echo = echo;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

}
