/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.mysql.request.object;

import com.sdm.core.request.DefaultRequest;
import com.sdm.mysql.model.PropertyModel;
import com.sdm.mysql.model.type.PropertyType;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Htoonlin
 */
public class AddPropertyRequest extends DefaultRequest implements Serializable {

    private String objectName;
    private PropertyModel property;
    private String after;
    private boolean first;

    public AddPropertyRequest() {
    }

    @NotNull
    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    @NotNull
    public PropertyModel getProperty() {
        return property;
    }

    public void setProperty(PropertyModel property) {
        this.property = property;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

}
