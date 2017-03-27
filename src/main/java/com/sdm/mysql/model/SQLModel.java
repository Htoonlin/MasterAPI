/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.mysql.model;

import com.sdm.mysql.util.MySQLManager;

/**
 *
 * @author Htoonlin
 */
public abstract class SQLModel {

    public abstract String defaultSQL();

    protected String cleanLastChar(String query, String lastChar) {
        return MySQLManager.cleanLastChar(query, lastChar);
    }

    protected String escapeQuery(String query) {
        return MySQLManager.escapeQuery(query);
    }

    protected String quoteName(String input) {
        return MySQLManager.quoteName(input);
    }
}
