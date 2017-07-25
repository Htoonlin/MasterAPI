package com.sdm.facebook.model.attachment;

import org.json.JSONObject;

import com.sdm.facebook.model.FacebookSerialize;
import com.sdm.facebook.model.type.AttachmentType;

public class GeneralAttachment extends FacebookSerialize {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5249453189678864653L;

	private String url;

	private String title;

	private AttachmentType type;

	@Override
	public void setJson(JSONObject value) {
		if (value.has("payload") && value.getJSONObject("payload").has("url")) {
			this.url = value.getJSONObject("payload").getString("url");
		} else if (value.has("url")) {
			this.url = value.getString("url");
			if (value.has("title")) {
				this.title = value.getString("title");
			}
		}

		if (value.has("type")) {
			this.type = AttachmentType.valueOf(value.getString("type"));
		}
		super.setJson(value);
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

	public AttachmentType getType() {
		return type;
	}

	public void setType(AttachmentType type) {
		this.type = type;
	}

}
