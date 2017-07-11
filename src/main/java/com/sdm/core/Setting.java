/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.sdm.core.util.SecurityManager;

/**
 *
 * @author Htoonlin
 */
public final class Setting implements ISetting {

	private static final Logger LOG = Logger.getLogger(Setting.class.getName());
	private static final String SETTING_FILE = "setting.properties";

	private static Setting instance;

	private final Properties settingProps;

	public Setting() {
		settingProps = new Properties();
		this.loadSetting();
	}
	
	public static synchronized Setting getInstance() {
		if(instance == null) {
			instance = new Setting();
		}
		
		return instance;
	}
	
	public Properties getProperties() {
		return this.settingProps;
	}

	public String get(String name, String defaultValue) {
		return this.settingProps.getProperty(name, defaultValue);
	}

	public int getInt(String name, String defaultValue) {
		return Integer.parseInt(this.settingProps.getProperty(name, defaultValue));
	}

	public void loadSetting() {
		File settingFile = new File(SETTING_FILE);
		if(!settingFile.exists()) {
			LOG.info("Generating setting.properties at" + settingFile.getAbsolutePath() + ".");
			defaultSetting();
			save();
		}
		
		try (InputStream inputStream = new FileInputStream(settingFile)) {
			this.settingProps.load(inputStream);
			LOG.info("Loaded setting.properties.");
		} catch (IOException e) {
			LOG.error(e);
		}
	}

	public void changeSetting(String key, String value) {
		LOG.info("Changing setting [" + key + "=>" + value + "].");
		this.settingProps.setProperty(key, value);
	}

	public void changeSettings(Map<String, String> settings) {
		LOG.info("Preparing to change setting.properties file.");
		for (Map.Entry<String, String> settingEntry : settings.entrySet()) {
			String key = settingEntry.getKey();
			String value = settingEntry.getValue();
			this.changeSetting(key, value);
		}
	}

	public void save() {		
		String comments = Globalizer.getDateString("yyyy-MM-dd HH:mm:ss", new Date());
		File settingFile = new File(SETTING_FILE);
		try (OutputStream outputStream = new FileOutputStream(settingFile)) {
			this.settingProps.store(outputStream, comments);
			LOG.info("Generated setting.properties file at [" + comments + "] in " + settingFile.getAbsolutePath() + ".");
		} catch (IOException ex) {
			LOG.error(ex);
		}

	}

	public void defaultSetting() {
		this.settingProps.setProperty(SYSTEM_ENV, "DEV");
		this.settingProps.setProperty(DATE_TIME_FORMAT, "yyyy-MM-dd HH:mm:ss");
		this.settingProps.setProperty(DATE_FORMAT, "yyyy-MM-dd");
		this.settingProps.setProperty(TIME_FORMAT, "HH:mm:ss");

		this.settingProps.setProperty(FILE_STORAGE_PATH, "/var/www/master-api/upload/");
		this.settingProps.setProperty(TEMPLATE_PATH, "/WEB-INF/template/");

		this.settingProps.setProperty(CORS_ORIGIN, "*");
		this.settingProps.setProperty(CORS_METHODS, "GET, POST, PUT, DELETE, OPTIONS");
		this.settingProps.setProperty(CORS_HEADERS, "authorization,content-type");
		this.settingProps.setProperty(CORS_MAX_AGE, "36000");

		this.settingProps.setProperty(ROOT_ID, "1");
		this.settingProps.setProperty(OTP_LIFE, "10");
		this.settingProps.setProperty(AUTH_TOKEN_LIFE, "30");
		this.settingProps.setProperty(JWT_KEY, SecurityManager.generateJWTKey());
		this.settingProps.setProperty(SECURITY_TIMESTAMP_LIFE, "5");
		this.settingProps.setProperty(ENCRYPT_SALT, SecurityManager.generateSalt(16));
		this.settingProps.setProperty(TOKEN_CHARS, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
		this.settingProps.setProperty(AUTH_FAILED_COUNT, "3");

		this.settingProps.setProperty(MAIL_TYPE, "webmail");

		this.settingProps.setProperty(MAIL_HOST, "smtp.gmail.com");
		this.settingProps.setProperty(MAIL_PORT, "465");
		this.settingProps.setProperty(MAIL_IS_AUTH, "true");
		this.settingProps.setProperty(MAIL_USER, "example@gmail.com");
		this.settingProps.setProperty(MAIL_PASSWORD, "3x@mp13");

		this.settingProps.setProperty(MAILGUN_PRI_KEY, "private-key");
		this.settingProps.setProperty(MAILGUN_PUB_KEY, "public-key");
		this.settingProps.setProperty(MAILGUN_DOMAIN, "mydomain");
		this.settingProps.setProperty(MAILGUN_DEFAULT_MAIL, "info@mydomain.com");
	}

}
