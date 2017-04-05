/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.mysql.model.type;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Htoonlin
 */
@JsonTypeName("datetime")
public class DateTime implements PropertyType, Serializable {

    public enum DateTimeType implements Serializable {
        DATETIME("^\\d\\d\\d\\d-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01]) (00|[0-9]|1[0-9]|2[0-3]):([0-9]|[0-5][0-9]):([0-9]|[0-5][0-9])$"),
        DATE("^\\d\\d\\d\\d-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])$"),
        TIME("^(00|[0-9]|1[0-9]|2[0-3]):([0-9]|[0-5][0-9]):([0-9]|[0-5][0-9])$"),
        TIMESTAMP("^\\d\\d\\d\\d-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01]) (00|[0-9]|1[0-9]|2[0-3]):([0-9]|[0-5][0-9]):([0-9]|[0-5][0-9])$"),
        YEAR("^\\d\\d\\d\\d$");

        private final String pattern;

        private DateTimeType(String pattern) {
            this.pattern = pattern;
        }

        public boolean checkValue(String value) {
            return value.matches(pattern);
        }
    }

    public DateTime() {
    }

    public DateTime(DateTimeType type) {
        this.type = type;
    }

    private DateTimeType type;

    public DateTimeType getType() {
        return type;
    }

    public void setType(DateTimeType type) {
        this.type = type;
    }

    @Deprecated
    public void setSQL(String sql) {
        int i = sql.indexOf('(');
        if (i > 0) {
            sql = sql.substring(0, i);
        }
        this.type = DateTimeType.valueOf(sql.trim().toLowerCase());
    }

    @Override
    public String defaultSQL() {
        return this.type.toString().toUpperCase();
    }

    @Override
    public boolean validType(Object value) {
        if (value instanceof Date) {
            return true;
        } else if ((this.type.equals(DateTimeType.DATETIME)
                || this.type.equals(DateTimeType.TIMESTAMP)
                || this.type.equals(DateTimeType.DATE)) && value instanceof Long) {
            return true;
        } else {
            return this.type.checkValue(value.toString());
        }
    }

}
