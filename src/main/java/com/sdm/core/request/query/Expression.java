/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.request.query;

/**
 *
 * @author Htoonlin
 */
public enum Expression {
    EQ("="),
    NEQ("!="),
    GT(">"),
    GTE(">="),
    LT("<"),
    LTE("<="),
    IN("IN"),
    NIN("NOT IN"),
    LIKE("LIKE"),
    NLIKE("NOT LIKE");

    private final String value;

    Expression(String value) {
        this.value = value;
    }
    
    public String getValue(){
        return this.value;
    }
}
