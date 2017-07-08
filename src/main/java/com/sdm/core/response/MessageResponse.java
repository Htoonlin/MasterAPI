/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Htoonlin
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MessageResponse implements IBaseResponse, Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MessageResponse(int code, ResponseType type,  String content) {
        this.code = code;
        this.status = type;        
        this.content = content;
    }
    
    public MessageResponse(){}

    private int code;
    private ResponseType status;
    private String content;
    
    @JsonProperty("extra")
    private Map<String, Object> debug;

    public void setStatus(ResponseType status) {
        this.status = status;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setType(ResponseType type) {
        this.status = type;
    }
    
    @JsonGetter("extra")
    public Map<String, Object> getDebug() {
        return debug;
    }

    public void setDebug(Map<String, Object> debug) {
        this.debug = debug;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public ResponseType getStatus() {
        return this.status;
    }

    @Override
    public Object getContent() {
        return this.content;
    }

    @Override
    public long getTimestamp() {
        return (new Date()).getTime();
    }
}
