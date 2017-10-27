/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.ui;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sdm.core.Globalizer;
import java.io.Serializable;

/**
 *
 * @author Htoonlin
 */
@JsonPropertyOrder({"name", "label", "order_index", "@type", "length", "input_type", "is_primary", "is_required",
    "is_read_only", "is_hide_in_grid"})
public class UIProperty implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3497471114097791330L;
    private String name;

    @JsonProperty(value = "@type")
    private String type;

    @JsonProperty(value = "is_primary")
    private boolean primary;

    @JsonProperty(value = "label")
    private String label;

    @JsonProperty(value = "input_type")
    private UIInputType inputType;

    @JsonProperty(value = "is_read_only")
    private boolean readOnly;

    @JsonProperty(value = "is_hide_in_grid")
    private boolean hideInGrid;

    @JsonProperty(value = "is_required")
    private boolean required;

    @JsonProperty(value = "order_index")
    private int orderIndex;

    @JsonProperty(value = "length")
    private int length;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @JsonGetter("name")
    public String getRequestName() {
        return Globalizer.camelToLowerUnderScore(name);
    }

    public String getName() {
        return name;
    }

    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public UIInputType getInputType() {
        return inputType;
    }

    public void setInputType(UIInputType inputType) {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + (required ? 1231 : 1237);
        result = prime * result + orderIndex;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
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
        UIProperty other = (UIProperty) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (required != other.required) {
            return false;
        }
        if (orderIndex != other.orderIndex) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }

}
