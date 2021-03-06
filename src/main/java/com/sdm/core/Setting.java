/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core;

import com.sdm.Constants;
import com.sdm.core.util.SecurityManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author Htoonlin
 */
public final class Setting implements Constants.Setting {

    private static final Logger LOG = Logger.getLogger(Setting.class.getName());

    private static final String SETTING_FILE = "setting.properties";

    private File settingFile;

    private static Setting instance;

    private final Properties settingProps;

    public Setting() {
        settingProps = new Properties();
    }

    public static synchronized Setting getInstance() {
        if (instance == null) {
            instance = new Setting();
        }
        return instance;
    }

    public HashMap<String, Object> getProperties() {
        HashMap<String, Object> props = new HashMap<>();

        for (String key : this.settingProps.stringPropertyNames()) {
            if (key.startsWith("com.sdm.path")) {
                continue;
            }
            props.put(key.toLowerCase(), this.get(key));
        }
        return props;
    }

    public String get(String name) {
        return this.get(name, "");
    }

    public String get(String name, String defaultValue) {
        return this.settingProps.getProperty(name, defaultValue);
    }
    
    public int getInt(String name) {
        return this.getInt(name, "0");
    }

    public int getInt(String name, String defaultValue) {
        return Integer.parseInt(this.settingProps.getProperty(name, defaultValue));
    }

    public void loadSetting() {
        try (InputStream inputStream = new FileInputStream(settingFile)) {
            this.settingProps.load(inputStream);
            LOG.info("Loaded properties => " + settingFile.getAbsolutePath() + ".");
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void changeSetting(String key, String value) {
        if (key.startsWith("com.sdm.path")) {
            return;
        }
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
        String comments = "Modified this properties file: ";
        comments += Globalizer.getDateString("yyyy-MM-dd HH:mm:ss", new Date());
        try (OutputStream outputStream = new FileOutputStream(this.settingFile)) {
            this.settingProps.store(outputStream, comments);
            LOG.info("Generated setting.properties file at [" + comments + "] => " + settingFile.getAbsolutePath()
                    + ".");
        } catch (IOException ex) {
            LOG.error(ex);
        }

    }
    
    private void createUploadDir(){
        String uploadDir = this.settingProps.getProperty(UPLOAD_DIRECTORY, "");
        if(uploadDir == null || uploadDir.isEmpty()){
            uploadDir = this.settingFile.getParent() + "/upload/";
            this.settingProps.setProperty(UPLOAD_DIRECTORY, uploadDir);
        }
        //If upload directory is not exists, create directories.
        File uploadDirPath = new File(uploadDir);
        if(!uploadDirPath.exists()){
            uploadDirPath.mkdirs();
        }
    }
    
    private void copyTemplates(){
        try {
            String templateDir = this.settingProps.getProperty(TEMPLATE_DIRECTORY, "");
            if(templateDir == null || templateDir.isEmpty()){
                templateDir = this.settingFile.getParent() + "/template/";
                this.settingProps.setProperty(TEMPLATE_DIRECTORY, templateDir);
            }
            String resourceURL = getClass().getClassLoader().getResource("template").getPath();
            File templatePath = new File(URLDecoder.decode(resourceURL, "UTF-8"));
            Globalizer.copyFileOrDirectory(templatePath, new File(templateDir));
            LOG.info("Copied all templates => " + templatePath);
        } catch (IOException ex) {
            LOG.error(ex);
        }
    }
    
    private void copyFirebase(){
        try {
            String firebaseServiceAccountPath = this.settingProps.getProperty(FIREBASE_SERVICE_ACCOUNT_PATH, "");
            if(firebaseServiceAccountPath == null || firebaseServiceAccountPath.isEmpty()){
                firebaseServiceAccountPath = this.settingFile.getParent() + "/firebase.json";
                this.settingProps.setProperty(FIREBASE_SERVICE_ACCOUNT_PATH, firebaseServiceAccountPath);
            }
            String resourceURL = getClass().getClassLoader().getResource("firebase.json").getPath();
            File jsonFile = new File(URLDecoder.decode(resourceURL, "UTF-8"));
            Globalizer.copyFileOrDirectory(jsonFile, new File(firebaseServiceAccountPath));
            LOG.info("Copied firebase.json => " + jsonFile);
        } catch (IOException ex) {
            LOG.error(ex);
        }
    }

    public void init() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(SETTING_FILE);
        if (inputStream != null) {
            try {
                LOG.info("Loading default properites ....");
                this.settingProps.load(inputStream);
                inputStream.close();
                LOG.info("Loaded default properites.");
            } catch (IOException ex) {
                LOG.error(ex);
            }
        }

        this.settingFile = new File(this.settingProps.getProperty(ROOT_DIRECTORY, "/var/www/") + SETTING_FILE);
        if (this.settingFile.exists()) {
            this.loadSetting();
        } else {
            LOG.info("Createing upload dir....");
            this.createUploadDir();
            
            LOG.info("Copying templates....");
            this.copyTemplates();
            
            LOG.info("Copying firebase.json ....");
            this.copyFirebase();
            
            LOG.info("Generating system properites ....");
            this.settingProps.setProperty(JWT_KEY, SecurityManager.generateJWTKey());
            this.settingProps.setProperty(ENCRYPT_SALT, SecurityManager.generateSalt());
            this.settingProps.setProperty(FB_MESSENGER_TOKEN, Globalizer.generateToken(64));
            LOG.info("Generated system properites.");
            this.save();
        }
        LOG.info("Setup new properties file => " + settingFile.getAbsolutePath() + ".");
    }

}
