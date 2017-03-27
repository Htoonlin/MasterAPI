/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.mysql.request;

import com.sdm.mysql.model.SQLModel;
import com.sdm.core.request.DefaultRequest;
import com.sdm.mysql.model.PropertyModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Current Support SQL List : (CREATE TABLE => buildSQL), (RENAME TABLE => renameSQL), (CLOEN TABLE => cloneSQL), (DROP TABLE => dropSQL), (Add new property => insertPropertySQL,addPropertyLastSQL,
 * addPropertyFirstSQL), (Modified property => editPropertySQL), (Move property => movePropertyAfterSQL, movePropertyFirstSQL).
 *
 * @version 1.0
 * @author Htoonlin
 */
public class ObjectRequest extends DefaultRequest implements Serializable {
    //name of MySQL Table
    private String name;

    //properties of Table
    private List<PropertyModel> properties;

    //If it has true, Table will create on RAM. Default value is false.
    private boolean temporary;

    //Write description for table if need.
    private String description;

    //Define max row limits of table.
    private int maxRecords;

    public ObjectRequest() {
    }

    public ObjectRequest(String name, List<PropertyModel> properties) {
        this.name = name;
        this.properties = properties;
    }

    public ObjectRequest(String name, List<PropertyModel> properties, boolean temporary) {
        this.name = name;
        this.properties = properties;
        this.temporary = temporary;
    }

    public ObjectRequest(String name, List<PropertyModel> properties, boolean temporary, String description, int maxRecords) {
        this.name = name;
        this.properties = properties;
        this.temporary = temporary;
        this.description = description;
        this.maxRecords = maxRecords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PropertyModel> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyModel> properties) {
        this.properties = properties;
    }

    public void addProperty(PropertyModel property) {
        if (this.properties == null) {
            this.properties = new ArrayList<>();
        }
        this.properties.add(property);
    }

    public boolean isTemporary() {
        return temporary;
    }

    public void setTemporary(boolean temporary) {
        this.temporary = temporary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxRecords() {
        return maxRecords;
    }

    public void setMaxRecords(int maxRecords) {
        this.maxRecords = maxRecords;
    }

}
