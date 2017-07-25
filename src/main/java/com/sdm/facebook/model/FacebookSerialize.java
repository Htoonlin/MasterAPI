package com.sdm.facebook.model;

import java.io.Serializable;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FacebookSerialize implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5752100670074099844L;	

	@JsonIgnore
	private JSONObject json;

	public JSONObject getJson() {
		return json;
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}
}
