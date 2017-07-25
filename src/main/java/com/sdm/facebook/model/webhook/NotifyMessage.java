package com.sdm.facebook.model.webhook;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sdm.facebook.model.FacebookSerialize;

public class NotifyMessage extends FacebookSerialize {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4431046390411955130L;

	/**
	 * Array containing message IDs of messages that were delivered. Field may not
	 * be present.
	 */
	private List<String> messageIds;

	/**
	 * Messages are delivery or read.
	 */
	private String status;

	/**
	 * All messages that were sent before this timestamp were delivered.
	 */
	private long watermark;

	/**
	 * Sequence number
	 */
	private int sequence;

	public NotifyMessage(String status) {
		this.status = status;
	}

	@Override
	public void setJson(JSONObject value) {
		if (value.has("watermark")) {
			this.watermark = value.getLong("watermark");
		}
		if (value.has("seq")) {
			this.sequence = value.getInt("seq");
		}
		if (value.has("mids")) {
			JSONArray ids = value.getJSONArray("mids");
			for (int i = 0; i < ids.length(); i++) {
				this.addMessageId(ids.getString(i));
			}
		}
		super.setJson(value);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void addMessageId(String id) {
		if (this.messageIds == null) {
			this.messageIds = new ArrayList<>();
		}

		this.messageIds.add(id);
	}

	public List<String> getMessageIds() {
		return messageIds;
	}

	public void setMessageIds(List<String> messageIds) {
		this.messageIds = messageIds;
	}

	public long getWatermark() {
		return watermark;
	}

	public void setWatermark(long watermark) {
		this.watermark = watermark;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

}
