/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sdm.core.Globalizer;
import com.sdm.core.Setting;
import com.sdm.core.di.IMailManager;
import com.sdm.core.di.ITemplateManager;
import com.sdm.core.util.SecurityManager;
import com.sdm.core.util.mail.MailInfo;
import com.sdm.master.entity.UserEntity;
import com.sdm.master.request.ActivateRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Htoonlin
 */
public class AuthMailSend {

    private final ITemplateManager templateManager;
    private final IMailManager mailManager;
    private final int OTP_MINUTE;

    public AuthMailSend(IMailManager mailManager, ITemplateManager templateManager) {
        this.mailManager = mailManager;
        this.templateManager = templateManager;
        this.OTP_MINUTE = Setting.getInstance().getInt(Setting.OTP_LIFE, "10");
    }

    private UserEntity setToken(UserEntity user) {
        user.setOtpToken(Globalizer.generateToken(UserEntity.TOKEN_LENGTH));
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, this.OTP_MINUTE);
        user.setOtpExpired(cal.getTime());
        return user;
    }

    private ActivateRequest buildRequest(UserEntity user, String deviceId) {
        // Create Activate Request
        ActivateRequest request = new ActivateRequest();
        request.setToken(user.getOtpToken());
        request.setEmail(user.getEmail());
        request.setDeviceId(deviceId);
        return request;
    }

    public void forgetPasswordLink(UserEntity user) throws JsonProcessingException {
        user = setToken(user);
        ActivateRequest request = buildRequest(user, user.getPassword());

        // Build mail with Forget Password Link
        Map<String, Object> data = new HashMap<>();
        data.put("expire", this.OTP_MINUTE);
        data.put("user", user.getDisplayName());
        data.put("token", SecurityManager.base64Encode(Globalizer.jsonMapper().writeValueAsString(request)));
        data.put("current_year", Globalizer.getDateString("yyyy", new Date()));
        String mailBody = templateManager.buildTemplate("mail/forget-password.jsp", data);
        MailInfo info = new MailInfo(user.getEmail(), "Forget password response", mailBody);
        mailManager.sendHTML(info);
    }

    public void activateLink(UserEntity user, String deviceId) throws JsonProcessingException {
        user = setToken(user);
        ActivateRequest request = buildRequest(user, deviceId);

        // Build mail with activation link
        Map<String, Object> data = new HashMap<>();
        data.put("expire", this.OTP_MINUTE);
        data.put("user", user.getDisplayName());
        data.put("token", SecurityManager.base64Encode(Globalizer.jsonMapper().writeValueAsString(request)));
        data.put("current_year", Globalizer.getDateString("yyyy", new Date()));
        String mailBody = templateManager.buildTemplate("mail/auth-activate.jsp", data);
        MailInfo info = new MailInfo(user.getEmail(), "Activate your account on SUNDEW MASTER API.", mailBody);
        mailManager.sendHTML(info);
    }

    public void welcomeUser(UserEntity user, String rawPassword) {

        Map<String, Object> data = new HashMap<>();
        data.put("email", user.getEmail());
        data.put("name", user.getDisplayName());
        data.put("password", rawPassword);
        data.put("current_year", Globalizer.getDateString("yyyy", new Date()));
        String mailBody = templateManager.buildTemplate("mail/create-user.jsp", data);

        MailInfo info = new MailInfo(user.getEmail(), "Welcome New User!", mailBody);
        mailManager.sendHTML(info);
    }
}
