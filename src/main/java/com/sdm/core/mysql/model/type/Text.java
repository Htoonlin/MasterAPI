/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.mysql.model.type;

import java.io.Serializable;

/**
 *
 * @author Htoonlin
 */
public class Text implements PropertyType, Serializable {

    public static final String ALPHA_NUMERIC_PATTERN = "^[a-zA-Z0-9]*$";
    public static final String CHARACTER_ONLY_PATTERN = "^[a-zA-Z0-9]*$";
    public static final String DIGIT_ONLY_PATTERN = "^[0-9]*$";
    public static final String STRONG_PASSWORD_PATTERN = "^(?=^.{6,}$)((?=.*[A-Za-z0-9])(?=.*[A-Z])(?=.*[a-z]))^.*$";
    public static final String COLOR_HEX_PATTERN = "^#?([a-f0-9]{8}|[a-f0-9]{6}|[a-f0-9]{4}|[a-f0-9]{3})$";
    public static final String EMAIL_PATTERN = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$";
    public static final String URL_PATTERN = "^(((http|https|ftp):\\/\\/)?([[a-zA-Z0-9]\\-\\.])+(\\.)([[a-zA-Z0-9]]){2,4}([[a-zA-Z0-9]\\/+=%&_\\.~?\\-]*))*$";
    public static final String IP_PATTERN = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    public static final String CREDIT_PATTERN = "^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|6011[0-9]{12}|622((12[6-9]|1[3-9][0-9])|([2-8][0-9][0-9])|(9(([0-1][0-9])|(2[0-5]))))[0-9]{10}|64[4-9][0-9]{13}|65[0-9]{14}|3(?:0[0-5]|[68][0-9])[0-9]{11}|3[47][0-9]{13})*$";
    public static final String MASTER_CARD_PATTERN = "^(5[1-5][0-9]{14})*$";
    public static final String VISA_CARD_PATTERN = "^(4[0-9]{12}(?:[0-9]{3})?)*$";
    public static final String AMERICAN_EXPRESS_CARD_PATTERN = "^(3[47][0-9]{13})*$";
    public static final String UNKNOWN_CARD_PATTERN = "^(3(?:0[0-5]|[68][0-9])[0-9]{11})*$";

    private String regex;
    private int maxLength;
    private boolean fixed;
    private String name;
    private boolean uniqueId;

    public Text() {
        this.name = "text";
    }

    public Text(int maxLength, String name) {
        this.maxLength = maxLength;
        this.name = name;
    }

    public Text(String regex, int maxLength, int minLength, boolean fixed, String name) {
        this.regex = regex;
        this.maxLength = maxLength;
        this.fixed = fixed;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String defaultSQL() {
        if (this.isUniqueId()) {
            return "CHAR(36) NOT NULL UNIQUE";
        }
        String sql = fixed ? "CHAR" : "VARCHAR";
        if (maxLength <= 0) {
            maxLength = fixed ? 50 : 255;
        }
        sql += "(" + maxLength + ")";
        return sql;
    }

    @Override
    public boolean validType(Object value) {
        String strValue = value.toString().trim();
        if (strValue.length() > maxLength) {
            return false;
        }

        if (regex != null && !regex.isEmpty()) {
            return strValue.matches(regex);
        }

        return true;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public boolean isUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(boolean uniqueId) {
        this.uniqueId = uniqueId;
    }
}
