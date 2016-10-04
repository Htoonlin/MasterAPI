/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.Map;

/**
 *
 * @author Htoonlin
 */
public interface IBaseRequest {
    @JsonSetter("timestamp")
    public void setTimeStamp(long date);

    @JsonSetter("extra")
    public void setExtra(Map<String, Object> extra);

    @JsonIgnore
    public Map<String,String> getErrors();

    @JsonIgnore
    public boolean isValid();
}
