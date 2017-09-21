package com.sdm.facebook.model;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sdm.facebook.model.attachment.GeneralAttachment;
import com.sdm.facebook.model.type.MessageTag;
import com.sdm.facebook.model.type.NotificationType;
import com.sdm.facebook.model.type.SenderAction;

/**
 * It is the main API used to send messages to users. Ref :
 * https://developers.facebook.com/docs/messenger-platform/send-api-reference
 *
 * @author htoonlin
 *
 */
public class MessageBuilder {

	private JSONObject recipient;
	private JSONObject message;
	private JSONArray quickReplies;

	public MessageBuilder() {
		this.recipient = new JSONObject();
		this.message = new JSONObject();
	}

	public JSONObject buildMessage() {
		return this.buildMessage(null, null);
	}

	/**
	 * Set typing indicators or send read receipts using the Send API, to let users
	 * know you are processing their request.
	 *
	 * @param action
	 * @return
	 */
	public JSONObject buildAction(SenderAction action) {
		JSONObject result = new JSONObject();
		result.put("recipient", this.recipient);
		if (action != null) {
			result.put("sender_action", action.toString());
		}

		return result;
	}

	/**
	 *
	 * @param senderAction
	 * @param notificationType
	 * @return
	 */
	public JSONObject buildMessage(NotificationType notificationType, MessageTag tag) {
		JSONObject result = new JSONObject();
		result.put("recipient", this.recipient);

		if (quickReplies != null && quickReplies.length() > 0) {
			this.message.put("quick_replies", this.quickReplies);
		}

		result.put("message", this.message);

		if (notificationType != null) {
			result.put("notification_type", notificationType);
		}

		if (tag != null) {
			result.put("tag", tag.toString());
		}
		return result;
	}

	/**
	 * Page-scoped user ID of the recipient. This is the field most developers will
	 * commonly use to send messages.
	 *
	 * @param userId
	 * @return
	 */
	public MessageBuilder setRecipientId(String userId) {
		this.recipient.put("id", userId);
		return this;
	}

	/**
	 *
	 *
	 * @param phone
	 * @return
	 */
	public MessageBuilder setRecipientPhone(String phone) {
		this.recipient.put("phone_number", phone);
		return this;
	}

	/**
	 * If passing a phone number, also passing the user's name that you have on file
	 * will increase the odds of a successful match. Specify it as an object with
	 * the format:
	 *
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	public MessageBuilder setRecipientName(String firstName, String lastName) {
		JSONObject name = new JSONObject();
		name.put("first_name", firstName);
		name.put("last_name", lastName);
		this.recipient.put("name", name);
		return this;
	}

	/**
	 * Message text. Previews will not be shown for the URLs in this field.
	 *
	 * @param message
	 * @return
	 */
	public MessageBuilder setText(String message) {
		this.message.put("text", message);
		return this;
	}

	public MessageBuilder setTemplate(JSONObject payload) {
		JSONObject attachment = new JSONObject();
		attachment.put("type", "template");
		attachment.put("payload", payload);
		this.message.put("attachment", attachment);
		return this;
	}

	/**
	 * attachment object. Previews the URL. .
	 *
	 * @param type
	 *            It will support image, audio, video, file attachments
	 * @param url
	 * @param reuse
	 *            (This API allows you to upload an attachment that you may later
	 *            send out to many users, without having to repeatedly upload the
	 *            same data each time it is sent.)
	 * @return
	 */
	public MessageBuilder setFile(GeneralAttachment attachment, boolean reuse) {
		JSONObject jsonAttachment = attachment.serialize();
		if (reuse) {
			jsonAttachment.getJSONObject("payload").put("is_reusable", true);
		}
		this.message.put("attachment", jsonAttachment);
		return this;
	}

	/**
	 * Quick Replies provide a way to present buttons to the user in response to a
	 * message.s
	 *
	 * @param quickReply
	 * @return
	 */
	public MessageBuilder addQuickReply(QuickReply quickReply) {
		this.quickReplies.put(quickReply.serialize());
		return this;
	}

	/**
	 * Custom string that is delivered as a message echo.
	 *
	 * @param metaData
	 * @return
	 */
	public MessageBuilder setMetaData(String metaData) {
		this.message.put("metadata", metaData);
		return this;
	}

}
