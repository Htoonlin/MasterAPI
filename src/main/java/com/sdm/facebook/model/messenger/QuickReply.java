/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.facebook.model.messenger;

import java.io.Serializable;

/**
 *
 * @author Htoonlin
 */
public class QuickReply implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6639614315498621385L;

	// Contenty Type must be => text, location
	private String contentType;
	private String title;
	private String payload;
	private String imageUrl;

	public QuickReply(String contentType) {
		this.contentType = contentType;
	}

	public QuickReply(String title, String payload) {
		this.title = title;
		this.payload = payload;
	}

	public QuickReply(String contentType, String title, String payload) {
		this.contentType = contentType;
	}

	public QuickReply(String contentType, String title, String payload, String imageUrl) {
		this.contentType = contentType;
		this.title = title;
		this.payload = payload;
		this.imageUrl = imageUrl;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
		result = prime * result + ((imageUrl == null) ? 0 : imageUrl.hashCode());
		result = prime * result + ((payload == null) ? 0 : payload.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		QuickReply other = (QuickReply) obj;
		if (contentType == null) {
			if (other.contentType != null)
				return false;
		} else if (!contentType.equals(other.contentType))
			return false;
		if (imageUrl == null) {
			if (other.imageUrl != null)
				return false;
		} else if (!imageUrl.equals(other.imageUrl))
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
		return true;
	}

}
