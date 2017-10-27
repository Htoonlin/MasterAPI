/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Map;

/**
 *
 * @author Htoonlin
 */
@JsonPropertyOrder({"code", "status", "content", "timestamp"})
public interface IBaseResponse {

    @JsonGetter("code")
    public int getCode();

    @JsonGetter("status")
    public ResponseType getStatus();

    @JsonGetter("content")
    public Object getContent();

    @JsonGetter("timestamp")
    public long getTimestamp();

    @JsonIgnore
    public Map<String, Object> getHeaders();
}
