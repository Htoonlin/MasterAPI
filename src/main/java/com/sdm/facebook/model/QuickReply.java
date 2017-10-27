package com.sdm.facebook.model;

import org.json.JSONObject;

public class QuickReply implements FacebookSerialize {

    /**
     *
     */
    private static final long serialVersionUID = -8274764356753923359L;

    private boolean locationType;
    private String title;
    private String payload;
    private String image;

    @Override
    public void deserialize(JSONObject value) {
        if (value.has("content_type")) {
            this.locationType = value.getString("content_type").equalsIgnoreCase("location");
        }

        if (value.has("title")) {
            this.title = value.getString("title");
        }

        if (value.has("payload")) {
            this.title = value.getString("payload");
        }

        if (value.has("image_url")) {
            this.title = value.getString("image_url");
        }
    }

    @Override
    public JSONObject serialize() {
        JSONObject quickReply = new JSONObject();
        quickReply.put("content_type", (locationType ? "location" : "text"));
        if (this.title != null && this.title.length() > 0) {
            quickReply.put("title", this.title);
        }
        if (this.payload != null && this.payload.length() > 0) {
            quickReply.put("payload", this.payload);
        }
        if (this.image != null && this.image.length() > 0) {
            quickReply.put("image_url", this.image);
        }
        return quickReply;
    }

}
