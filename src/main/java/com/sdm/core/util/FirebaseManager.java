/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.ApsAlert;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.sdm.core.Setting;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author htoonlin
 */
public class FirebaseManager {

    private static final Logger LOG = Logger.getLogger(FirebaseManager.class.getName());

    private static FirebaseManager instance;

    public FirebaseManager() {
        String serviceJsonFile = Setting.getInstance().get(Setting.FIREBASE_SERVICE_ACCOUNT_PATH);
        String projectURL = Setting.getInstance().get(Setting.FIREBASE_URL);

        try (FileInputStream serviceAccount = new FileInputStream(serviceJsonFile)) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(projectURL)
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (FileNotFoundException ex) {
            LOG.error(ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FirebaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static synchronized FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }
        return instance;
    }

    private AndroidConfig androidNotification(String title, String body) {
        return AndroidConfig.builder()
                .setTtl(3600 * 1000) // 1 hour in milliseconds
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .setSound("default")
                        .build())
                .build();
    }

    private ApnsConfig iosNotification(String title, String body, int badgeCount) {
        return ApnsConfig.builder()
                .putHeader("apns-priority", "10")
                .setAps(Aps.builder()
                        .setAlert(ApsAlert.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build())
                        .setBadge(badgeCount)
                        .build())
                .build();
    }

    public ApiFuture<String> sendMessage(String token, String title, String body, int badgeCount,
            Map<String, String> data) {
        Message message = Message.builder()
                .setToken(token)
                .setNotification(new Notification(title, body))
                .setApnsConfig(this.iosNotification(title, body, badgeCount))
                .setAndroidConfig(this.androidNotification(title, body))
                .putAllData(data)
                .build();

        return FirebaseMessaging.getInstance().sendAsync(message);
    }

}
