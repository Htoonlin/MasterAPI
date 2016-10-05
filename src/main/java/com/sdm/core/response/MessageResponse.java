/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Htoonlin
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"title", "message"})
public class MessageResponse implements IResponseContent, Serializable {
    
    public MessageResponse(int code, ResponseType type, String title, String message) {
        this.code = code;
        this.type = type;        
        this.title = title;
        this.message = message;
    }
    
    public MessageResponse(){}

    private int code;
    private ResponseType type;
    private String title;
    private String message;
    private Map<String, Object> debug;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public Map<String, Object> getDebug() {
        return debug;
    }

    public void setDebug(Map<String, Object> debug) {
        this.debug = debug;
    }
    
    @Override
    public int getResponseCode() {
        return this.code;
    }

    @Override
    public ResponseType getResponseStatus() {
        return this.type;
    }
}
