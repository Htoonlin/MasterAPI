/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.mysql.model;

import com.sdm.mysql.model.type.PropertyType;
import com.sdm.mysql.util.MySQLManager;
import java.io.Serializable;

/**
 *
 * @author Htoonlin
 */
public class PropertyModel implements Serializable {

    private String name;
    private boolean primary;
    private PropertyType type;

    private boolean serial;
    private int index;
    private String defaultValue;
    private String label;
    private String description;
    private String groupName;
    private boolean readOnly;
    private boolean showInGrid;
    private boolean require;
    private boolean searchable;

    public String defaultSQL() {
        String sql = MySQLManager.quoteName(this.name) + " " + this.type.defaultSQL();
        if (this.type instanceof com.sdm.mysql.model.type.Integer) {
            com.sdm.mysql.model.type.Integer integerType = (com.sdm.mysql.model.type.Integer) this.type;
            if (integerType.isSerial()) {
                this.primary = true;
                this.serial = true;
            }
        } else if (this.type instanceof com.sdm.mysql.model.type.Text) {
            com.sdm.mysql.model.type.Text textType = (com.sdm.mysql.model.type.Text) this.type;
            if (textType.isUniqueId()) {
                this.primary = true;
            }
        } else {
            if (this.require && !this.primary) {
                sql += " NULL";
            } else {
                sql += " NOT NULL";
            }

            if (!this.defaultValue.isEmpty()) {
                sql += " DEFAULT " + this.defaultValue;
            }
        }

        if (this.description != null && !this.description.isEmpty()) {
            sql += " COMMENT '" + MySQLManager.escapeQuery(this.description) + "'";
        }

        return sql;
    }

    public PropertyModel() {
        this.primary = false;
        this.serial = false;
        this.description = "";
    }

    public PropertyModel(String name, PropertyType type, int index, String label, boolean require) {
        this();
        this.name = name;
        this.type = type;
        this.index = index;
        this.label = label;
        this.require = require;
    }

    public boolean isSerial() {
        return serial;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public PropertyType getType() {
        return type;
    }

    public void setType(PropertyType type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getLabel() {
        if (this.label != null && this.label.length() > 0) {
            return label;
        } else {
            return name;
        }
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isShowInGrid() {
        return showInGrid;
    }

    public void setShowInGrid(boolean showInGrid) {
        this.showInGrid = showInGrid;
    }

    public boolean isRequire() {
        return require;
    }

    public void setRequire(boolean require) {
        this.require = require;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

}
