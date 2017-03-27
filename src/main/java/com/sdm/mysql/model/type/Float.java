/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.mysql.model.type;

import com.sdm.mysql.model.SQLModel;
import java.io.Serializable;

/**
 *
 * @author Htoonlin
 */
public class Float extends SQLModel implements PropertyType, Serializable {

    public enum FloatType implements Serializable {
        FLOAT(""),
        DOUBLE(""),
        DECIMAL("");
        
        private final String pattern;
        private FloatType(String pattern){
            this.pattern = pattern;
        }
    }

    private FloatType type;
    private int length;
    private int point;

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
        return this.getSQL();
    }

    @Override
    public String getName() {
        return this.type.toString();
    }

    @Override
    public String getSQL() {
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
        return false;
    }
}
