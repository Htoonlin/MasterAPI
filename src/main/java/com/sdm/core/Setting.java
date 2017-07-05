/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author Htoonlin
 */
public final class Setting {

    private static final Logger LOG = Logger.getLogger(Setting.class.getName());
    
    /* System Setting */
    public String ENVIRONMENT;

    /* Storage Settings */
    public String STORAGE_PATH;
    public String TEMPLATE_PATH;

    /* Security Setting */
    public int ROOT_ID;
    public int AUTH_TOKEN_LIFE;
    public String JWT_KEY;
    public int OTP_LIFE;
    public int SECURITY_TIMESTAMP_LIFE;
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
    public String MAIL_HOST;
    public String MAIL_PORT;
    public String MAIL_NEED_AUTH;
    public String MAIL_USER;
    public String MAIL_PASSWORD;

    private static final String FILE_NAME = "setting.properties";

    private final Properties settingProps;

    private static Setting instance;

    public static synchronized Setting getInstance() {
        if (instance == null) {
            instance = new Setting();
        }
        return instance;
    }

    public void reload() {
        instance = new Setting();
    }

    public void changeSetting(String key, String value) {
        this.settingProps.setProperty(key, value);
    }

    public void changeSettings(Map<String, String> settings) {
        for (Map.Entry<String, String> settingEntry : settings.entrySet()) {
            String key = settingEntry.getKey();
            String value = settingEntry.getValue();
            this.changeSetting(key, value);
        }
    }

    public void save() {
        String propFile = getClass().getClassLoader().getResource(FILE_NAME).getFile();
        String comments = Globalizer.getDateString("yyyy-MM-dd HH:mm:ss", new Date());

        try (OutputStream outputStream = new FileOutputStream(propFile)) {
            this.settingProps.store(outputStream, comments);
        } catch (IOException ex) {
            LOG.error(ex);
        }

    }

    public Properties getProperties() {
        return this.settingProps;
    }

    public Setting() {
        settingProps = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(FILE_NAME);
        if (inputStream != null) {
            try {
                settingProps.load(inputStream);
                ENVIRONMENT = settingProps.getProperty("SYSTEM_ENVIRONMENT", "BETA");
                STORAGE_PATH = settingProps.getProperty("STORAGE_PATH", "");
                TEMPLATE_PATH = settingProps.getProperty("TEMPLATE_PATH", "/WEB-INF/");

                DATE_TIME_FORMAT = new SimpleDateFormat(settingProps.getProperty("DATE_TIME_FORMAT", "yyyy-MM-dd HH:mm:ss"));
                DATE_FORMAT = new SimpleDateFormat(settingProps.getProperty("DATE_FORMAT", "yyyy-MM-dd"));
                TIME_FORMAT = new SimpleDateFormat(settingProps.getProperty("TIME_FORMAT", "HH:mm:ss"));

                ROOT_ID = Integer.parseInt(settingProps.getProperty("ROOT_ID", "1"));
                AUTH_TOKEN_LIFE = Integer.parseInt(settingProps.getProperty("AUTH_TOKEN_LIFE", "7"));
                JWT_KEY = settingProps.getProperty("JWT_KEY", "iMyohUD3G1BE8BXxOpg5qoPdbT1DvB2ihyNYFQm1VLPvrErmeLEtGT74GqOeQClGbSSuIcjYMh+3m+mqCucH3A==");
                OTP_LIFE = Integer.parseInt(settingProps.getProperty("OTP_LIFE", "10"));
                SECURITY_TIMESTAMP_LIFE = Integer.parseInt(settingProps.getProperty("SECURITY_TIMESTAMP_LIFE", "5"));
                ENCRYPT_SALT = settingProps.getProperty("ENCRYPT_SALT", "s4qYAxH0SqiHAwrgwWPSI6DVm");
                TOKEN_CHAR = settingProps.getProperty("TOKEN_CHAR", "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
                AUTH_FAILED_COUNT = Integer.parseInt(settingProps.getProperty("AUTH_FAILED_COUNT", "3"));

                ACCESS_CONTROL_ALLOW_ORIGIN = settingProps.getProperty("ACCESS_CONTROL_ALLOW_ORIGIN", "*");
                ACCESS_CONTROL_ALLOW_METHOD = settingProps.getProperty("ACCESS_CONTROL_ALLOW_METHOD", "GET, POST, DELETE, PUT, OPTIONS");
                ACCESS_CONTROL_ALLOW_HEADERS = settingProps.getProperty("ACCESS_CONTROL_ALLOW_HEADERS", "authorization, content-type, xsrf-token");
                ACCESS_CONTROL_MAX_AGE = settingProps.getProperty("ACCESS_CONTROL_MAX_AGE", "0");

                MAIL_SERVICE = settingProps.getProperty("MAIL_SERVICE", "webmail");
                MAIL_HOST = settingProps.getProperty("MAIL_HOST", "smtp.gmail.com");
                MAIL_PORT = settingProps.getProperty("MAIL_PORT", "465");
                MAIL_NEED_AUTH = settingProps.getProperty("MAIL_NEED_AUTH", "true");
                MAIL_USER = settingProps.getProperty("MAIL_USER", "example@gmail.com");
                MAIL_PASSWORD = settingProps.getProperty("MAIL_PASSWORD", "3x@mp13");
            } catch (IOException ex) {
                LOG.error(ex);
            }
        }
    }
}
