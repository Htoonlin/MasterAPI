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
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IResponseContent;
import com.sdm.core.response.ListResponse;
import com.sdm.core.response.PropertiesResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.core.ui.UIStructure;
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

    @JsonIgnore
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

    @Override
    public int getResponseCode() {
        return 200;
    }

    @Override
    public ResponseType getResponseStatus() {
        return ResponseType.SUCCESS;
    }
}
