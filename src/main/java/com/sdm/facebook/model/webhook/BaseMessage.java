package com.sdm.facebook.model.webhook;

import org.json.JSONObject;

import com.sdm.facebook.model.FacebookSerialize;
import com.sdm.facebook.model.type.MessageType;

public class BaseMessage extends FacebookSerialize {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1611126755063299657L;
	/**
	 * Sender user ID
	 */
	private String senderId;
	/**
	 * Recipient user ID
	 */
	private String recipientId;
	/**
	 * Message Type
	 */
	private MessageType type;

	/**
	 * Message body
	 */
	private FacebookSerialize body;

	/**
	 * Message timestamp
	 */
	private long timestamp;

	/**
	 * Message object
	 */

	public BaseMessage() {
	}

	public BaseMessage(String senderId, String recipientId) {
		this.senderId = senderId;
		this.recipientId = recipientId;
		this.type = MessageType.text;
	}

	public BaseMessage(String senderId, String recipientId, MessageType type) {
		super();
		this.senderId = senderId;
		this.recipientId = recipientId;
		this.type = type;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public JSONObject getJson() {
		return null;
	}

	public <T extends FacebookSerialize> T getBody() {
		return (T) body;
	}

	public void setBody(FacebookSerialize body) {
		this.body = body;
	}

	@Override
	public void setJson(JSONObject value) {
		if (value.has("sender") && value.getJSONObject("sender").has("id")) {
			this.senderId = value.getJSONObject("sender").getString("id");
		}

		if (value.has("recipient") && value.getJSONObject("recipient").has("id")) {
			this.recipientId = value.getJSONObject("recipient").getString("id");
		}

		if (value.has("timestamp")) {
			this.timestamp = value.getLong("timestamp");
		}

		if (value.has("message")) {
			this.type = MessageType.text;
			this.body = new TextMessage();
			this.body.setJson(value.getJSONObject("message"));
		} else if (value.has("delivery")) {
			this.type = MessageType.delivery;
			this.body = new NotifyMessage("delivery");
			this.body.setJson(value.getJSONObject("delivery"));
		} else if (value.has("read")) {
			this.type = MessageType.read;
			this.body = new NotifyMessage("read");
			this.body.setJson(value.getJSONObject("read"));
		} else if (value.has("postback")) {
			this.type = MessageType.postback;
			this.body = new PostBackMessage();
			this.body.setJson(value.getJSONObject("postback"));
		} else if (value.has("optin")) {
			this.type = MessageType.plugin_optin;
			this.body.setJson(value.getJSONObject("optin"));
		} else if (value.has("referral")) {
			this.type = MessageType.referreal;
			this.body = new ReferralMessage();
			this.body.setJson(value.getJSONObject("referral"));
		} else if (value.has("payment")) {
			this.type = MessageType.payment;
			this.body = new FacebookSerialize();
			this.body.setJson(value.getJSONObject("payment"));
		} else if (value.has("checkout_update")) {
			this.type = MessageType.checkout;
			this.body = new FacebookSerialize();
			this.body.setJson(value.getJSONObject("checkout_update"));
		} else if (value.has("pre_checkout")) {
			this.type = MessageType.pre_checkout;
			this.body = new FacebookSerialize();
			this.body.setJson(value.getJSONObject("pre_checkout"));
		} else if (value.has("account_linking")) {
			this.type = MessageType.account_linking;
			this.body = new AccountLinkingMessage();
			this.body.setJson(value.getJSONObject("account_linking"));
		}
		super.setJson(value);
	}
}
