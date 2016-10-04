/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.request.query;

import java.io.Serializable;

/**
 *
 * @author Htoonlin
 */
public class Condition implements Serializable {

    private Logical logic;
    private Expression expression;
    private String column;
    private Object value;

    public Logical getLogic() {
        return logic;
    }

    public void setLogic(Logical logic) {
        this.logic = logic;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
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
