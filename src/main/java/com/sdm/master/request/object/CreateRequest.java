/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.request.object;

import com.sdm.core.request.DefaultRequest;
import com.sdm.core.mysql.model.PropertyModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @version 1.0
 * @author Htoonlin
 */
public class CreateRequest extends DefaultRequest implements Serializable {
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

    public CreateRequest() {
    }

    public CreateRequest(String name, List<PropertyModel> properties) {
        this.name = name;
        this.properties = properties;
    }

    public CreateRequest(String name, List<PropertyModel> properties, boolean temporary) {
        this.name = name;
        this.properties = properties;
        this.temporary = temporary;
    }

    public CreateRequest(String name, List<PropertyModel> properties, boolean temporary, String description, int maxRecords) {
        this.name = name;
        this.properties = properties;
        this.temporary = temporary;
        this.description = description;
        this.maxRecords = maxRecords;
    }

    @NotNull
    @Size(min = 6, max = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull    
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
