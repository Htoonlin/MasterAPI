/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.hibernate;

import org.hibernate.dialect.InnoDBStorageEngine;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.MySQLStorageEngine;

/**
 *
 * @author Htoonlin
 */
public class SundewMySQLDialect extends MySQLDialect {

    public SundewMySQLDialect() {
        super();
    }

    @Override
    protected MySQLStorageEngine getDefaultMySQLStorageEngine() {
        return InnoDBStorageEngine.INSTANCE;
    }

    @Override
    protected String getEngineKeyword() {
        //Default is type. It is invalid code in mysql.
        return "ENGINE";
    }

    @Override
    public String[] getCreateCatalogCommand(String catalogName) {
        //Default create database
        String query = "CREATE DATABASE " + catalogName;
        //Add charset to support emoji and other extra unicode text.
        query += " DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci";
        return new String[] { query };
    }
}
