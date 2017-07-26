package com.sdm.facebook.model.webhook;

import org.json.JSONObject;

import com.sdm.facebook.model.FacebookSerialize;
import com.sdm.facebook.model.type.MessageType;

public class BaseMessage implements FacebookSerialize {

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
	private Object body;

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

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	@Override
	public JSONObject serialize() {
		return new JSONObject(this);
	}

	@Override
	public void deserialize(JSONObject value) {
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
			TextMessage message = new TextMessage();
			message.deserialize(value.getJSONObject("message"));
			this.body = message;
		} else if (value.has("delivery")) {
			this.type = MessageType.delivery;
			NotifyMessage message = new NotifyMessage("delivery");
			message.deserialize(value.getJSONObject("delivery"));
			this.body = message;
		} else if (value.has("read")) {
			this.type = MessageType.read;
			NotifyMessage message = new NotifyMessage("read");
			message.deserialize(value.getJSONObject("read"));
			this.body = message;
		} else if (value.has("postback")) {
			this.type = MessageType.postback;
			PostBackMessage message = new PostBackMessage();
			message.deserialize(value.getJSONObject("postback"));
			this.body = message;
		} else if (value.has("optin")) {
			this.type = MessageType.plugin_optin;
			if (value.getJSONObject("optin").has("ref")) {
				this.body = value.getJSONObject("optin").getString("ref");
			}
		} else if (value.has("referral")) {
			this.type = MessageType.referreal;
			ReferralMessage message = new ReferralMessage();
			message.deserialize(value.getJSONObject("referral"));
			this.body = message;
		} else if (value.has("payment")) {
			this.type = MessageType.payment;
			this.body = value.getJSONObject("payment");
		} else if (value.has("checkout_update")) {
			this.type = MessageType.checkout;
			this.body = value.getJSONObject("checkout_update");
		} else if (value.has("pre_checkout")) {
			this.type = MessageType.pre_checkout;
			this.body = value.getJSONObject("pre_checkout");
		} else if (value.has("account_linking")) {
			this.type = MessageType.account_linking;
			AccountLinkingMessage message = new AccountLinkingMessage();
			message.deserialize(value.getJSONObject("account_linking"));
			this.body = message;
		}
	}
}
