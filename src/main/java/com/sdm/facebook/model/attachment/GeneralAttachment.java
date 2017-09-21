package com.sdm.facebook.model.attachment;

import org.json.JSONObject;

import com.sdm.facebook.model.FacebookSerialize;
import com.sdm.facebook.model.type.AttachmentType;

public class GeneralAttachment implements FacebookSerialize {

	/**
	 *
	 */
	private static final long serialVersionUID = 5249453189678864653L;

	private String url;

	private String title;

	private AttachmentType type;

	@Override
	public JSONObject serialize() {
		JSONObject attachment = new JSONObject();
		if (this.type != null) {
			attachment.put("type", this.type.toString());
		}

		if (this.title != null && this.title.length() > 0) {
			attachment.put("title", this.title);
			if (this.url != null && this.url.length() > 0) {
				attachment.put("url", this.url);
			}
		} else if (this.url != null && this.url.length() > 0) {
			attachment.put("payload", new JSONObject().put("url", this.url));
		}

		return attachment;
	}

	@Override
	public void deserialize(JSONObject value) {
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
