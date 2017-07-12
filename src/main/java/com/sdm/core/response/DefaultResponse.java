/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sdm.core.response.model.Message;

/**
 *
 * @author Htoonlin
 * @param <T>
 */
@JsonPropertyOrder(value = { "code", "status", "content", "timestamp" })
public class DefaultResponse<T extends Serializable> implements IBaseResponse {

	private int code;
	private ResponseType status;

	public DefaultResponse() {
		this.code = 200;
		this.status = ResponseType.SUCCESS;
	}

	public DefaultResponse(int code, ResponseType status, T content) {
		this.code = code;
		this.status = status;
		this.content = content;
	}

	public DefaultResponse(T content) {
		this();
		this.content = content;
	}

	private T content;

	public void setContent(T content) {
		this.content = content;
	}

	@Override
	public long getTimestamp() {
		return (new Date()).getTime();
	}

	@Override
	public int getCode() {
		if (content instanceof Message) {
			Message message = (Message) this.content;
			if (message.getCode() != 204)
				return message.getCode();
		}
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@Override
	public ResponseType getStatus() {
		return status;
	}

	public void setStatus(ResponseType status) {
		this.status = status;
	}

	@Override
	public Object getContent() {
		return this.content;
	}
}
