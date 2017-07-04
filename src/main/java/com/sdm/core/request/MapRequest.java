/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.request;

import com.sdm.core.response.PropertiesResponse;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author htoonlin
 */
public class MapRequest extends HashMap implements IBaseRequest, Serializable{

    public MapRequest(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public MapRequest(int initialCapacity) {
        super(initialCapacity);
    }

    public MapRequest() {
    }

    public MapRequest(Map m) {
        super(m);
    }
    
    private Date timestamp;

    public Date getTimestamp() {
        return this.timestamp;
    }

    @Override
    public void setTimeStamp(long date) {
        this.timestamp = new Date(date);
    }

    private Map<String, String> errors;

    protected void addError(String key, String value) {
        if (errors == null) {
            errors = new HashMap<>();
        }
        errors.put(key, value);
    }

    @Override
    public Map<String, String> getErrors() {
        return errors;
    }
    @Override
    public boolean isValid() {
        return (errors == null || errors.size() <= 0);
    }

    @Override
    public List<PropertiesResponse> getStructure() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
}
