/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util.facebook.model.messenger;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Htoonlin
 */
public class MediaAttachment implements IAttachment, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2362817535413517369L;

	public enum Type {
		IMAGE, AUDIO, VIDEO, FILE;
	}

	@JsonIgnore
	private final Type type;

	@JsonIgnore
	private final String url;

	public MediaAttachment(Type type, String url) {
		this.type = type;
		this.url = url;
	}

	@Override
	public String getType() {
		return type.toString().toLowerCase();
	}

	@Override
	public Object getPayload() {
		Map<String, String> payload = new HashMap<>();
		payload.put("url", url);
		return payload;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		MediaAttachment other = (MediaAttachment) obj;
		if (type != other.type)
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

}
