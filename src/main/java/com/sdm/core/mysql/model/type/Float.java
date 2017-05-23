/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.mysql.model.type;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Htoonlin
 */
@JsonTypeName("float")
public class Float implements PropertyType, Serializable {

    public enum FloatType implements Serializable {
        FLOAT("^[+-]?(\\d+([.]\\d*)?|[.]\\d+){0,24}$"),
        DOUBLE("^[+-]?(\\d+([.]\\d*)?|[.]\\d+){0,55}$"),
        DECIMAL("^[+-]?(\\d+([.]\\d*)?|[.]\\d+){0,65}$");

        private final String pattern;

        private FloatType(String pattern) {
            this.pattern = pattern;
        }

        public boolean checkValue(String value) {
            return value.matches(pattern);
        }
    }

    private FloatType type;
    private int length;
    private int point;

    public Float() {
    }

    public Float(String sql) {
        String typeString = sql;
        String[] infoString = null;
        int i = sql.indexOf('(');
        if (i > 0) {
            typeString = sql.substring(0, i);
            infoString = sql.substring(i + 1, sql.length() - 1).split(",");
        }

        if (infoString != null && infoString.length == 2) {
            this.length = java.lang.Integer.parseInt(infoString[0]);
            this.point = java.lang.Integer.parseInt(infoString[1]);
        }

        this.type = FloatType.valueOf(typeString);
    }

    public Float(FloatType type, int length, int point) {
        this.type = type;
        this.length = length;
        this.point = point;
    }

    public FloatType getType() {
        return type;
    }

    public void setType(FloatType type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    @Override
    public String defaultSQL() {
        String sql = this.type.toString();
        if (this.length > 0) {
            sql += "(" + length;

            if (this.point > 0) {
                sql += "," + point;
            }

            sql += ")";
        }

        return sql;
    }

    @Override
    public boolean validType(Object value) {
        if (value instanceof Float && type.equals(FloatType.FLOAT)) {
            return true;
        } else if (value instanceof Double && type.equals(FloatType.DOUBLE)) {
            return true;
        } else if (value instanceof BigDecimal && type.equals(FloatType.DECIMAL)) {
            return true;
        } else {
            return type.checkValue(String.valueOf(value));
        }
    }
}
