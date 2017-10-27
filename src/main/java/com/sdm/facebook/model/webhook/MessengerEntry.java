package com.sdm.facebook.model.webhook;

import com.sdm.facebook.model.FacebookSerialize;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author htoonlin
 *
 */
public class MessengerEntry implements FacebookSerialize {

    /**
     *
     */
    private static final long serialVersionUID = -7344678089072825736L;

    /**
     * Page ID of page
     */
    private String pageId;

    /**
     * Time of update (epoch time in milliseconds)
     */
    private long timestamp;

    /**
     * Array containing objects related to messaging
     */
    private List<BaseMessage> messages;

    public MessengerEntry() {
    }

    @Override
    public JSONObject serialize() {
        JSONObject entry = new JSONObject();
        if (this.pageId != null && this.pageId.length() > 0) {
            entry.put("id", this.pageId);
        }

        entry.put("time", this.timestamp);
        if (this.messages != null && this.messages.size() > 0) {
            JSONArray messaging = new JSONArray();
            for (BaseMessage message : this.messages) {
                messaging.put(message.serialize());
            }
            entry.put("messaging", messaging);
        }
        return entry;
    }

    @Override
    public void deserialize(JSONObject value) {
        if (value.has("id")) {
            this.pageId = value.getString("id");
        }

        if (value.has("time")) {
            this.timestamp = value.getLong("time");
        }

        if (value.has("messaging")) {
            JSONArray messages = value.getJSONArray("messaging");
            for (int i = 0; i < messages.length(); i++) {
                BaseMessage message = new BaseMessage();
                message.deserialize(messages.getJSONObject(i));
                this.addMessage(message);
            }
        }
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void addMessage(BaseMessage message) {
        if (this.messages == null) {
            this.messages = new ArrayList<>();
        }
        this.messages.add(message);
    }

    public List<BaseMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<BaseMessage> messages) {
        this.messages = messages;
    }

}
