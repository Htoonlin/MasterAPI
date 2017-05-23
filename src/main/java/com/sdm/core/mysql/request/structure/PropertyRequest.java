/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.mysql.request.structure;

import com.sdm.core.request.DefaultRequest;
import com.sdm.core.mysql.model.PropertyModel;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Htoonlin
 */
public class PropertyRequest extends DefaultRequest implements Serializable {

    private PropertyModel property;
    private String after;
    private boolean first;

    public PropertyRequest() {
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
