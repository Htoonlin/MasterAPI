/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util;

/**
 *
 * @author Htoonlin
 */
public class MySQLManager {

    private static final String QUOTE = "`";

    public static String cleanLastChar(String query, String lastChar) {
        if (query.endsWith(lastChar)) {
            query = query.substring(0, query.length() - lastChar.length());
        }

        return query;
    }

    public static String escapeQuery(String query) {
        String safeQuery = query;
        if (query.contains("'")) {
            safeQuery = query.replaceAll("'", "''");
        }
        return safeQuery;
    }

    public static String quoteName(String input) {
        if (!input.startsWith(QUOTE)) {
            input = QUOTE + input;
        }

        if (!input.endsWith(QUOTE)) {
            input += QUOTE;
        }

        return input;
    }
}
