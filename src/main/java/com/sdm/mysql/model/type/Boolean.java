/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.mysql.model.type;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;

/**
 *
 * @author Htoonlin
 */
@JsonTypeName("boolean")
public class Boolean implements PropertyType, Serializable {

    @Override
    public String defaultSQL() {
        return "BIT(1)";
    }

    @Override
    public boolean validType(Object value) {
        if (value instanceof Boolean) {
            return true;
        } else if (value instanceof Integer) {
            return true;
        } else if (value instanceof String) {
            return true;
        }
        return false;
    }
}
