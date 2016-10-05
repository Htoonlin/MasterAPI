/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.util;

import com.sdm.core.Globalizer;
import com.sdm.core.Setting;
import com.sdm.core.util.SecurityInstance;
import com.sdm.core.util.mail.MailInfo;
import com.sdm.core.util.mail.MailgunService;
import com.sdm.master.entity.UserEntity;
import com.sdm.master.request.auth.ActivateRequest;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Htoonlin
 */
public class AuthMailSend {
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
    }

    public void activateLink(UserEntity user, String deviceId, String baseURI) throws Exception {
        user.setOtpToken(Globalizer.generateToken(UserEntity.TOKEN_LENGTH));
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, Setting.getInstance().OTP_LIFE);
        user.setOtpExpired(cal.getTime());
       
        //Create Activate Request
        ActivateRequest request = new ActivateRequest();
        request.setToken(user.getOtpToken());
        request.setEmail(user.getEmail());     
        request.setDeviceId(deviceId);
        
        //Build mail with activation link
        String links = baseURI + "auth/activate/?token=";
        links += SecurityInstance.base64Encode(Globalizer.jsonMapper().writeValueAsString(request));
        StringBuilder mailContent = new StringBuilder();
        mailContent.append("<p> Thanks for your registration!")
                .append("Please follow this link to activate your account: <br />")
                .append("<a href='" + links + "'>" + links + "</a></p>")
                .append("It will be expired in next <b>")
                .append(Setting.getInstance().OTP_LIFE)
                .append("</b> minutes. <br/></p>")
                .append("<code>\"Never give up to be a warrior.\"</code>");
        MailInfo info = new MailInfo(Setting.getInstance().MAILGUN_DEF_MAIL_SENDER,
                user.getEmail(), "Activate your account on SUNDEW MASTER API.",
                mailContent.toString());
        MailgunService.getInstance().sendHTML(info);
    }

    public void otpToken(UserEntity user, String type) throws Exception {
        user.setOtpToken(Globalizer.generateToken(UserEntity.TOKEN_LENGTH));
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, Setting.getInstance().OTP_LIFE);
        user.setOtpExpired(cal.getTime());

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
}
