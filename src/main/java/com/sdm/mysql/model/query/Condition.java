/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.mysql.model.query;

import com.sdm.mysql.model.SQLModel;
import java.io.Serializable;

/**
 *
 * @author Htoonlin
 */
public class Condition extends SQLModel implements Serializable {

    private Logical logic;
    private Operator operator;
    private String column;
    private Object value;

    @Override
    public String defaultSQL(){
        return logic.getValue() + " " + quoteName(column)
                + " " + operator.getValue() + " ?";
    }
    
    public Condition() {
    }

    public Condition(Logical logic, Operator operator, String column, Object value) {
        this.logic = logic;
        this.operator = operator;
        this.column = column;
        this.value = value;
    }

    public Logical getLogic() {
        return logic;
    }

    public void setLogic(Logical logic) {
        this.logic = logic;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
