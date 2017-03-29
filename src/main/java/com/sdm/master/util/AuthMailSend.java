/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.util;

import com.sdm.core.Globalizer;
import com.sdm.core.Setting;
import com.sdm.core.util.ITemplateManager;
import com.sdm.core.util.SecurityInstance;
import com.sdm.core.util.mail.GmailService;
import com.sdm.core.util.mail.MailInfo;
import com.sdm.core.util.mail.MailgunService;
import com.sdm.master.entity.UserEntity;
import com.sdm.master.request.auth.ActivateRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Htoonlin
 */
public class AuthMailSend {

    private final ITemplateManager manager;

    public AuthMailSend(ITemplateManager manager) {
        this.manager = manager;
    }

    private UserEntity setToken(UserEntity user) {
        user.setOtpToken(Globalizer.generateToken(UserEntity.TOKEN_LENGTH));
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, Setting.getInstance().OTP_LIFE);
        user.setOtpExpired(cal.getTime());
        return user;
    }

    private ActivateRequest buildRequest(UserEntity user, String deviceId) {
        //Create Activate Request
        ActivateRequest request = new ActivateRequest();
        request.setToken(user.getOtpToken());
        request.setEmail(user.getEmail());
        request.setDeviceId(deviceId);
        return request;
    }

    public void forgetPasswordLink(UserEntity user) throws Exception {
        user = setToken(user);
        ActivateRequest request = buildRequest(user, user.getPassword());

        //Build mail with Forget Password Link        
        Map<String, Object> data = new HashMap<>();
        data.put("expire", Setting.getInstance().OTP_LIFE);
        data.put("user", user.getDisplayName());
        data.put("token", SecurityInstance.base64Encode(Globalizer.jsonMapper().writeValueAsString(request)));
        data.put("current_year", Globalizer.getDateString("yyyy", new Date()));
        String mailBody = manager.buildTemplate("mail/forget-password.jsp", data);
        MailInfo info = new MailInfo(
                user.getEmail(), "Forget password response", mailBody);
        if (Setting.getInstance().MAIL_SERVICE.equalsIgnoreCase("mailgun")) {
            MailgunService.getInstance().sendHTML(info);
        } else {
            GmailService.getInstance().sendHTML(info);
        }
    }

    public void activateLink(UserEntity user, String deviceId) throws Exception {
        user = setToken(user);
        ActivateRequest request = buildRequest(user, deviceId);

        //Build mail with activation link        
        Map<String, Object> data = new HashMap<>();
        data.put("expire", Setting.getInstance().OTP_LIFE);
        data.put("user", user.getDisplayName());
        data.put("token", SecurityInstance.base64Encode(Globalizer.jsonMapper().writeValueAsString(request)));
        data.put("current_year", Globalizer.getDateString("yyyy", new Date()));
        String mailBody = manager.buildTemplate("mail/auth-activate.jsp", data);
        MailInfo info = new MailInfo(user.getEmail(), "Activate your account on SUNDEW MASTER API.", mailBody);
        if (Setting.getInstance().MAIL_SERVICE.equalsIgnoreCase("mailgun")) {
            MailgunService.getInstance().sendHTML(info);
        } else {
            GmailService.getInstance().sendHTML(info);
        }
    }

    public void welcomeUser(UserEntity user, String rawPassword) throws Exception {

        Map<String, Object> data = new HashMap<>();
        data.put("email", user.getEmail());
        data.put("name", user.getDisplayName());
        data.put("password", rawPassword);
        data.put("current_year", Globalizer.getDateString("yyyy", new Date()));
        String mailBody = manager.buildTemplate("mail/create-user.jsp", data);

        MailInfo info = new MailInfo(user.getEmail(), "Welcome New User!", mailBody);
        if (Setting.getInstance().MAIL_SERVICE.equalsIgnoreCase("mailgun")) {
            MailgunService.getInstance().sendHTML(info);
        } else {
            GmailService.getInstance().sendHTML(info);
        }
    }

    /*
    public void otpToken(UserEntity user, String type) throws Exception {
        user = this.setToken(user);
        StringBuilder mailContent = new StringBuilder();
        mailContent.append("<p>Dear " + user.getDisplayName() + ", <br />")
                .append("Thank you for your " + type + ". <br/>")
                .append("Your OTP is : <h3>")
                .append(user.getOtpToken())
                .append("</h3>")
                .append("It will be expired in next <b>")
                .append(Setting.getInstance().OTP_LIFE)
                .append("</b> minutes. <br/></p>")
                .append("<code>\"Never give up to be a warrior.\"</code>");
        MailInfo info = new MailInfo(Setting.getInstance().MAILGUN_DEF_MAIL_SENDER,
                user.getEmail(), "OTP code to verify.",
                mailContent.toString());
        MailgunService.getInstance().sendHTML(info);
    }
    
    public void newPassword(UserEntity user) throws Exception {
        String newPassword = user.getPassword().substring(0, 12);
        String encryptPassword = SecurityInstance.md5String(user.getEmail(), newPassword);
        user.setPassword(encryptPassword);
        StringBuilder mailContent = new StringBuilder();
        mailContent.append("<p>Dear " + user.getDisplayName() + ", <br/>")
                .append("We generated new password for your request. Try to use this passsword for account login.")
                .append("Your New Password is : <h3>")
                .append(newPassword)
                .append("</h3>")
                .append("<code>\"Never give up to be a warrior.\"</code>");

        MailInfo info = new MailInfo(Setting.getInstance().MAILGUN_DEF_MAIL_SENDER,
                user.getEmail(), "New generated password for your request.",
                mailContent.toString());
        MailgunService.getInstance().sendHTML(info);
    } */
}
