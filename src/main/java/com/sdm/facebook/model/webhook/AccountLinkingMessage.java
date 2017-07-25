package com.sdm.facebook.model.webhook;

import org.json.JSONObject;

import com.sdm.facebook.model.FacebookSerialize;

public class AccountLinkingMessage extends FacebookSerialize {

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
	public void setJson(JSONObject value) {
		if (value.has("status")) {
			this.linked = value.getString("status");
		}

		if (value.has("authorization_code")) {
			this.authorizationCode = value.getString("authorization_code");
		}
		super.setJson(value);
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
