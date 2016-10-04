/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sdm.core.Globalizer;
import com.sdm.core.Setting;
import com.sdm.core.request.IBaseRequest;
import com.sdm.core.response.IResponseContent;
import com.sdm.core.response.ResponseType;
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
 * @param <PK>
 */

public abstract class RestEntity<PK extends Serializable> extends DefaultEntity implements IBaseRequest, IResponseContent {
    private static final long serialVersionUID = 1L;
        
    @JsonProperty(value = "id", index = -999)
    protected PK id;
        
    public abstract PK getId();
    @JsonSetter(value = "id")
    public abstract void setId(PK id);
    
    private Date timestamp;   
    
    @JsonIgnore
    public Date getTimestamp() {
        return this.timestamp;
    }

    @Override
    public void setTimeStamp(long date) {
        this.timestamp = new Date(date);
    }

    private Map<String, Object> extra;

    @JsonIgnore
    public Map<String, Object> getExtra() {
        return extra;
    }

    @Override
    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
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
        Set<ConstraintViolation<RestEntity<PK>>> violoationSet = validator.validate(this);
        for (ConstraintViolation<RestEntity<PK>> v : violoationSet) {
            String propertyName = Globalizer.camelToLowerUnderScore(v.getPropertyPath().toString());
            errors.put(propertyName, v.getMessage());
        }
        return violoationSet.isEmpty();        
    }

    @Override
    public int getResponseCode() {
        return 200;
    }

    @Override
    public ResponseType getResponseStatus() {
        return ResponseType.SUCCESS;
    }
}
