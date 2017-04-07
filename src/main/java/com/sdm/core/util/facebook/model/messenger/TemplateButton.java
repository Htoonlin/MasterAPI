/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util.facebook.model.messenger;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

/**
 *
 * @author Htoonlin
 */
public class TemplateButton implements Serializable{

    public enum ButtonType {
        WEB_URL,
        POSTBACK,
        PHONE_NUMBER,
        ELEMENT_SHARE;
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

}
