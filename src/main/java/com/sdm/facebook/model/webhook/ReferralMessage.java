package com.sdm.facebook.model.webhook;

import com.sdm.facebook.model.FacebookSerialize;
import org.json.JSONObject;

/**
 *
 * @author htoonlin Reference : https://developers.facebook.com/docs/messenger-platform/webhook-reference/referral
 */
public class ReferralMessage implements FacebookSerialize {

    /**
     *
     */
    private static final long serialVersionUID = -5085297294538643176L;

    private String source;
    private String type;
    private String ref;
    private String adId;

    @Override
    public JSONObject serialize() {
        return new JSONObject(this);
    }

    @Override
    public void deserialize(JSONObject value) {
        if (value.has("source")) {
            this.source = value.getString("source");
        }

        if (value.has("type")) {
            this.type = value.getString("type");
        }

        if (value.has("ref")) {
            this.ref = value.getString("ref");
        }

        if (value.has("ad_id")) {
            this.adId = value.getString("ad_id");
        }

    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

}
