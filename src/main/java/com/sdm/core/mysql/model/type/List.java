/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.mysql.model.type;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Htoonlin
 */
@JsonTypeName("list")
public class List implements PropertyType, Serializable {

    private java.util.List<String> values;

    public List() {
    }

    public List(String sql) {
        String sqlValues = sql.substring(sql.indexOf('('));
        sqlValues = sqlValues.replaceAll("', '", ",");
        this.values = Arrays.asList(sqlValues.substring(2, sqlValues.length() - 3).split(","));
    }

    public List(java.util.List<String> values) {
        this.values = values;
    }

    public java.util.List<String> getValues() {
        return values;
    }

    public void setValues(java.util.List<String> values) {
        this.values = values;
    }

    public void addValue(String value) {
        if (this.values == null) {
            this.values = new ArrayList<>();
        }
        this.values.add(value);
    }

    @Override
    public String defaultSQL() {
        return "ENUM('" + String.join("', '", values) + "')";
    }

    @Override
    public boolean validType(Object value) {
        return this.values.contains(String.valueOf(value));
    }
}
