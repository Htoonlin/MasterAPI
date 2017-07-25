package com.sdm.facebook.model.webhook;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sdm.facebook.model.FacebookSerialize;

/**
 * 
 * @author htoonlin
 *
 */
public class MessengerEntry extends FacebookSerialize {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7344678089072825736L;

	/**
	 * Page ID of page
	 */
	private String pageId;

	/**
	 * Time of update (epoch time in milliseconds)
	 */
	private long timestamp;

	/**
	 * Array containing objects related to messaging
	 */
	private List<BaseMessage> messages;

	public MessengerEntry() {
	}

	@Override
	public void setJson(JSONObject value) {
		if (value.has("id")) {
			this.pageId = value.getString("id");
		}

		if (value.has("time")) {
			this.timestamp = value.getLong("time");
		}

		if (value.has("messaging")) {
			JSONArray messages = value.getJSONArray("messaging");
			for (int i = 0; i < messages.length(); i++) {
				BaseMessage message = new BaseMessage();
				message.setJson(messages.getJSONObject(i));
				this.addMessage(message);
			}
		}
		
		super.setJson(value);
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public void addMessage(BaseMessage message) {
		if (this.messages == null) {
			this.messages = new ArrayList<>();
		}
		this.messages.add(message);
	}

	public List<BaseMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<BaseMessage> messages) {
		this.messages = messages;
	}

}
