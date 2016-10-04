/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author Htoonlin
 */
public class ErrorResponse implements IBaseResponse{

    public ErrorResponse() {
    }

    public ErrorResponse(Map<String, String> errors) {
        this.errors = errors;
    }
    
    private Map<String, String> errors;    

    @JsonIgnore
    public void setContent(Map<String, String> errors) {
        this.errors = errors;
    }

    @Override
    public long getTimestamp() {
        return (new Date()).getTime();
    }

    @Override
    public int getCode() {
        return 400;
    }

    @Override
    public ResponseType getStatus() {
        return ResponseType.WARNING;
    }

    @Override
    public Map<String, String> getContent() {
        return this.errors;
    }
}
