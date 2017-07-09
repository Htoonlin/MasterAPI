/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 * @author Htoonlin
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder(value = { "code", "status", "content", "timestamp" })
public class MessageResponse implements IBaseResponse, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MessageResponse(int code, ResponseType type, String message) {
		this.code = code;
		this.status = type;
		this.message = message;
	}

	public MessageResponse() {
	}

	private int code;
	private ResponseType status;

	@JsonIgnore
	private String message;
	
	@JsonIgnore
	private Map<String, Object> debug;

	public void setStatus(ResponseType status) {
		this.status = status;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setType(ResponseType type) {
		this.status = type;
	}

	public Map<String, Object> getDebug() {
		return debug;
	}

	public void setDebug(Map<String, Object> debug) {
		this.debug = debug;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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
		Map<String, Object> content = new HashMap<>();
		if(this.debug != null) {
			content.put("trace", this.debug);
		}
		content.put("message", this.message);
		return content;
	}

	@Override
	public long getTimestamp() {
		return (new Date()).getTime();
	}
}
