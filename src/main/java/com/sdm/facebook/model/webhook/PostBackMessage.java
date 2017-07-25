package com.sdm.facebook.model.webhook;

import org.json.JSONObject;

public class PostBackMessage extends ReferralMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1268975340559770781L;

	/**
	 * payload parameter that was defined with the button
	 */
	private String payload;

	@Override
	public void setJson(JSONObject value) {
		if (value.has("payload")) {
			this.payload = value.getString("payload");
		}
		super.setJson(value);
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

}
