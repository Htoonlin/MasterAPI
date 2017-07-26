package com.sdm.facebook.model.webhook;

import org.json.JSONObject;

import com.sdm.facebook.model.FacebookSerialize;

public class AccountLinkingMessage implements FacebookSerialize {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1373683875504932086L;

	/**
	 * linked or unlinked
	 */
	private String linked;

	/**
	 * Value of pass-through authorization_code provided in the Account Linking flow
	 */
	private String authorizationCode;

	@Override
	public JSONObject serialize() {
		JSONObject account_linking = new JSONObject();
		if (this.linked != null && this.linked.length() > 0) {
			account_linking.put("status", this.linked);
		}
		if (this.authorizationCode != null && this.authorizationCode.length() > 0) {
			account_linking.put("authorization_code", this.authorizationCode);
		}
		return account_linking;
	}

	@Override
	public void deserialize(JSONObject value) {
		if (value.has("status")) {
			this.linked = value.getString("status");
		}

		if (value.has("authorization_code")) {
			this.authorizationCode = value.getString("authorization_code");
		}
	}

	public String getLinked() {
		return linked;
	}

	public void setLinked(String linked) {
		this.linked = linked;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}
}
