/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.sdm.core.Globalizer;
import java.io.Serializable;

/**
 *
 * @author Htoonlin
 */
public class PropertiesResponse implements Serializable {

    private String name;
    private String label;
    private String type;
    private String dbName;
    private String dbType;
    private String special;
    private Boolean primaryKey;
    private boolean nullable;
    private boolean hideInGrid;
    private boolean readOnly;
    private int orderIndex;

    public PropertiesResponse() {
        this.hideInGrid = false;
        this.readOnly = false;
    }

    @JsonGetter("request_name")
    public String getRequestName() {
        return Globalizer.camelToLowerUnderScore(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        if (label.length() <= 0) {
            return name;
        }
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDbName() {
        if (dbName == null) {
            dbName = name;
        }
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public boolean getNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public Boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isHideInGrid() {
        return hideInGrid;
    }

    public void setHideInGrid(boolean hideInGrid) {
        this.hideInGrid = hideInGrid;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

}
