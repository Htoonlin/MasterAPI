/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.mysql.model.type;

import java.io.Serializable;

/**
 *
 * @author Htoonlin
 */
public class Boolean implements PropertyType, Serializable {

    @Override
    public String defaultSQL() {
        return "BIT(1)";
    }

    @Override
    public String getName() {
        return "boolean";
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
