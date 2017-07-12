/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response.model;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 * @author Htoonlin
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder(value = { "code", "title", "message", "trace" })
public class MessageModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -459150304625745739L;

	public MessageModel() {
	}

	public MessageModel(int code, String title, String message) {
		this.code = code;
		this.title = title;
		this.message = message;
	}

	private int code;
	private String title;
	private String message;
	private Map<String, Object> trace;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

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

	public Map<String, Object> getTrace() {
		return trace;
	}

	public void setTrace(Map<String, Object> trace) {
		this.trace = trace;
	}
}
