package com.sdm.facebook.model;

import java.io.Serializable;

import org.json.JSONObject;

public interface FacebookSerialize extends Serializable {

	public void deserialize(JSONObject value);
	
	public JSONObject serialize();
}
