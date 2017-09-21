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
	public JSONObject serialize() {
		JSONObject postback = new JSONObject();
		postback.put("payload", this.payload);
		postback.put("referral", super.serialize());
		return postback;
	}

	@Override
	public void deserialize(JSONObject value) {
		if (value.has("payload")) {
			this.payload = value.getString("payload");
		}
		if (value.has("referral")) {
			super.deserialize(value.getJSONObject("referral"));
		}
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

}
