/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 * @author Htoonlin
 */
@JsonPropertyOrder({"code", "status", "content", "extra", "timestamp"})
public interface IBaseResponse {
    @JsonGetter("code")
    public int getCode();
    
    @JsonGetter("status")
    public ResponseType getStatus();

    @JsonGetter("content")
    public Object getContent();
    
    @JsonGetter("timestamp")
    public long getTimestamp();
}
