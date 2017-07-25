package com.sdm.facebook.model.webhook;

import org.json.JSONObject;

import com.sdm.facebook.model.FacebookSerialize;

public class OptinMessage extends FacebookSerialize {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6216168454098592611L;
	/**
	 * data-ref parameter that was defined with the entry point
	 */
	private String ref;

	@Override
	public void setJson(JSONObject value) {
		if (value.has("ref")) {
			this.ref = value.getString("ref");
		}
		super.setJson(value);
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}
}
