/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;

/**
 *
 * @author Htoonlin
 */
public final class Setting {

    /* System Setting */
    public String ENVIRONMENT;

    /* Storage Settings */
    public String STORAGE_PATH;
    public String BASE_PATH;
    public String TEMPLATE_PATH;

    /* Security Setting */
    public int ROOT_ID;
    public int AUTH_TOKEN_LIFE;
    public int OTP_LIFE;
    public int SECURITY_TIMESTAMP_LIFE;
    public String AES_KEY;
    public String ENCRYPT_SALT;
    public String TOKEN_CHAR;
    public int AUTH_FAILED_COUNT;

    /* Date Time Setting */
    public SimpleDateFormat DATE_TIME_FORMAT;
    public SimpleDateFormat DATE_FORMAT;
    public SimpleDateFormat TIME_FORMAT;

    /* CORS Setting */
    public String ACCESS_CONTROL_ALLOW_ORIGIN;
    public String ACCESS_CONTROL_ALLOW_METHOD;
    public String ACCESS_CONTROL_ALLOW_HEADERS;
    public String ACCESS_CONTROL_MAX_AGE;
    
    /* Mail Setting */
    public String MAIL_SERVICE;

    private final Properties settingProps;

    private static Setting instance;

    public static synchronized Setting getInstance() {
        if (instance == null) {
            instance = new Setting();
        }
        return instance;
    }

    public Properties getProperties() {
        return this.settingProps;
    }

    public Setting() {
        settingProps = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("setting.properties");
        if (inputStream != null) {
            try {
                settingProps.load(inputStream);
                ENVIRONMENT = settingProps.getProperty("SYSTEM_ENVIRONMENT", "BETA");
                STORAGE_PATH = settingProps.getProperty("STORAGE_PATH", "");
                BASE_PATH = settingProps.getProperty("BASE_PATH", "http://localhost:8080/MasterAPI");
                TEMPLATE_PATH = settingProps.getProperty("TEMPLATE_PATH", "/WEB-INF/");

                DATE_TIME_FORMAT = new SimpleDateFormat(settingProps.getProperty("DATE_TIME_FORMAT", "yyyy-MM-dd HH:mm:ss"));
                DATE_FORMAT = new SimpleDateFormat(settingProps.getProperty("DATE_FORMAT", "yyyy-MM-dd"));
                TIME_FORMAT = new SimpleDateFormat(settingProps.getProperty("TIME_FORMAT", "HH:mm:ss"));

                ROOT_ID = Integer.parseInt(settingProps.getProperty("ROOT_ID", "1"));
                AUTH_TOKEN_LIFE = Integer.parseInt(settingProps.getProperty("AUTH_TOKEN_LIFE", "7"));
                OTP_LIFE = Integer.parseInt(settingProps.getProperty("OTP_LIFE", "10"));
                SECURITY_TIMESTAMP_LIFE = Integer.parseInt(settingProps.getProperty("SECURITY_TIMESTAMP_LIFE", "5"));
                AES_KEY = settingProps.getProperty("AES_KEY", "aL8AEKV6SSyEaZhbuPXhfIDX550rwjvEj25E3oLxHro=");
                ENCRYPT_SALT = settingProps.getProperty("ENCRYPT_SALT", "s4qYAxH0SqiHAwrgwWPSI6DVm");
                TOKEN_CHAR = settingProps.getProperty("TOKEN_CHAR", "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
                AUTH_FAILED_COUNT = Integer.parseInt(settingProps.getProperty("AUTH_FAILED_COUNT", "3"));

                ACCESS_CONTROL_ALLOW_ORIGIN = settingProps.getProperty("ACCESS_CONTROL_ALLOW_ORIGIN", "*");
                ACCESS_CONTROL_ALLOW_METHOD = settingProps.getProperty("ACCESS_CONTROL_ALLOW_METHOD", "GET, POST, DELETE, PUT, OPTIONS");
                ACCESS_CONTROL_ALLOW_HEADERS = settingProps.getProperty("ACCESS_CONTROL_ALLOW_HEADERS", "authorization, content-type, xsrf-token");
                ACCESS_CONTROL_MAX_AGE = settingProps.getProperty("ACCESS_CONTROL_MAX_AGE", "0");
                
                MAIL_SERVICE = settingProps.getProperty("MAIL_SERVICE", "gmail");
            } catch (IOException ex) {
                Logger.getLogger(Setting.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
