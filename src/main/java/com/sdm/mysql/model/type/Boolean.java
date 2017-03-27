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
public class Boolean extends SQLModel implements PropertyType, Serializable{

    public Boolean() {
    }

    @Override
    public String getSQL() {
        return "BIT(1)";
    }

    @Override
    public String getName() {
        return "boolean";
    }

    @Override
    public boolean validType(Object value) {
        if(value instanceof Boolean){
            return true;
        }else if(value instanceof Integer){
            return true;
        }else if(value instanceof String){
            return true;
        }
        return false;
    }

    @Override
    public String defaultSQL() {
        return this.getSQL();
    }
    
}
