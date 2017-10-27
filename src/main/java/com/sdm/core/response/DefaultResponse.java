/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sdm.core.hibernate.entity.DefaultEntity;
import com.sdm.core.response.model.MessageModel;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Htoonlin
 * @param <T>
 */
@JsonPropertyOrder(value = {"code", "status", "content", "timestamp"})
public class DefaultResponse<T extends Serializable> implements IBaseResponse {

    private int code;
    private ResponseType status;
    private Map<String, Object> headers;

    public DefaultResponse(int code, ResponseType status, T content) {
        this.code = code;
        this.status = status;
        this.content = content;
    }

    public DefaultResponse(T content) {
        // Get Code from message model
        this.code = 200;
        this.status = ResponseType.UNKNOWN;
        this.content = content;

        if (content instanceof MessageModel) {
            MessageModel message = (MessageModel) this.content;
            if (message.getCode() != 204) {
                this.code = message.getCode();
            }

            // Define status on code.
            if (this.code >= 100 && this.code < 200) {
                this.status = ResponseType.INFO;
            } else if (this.code >= 200 && this.code < 300) {
                this.status = ResponseType.SUCCESS;
            } else if (this.code >= 400 && this.code < 500) {
                this.status = ResponseType.CLIENT_ERROR;
            } else if (this.code >= 500 && this.code < 600) {
                this.status = ResponseType.CLIENT_ERROR;
            }
        } else if (content instanceof DefaultEntity) {
            this.status = ResponseType.SUCCESS;
        }

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

    public void addHeader(String key, String value) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.put(key, value);
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    @Override
    public Map<String, Object> getHeaders() {
        return this.headers;
    }
}
