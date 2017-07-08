/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sdm.core.Globalizer;

/**
 *
 * @author Htoonlin
 */
@JsonPropertyOrder({"name", "request_name", "type", "length", "label", "input_type", "primary", "nullable", "read_only", "hide_in_grid"})
public class UIProperty implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
    private String type;
    private Boolean primary;
    private String label;
    private String inputType;
    private boolean readOnly;
    private boolean hideInGrid;
    private boolean nullable;
    private int orderIndex;
    private int length;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
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
