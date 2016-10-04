/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.database;

import org.hibernate.dialect.MySQLInnoDBDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

/**
 *
 * @author Htoonlin
 */
public class SundewMySQLDialect extends MySQLInnoDBDialect {

    public SundewMySQLDialect() {
        super();
        registerFunction("regexp", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "?1 REGEXP ?2"));        
    }
    
}
