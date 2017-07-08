/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util.facebook.model.messenger;

import java.io.Serializable;

/**
 *
 * @author Htoonlin
 */
public class QuickReply implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Contenty Type must be => text, location
    private String contentType;
    private String title;
    private String payload;
    private String imageUrl;

    public QuickReply(String contentType) {
        this.contentType = contentType;
    }
    public QuickReply(String title, String payload){        
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
}
