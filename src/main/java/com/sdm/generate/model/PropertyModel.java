package com.sdm.generate.model;

import java.io.Serializable;

import com.sdm.core.ui.UIProperty;

public class PropertyModel extends UIProperty implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1027044065751863361L;

	private boolean auditable;	
	private String jsonName;
	private boolean jsonIgnore;
	private String validationRegex;

	public boolean isAuditable() {
		return auditable;
	}

	public void setAuditable(boolean auditable) {
		this.auditable = auditable;
	}

	public String getJsonName() {
		return jsonName;
	}

	public void setJsonName(String jsonName) {
		this.jsonName = jsonName;
	}
	
	public boolean isJsonIgnore() {
		return jsonIgnore;
	}

	public void setJsonIgnore(boolean jsonIgnore) {
		this.jsonIgnore = jsonIgnore;
	}

	public String getValidationRegex() {
		return validationRegex;
	}

	public void setValidationRegex(String validationRegex) {
		this.validationRegex = validationRegex;
	}
}
