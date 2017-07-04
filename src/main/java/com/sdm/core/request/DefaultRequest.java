/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdm.core.Globalizer;
import com.sdm.core.Setting;
import com.sdm.core.hibernate.UIStructure;
import com.sdm.core.response.PropertiesResponse;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

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

    @Override
    public List<PropertiesResponse> getStructure() {
        List<PropertiesResponse> properties = new ArrayList<>();
        for (Field field : this.getClass().getDeclaredFields()) {
            //Check has annotations
            if (field.getAnnotations().length <= 0) {
                continue;
            }

            //Check JsonIgnore 
            if (field.getAnnotation(JsonIgnore.class) != null) {
                continue;
            }

            PropertiesResponse property = new PropertiesResponse();
            //General info
            property.setName(field.getName());
            property.setType(field.getType().getSimpleName());

            if (field.getAnnotation(Id.class) != null) {
                property.setPrimary(true);
            }

            //UI Info
            UIStructure structure = field.getAnnotation(UIStructure.class);
            if (structure != null) {
                property.setInputType(structure.inputType());
                property.setLabel(structure.label());
                property.setHideInGrid(structure.hideInGrid());
                property.setReadOnly(structure.readOnly());
                property.setOrderIndex(structure.order());
            }

            //Db Info
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                if (column.nullable()) {
                    property.setNullable(column.nullable());
                }
                property.setLength(column.length());
            }

            //Validations Info
            properties.add(property);
        }

        Collections.sort(properties, new Comparator<PropertiesResponse>() {
            @Override
            public int compare(PropertiesResponse t1, PropertiesResponse t2) {
                return Integer.compare(t1.getOrderIndex(), t2.getOrderIndex());
            }
        });

        return properties;
    }
}
