/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.request.model;

/**
 *
 * @author Htoonlin
 */
public enum Aggregate {
    AVG("AVG"),
    COUNT("COUNT"),
    MAX("MAX"),
    MIN("MIN"),
    SUM("SUM"),
    NON("");

    private final String value;

    Aggregate(String value) {
        this.value = value;
    }

    public String getSQL(String expression) {
        if (this.value.length() > 0) {
            return this.value + "(" + expression + ")";
        }
        return expression;
    }
}
