/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.facebook.model.messenger;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Htoonlin
 */
public class TemplateButton implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8386357143415731276L;

	public enum ButtonType {
		WEB_URL, POSTBACK, PHONE_NUMBER, ELEMENT_SHARE;
	}

	@JsonIgnore
	private ButtonType type;
	private String title;
	private String url;
	private String payload;
	private String webviewHeightRatio;
	private boolean messengerExtensions;
	private String fallbackUrl;

	public ButtonType getType() {
		return type;
	}

	@JsonGetter("type")
	public String getTypeString() {
		return type.toString().toLowerCase();
	}

	public void setType(ButtonType type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getWebviewHeightRatio() {
		return webviewHeightRatio;
	}

	public void setWebviewHeightRatio(String webviewHeightRatio) {
		this.webviewHeightRatio = webviewHeightRatio;
	}

	public boolean isMessengerExtensions() {
		return messengerExtensions;
	}

	public void setMessengerExtensions(boolean messengerExtensions) {
		this.messengerExtensions = messengerExtensions;
	}

	public String getFallbackUrl() {
		return fallbackUrl;
	}

	public void setFallbackUrl(String fallbackUrl) {
		this.fallbackUrl = fallbackUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fallbackUrl == null) ? 0 : fallbackUrl.hashCode());
		result = prime * result + (messengerExtensions ? 1231 : 1237);
		result = prime * result + ((payload == null) ? 0 : payload.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((webviewHeightRatio == null) ? 0 : webviewHeightRatio.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TemplateButton other = (TemplateButton) obj;
		if (fallbackUrl == null) {
			if (other.fallbackUrl != null)
				return false;
		} else if (!fallbackUrl.equals(other.fallbackUrl))
			return false;
		if (messengerExtensions != other.messengerExtensions)
			return false;
		if (payload == null) {
			if (other.payload != null)
				return false;
		} else if (!payload.equals(other.payload))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (type != other.type)
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (webviewHeightRatio == null) {
			if (other.webviewHeightRatio != null)
				return false;
		} else if (!webviewHeightRatio.equals(other.webviewHeightRatio))
			return false;
		return true;
	}

}
