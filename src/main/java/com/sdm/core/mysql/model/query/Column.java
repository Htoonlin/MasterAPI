/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.mysql.model.query;

import com.sdm.core.mysql.util.MySQLManager;
import java.io.Serializable;

/**
 *
 * @author Htoonlin
 */
public class Column implements Serializable {

    private Aggregate aggregate;
    private String name;
    private String alias;
    private boolean expression;

    public String defaultSQL() {
        String sql = "";
        if (aggregate != null && !aggregate.equals(Aggregate.NON)) {
            sql += aggregate.getSQL(this.getName());
        } else {
            sql += this.getName();
        }

        if (alias != null && alias.length() > 0) {
            sql += " AS " + MySQLManager.quoteName(alias);
        }

        return sql;
    }

    public Column(String name) {
        this.name = name;
    }

    public Column() {
    }

    public Column(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public Aggregate getAggregate() {
        return aggregate;
    }

    public void setAggregate(Aggregate aggregate) {
        this.aggregate = aggregate;
    }

    public boolean isExpression() {
        return expression;
    }

    public void setExpression(boolean expression) {
        this.expression = expression;
    }

    public String getName() {
        return "(" + (this.expression ? name : MySQLManager.quoteName(name)) + ")";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

}
