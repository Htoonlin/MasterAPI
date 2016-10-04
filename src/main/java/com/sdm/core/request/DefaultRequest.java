/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdm.core.Globalizer;
import com.sdm.core.Setting;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 *
 * @author Htoonlin
 */
public class DefaultRequest implements Serializable, IBaseRequest{
    private static final long serialVersionUID = 1L;

    private Date timestamp;

    public Date getTimestamp() {
        return this.timestamp;
    }

    @Override
    public void setTimeStamp(long date) {
        this.timestamp = new Date(date);
    }
    
    private Map<String, Object> extra;

    public Map<String, Object> getExtra() {
        return extra;
    }

    @Override
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

    private Map<String, String> errors;

    @Override
    public Map<String, String> getErrors() {
        return errors;
    }

    @Override
    public boolean isValid() {
        errors = new HashMap<>();
        if (!Setting.getInstance().ENVIRONMENT.equalsIgnoreCase("dev")) {
            if (timestamp == null || !Globalizer.validTimeStamp(timestamp)) {
                errors.put("timestamp", "Invalid timestamp.");
                return false;
            }
        }
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<DefaultRequest>> violoationSet = validator.validate(this);
        for (ConstraintViolation<DefaultRequest> v : violoationSet) {
            String propertyName = Globalizer.camelToLowerUnderScore(v.getPropertyPath().toString());
            errors.put(propertyName, v.getMessage());
        }
        
        return violoationSet.isEmpty();        
    }
}
