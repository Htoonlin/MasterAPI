/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.request;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.sdm.core.Globalizer;
import com.sdm.core.Setting;

/**
 *
 * @author Htoonlin
 */
public class DefaultRequest implements Serializable, IBaseRequest {

    private static final long serialVersionUID = 1L;

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
        if (!Setting.getInstance().ENVIRONMENT.equalsIgnoreCase("dev")) {
            if (timestamp == null || !Globalizer.validTimeStamp(timestamp)) {
                addError("timestamp", "Invalid timestamp.");
                return false;
            }
        }
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<DefaultRequest>> violoationSet = validator.validate(this);
        for (ConstraintViolation<DefaultRequest> v : violoationSet) {
            String propertyName = Globalizer.camelToLowerUnderScore(v.getPropertyPath().toString());
            addError(propertyName, v.getMessage());
        }

        return violoationSet.isEmpty();
    }
}
