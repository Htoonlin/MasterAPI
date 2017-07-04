/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author Htoonlin
 * @param <T>
 */
@JsonPropertyOrder(value = {"code", "status", "content", "extra", "timestamp"})
public class DefaultResponse<T extends Serializable> implements IBaseResponse{
    
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
    private Map<String, Object> extra;
    
    public void setContent(T content) {
        this.content = content;
    }
    
    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

    @JsonIgnore
    public void putExtra(String key, Object obj) {
        if (this.extra == null) {
            this.extra = new HashMap<>();
        }
        this.extra.put(key, obj);
    }

    @JsonIgnore
    public Object getExtra(String key, Object defObject) {
        if (this.extra == null) {
            return defObject;
        }
        return this.extra.getOrDefault(key, defObject);
    }
    
    public Map<String, Object> getExtra() {
        return extra;
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
}
