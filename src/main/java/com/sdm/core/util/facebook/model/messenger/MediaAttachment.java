/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util.facebook.model.messenger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Htoonlin
 */
public class MediaAttachment implements IAttachment, Serializable {

    public enum Type {
        IMAGE,
        AUDIO,
        VIDEO,
        FILE;
    }

    private static final long serialVersionUID = 1L;

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
}
