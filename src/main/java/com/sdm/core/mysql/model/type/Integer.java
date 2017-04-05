/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.mysql.model.type;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;

/**
 *
 * @author Htoonlin
 */
@JsonTypeName("integer")
public class Integer implements PropertyType, Serializable {

    private boolean serial;
    private IntegerType type;
    private boolean unsigned;

    public Integer() {
    }

    public Integer(IntegerType type) {
        this.type = type;
    }

    public Integer(IntegerType type, boolean unsigned) {
        this.type = type;
        this.unsigned = unsigned;
    }

    public Integer(boolean serial, IntegerType type) {
        this.serial = serial;
        this.type = type;
    }

    public boolean isSerial() {
        return serial;
    }

    public void setSerial(boolean serial) {
        this.serial = serial;
    }

    public IntegerType getType() {
        return type;
    }

    public void setType(IntegerType type) {
        this.type = type;
    }

    public boolean isUnsigned() {
        return unsigned;
    }

    public void setUnsigned(boolean unsigned) {
        this.unsigned = unsigned;
    }

    public enum IntegerType implements Serializable {
        TINYINT(255d),
        SMALLINT(65535d),
        MEDIUMINT(16777215d),
        INT(4294967295d),
        BIGINT(18446744073709551615d);

        private final double maxSize;

        private IntegerType(double size) {
            this.maxSize = size;
        }

        public boolean checkValue(double value, boolean unsigned) {
            if (unsigned && value > 0d && value < this.maxSize) {
                return true;
            } else if (!unsigned && Math.abs(value) < Math.abs(maxSize / 2)) {
                return true;
            }
            return false;
        }
    }

    @Deprecated
    public void setSQL(String sql) {
        int i = sql.indexOf('(');
        if (i > 0) {
            sql = sql.substring(0, i);
        }
        if (sql.trim().equalsIgnoreCase("integer")) {
            this.type = IntegerType.INT;
        } else {
            this.type = IntegerType.valueOf(sql.trim().toUpperCase());
        }
    }

    @Override
    public String defaultSQL() {
        String sql = this.type.toString();
        if (this.serial) {
            sql += " UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE";
        } else if (this.unsigned) {
            sql += " UNSIGNED";
        }
        return sql;
    }

    @Override
    public boolean validType(Object value) {
        String strValue = String.valueOf(value);
        if (strValue.matches("-?\\\\d+")) {
            return this.type.checkValue(Double.parseDouble(strValue), this.unsigned);
        }

        return false;
    }
}
