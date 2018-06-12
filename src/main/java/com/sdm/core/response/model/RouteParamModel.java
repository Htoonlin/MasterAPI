/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response.model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author htoonlin
 */
public class RouteParamModel implements Serializable {

    private static final long serialVersionUID = -7289197922827033798L;

    private String defaultValue;
    private String type;
    private String paramType;

    public RouteParamModel(String defaultValue, String type) {
        this.defaultValue = defaultValue;
        this.type = type;
        this.paramType = null;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public boolean isRequire() {
        return (defaultValue == null || defaultValue.length() <= 0);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.defaultValue);
        hash = 79 * hash + Objects.hashCode(this.type);
        hash = 79 * hash + Objects.hashCode(this.paramType);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RouteParamModel other = (RouteParamModel) obj;
        if (!Objects.equals(this.defaultValue, other.defaultValue)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.paramType, other.paramType)) {
            return false;
        }
        return true;
    }

}
