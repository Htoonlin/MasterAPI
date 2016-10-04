/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import com.sdm.core.Globalizer;
import com.sdm.core.util.mail.MailInfo;
import com.sdm.core.util.mail.MailgunService;
import com.sdm.core.response.MessageResponse;
import com.sdm.core.resource.DefaultResource;
import com.sdm.core.response.ResponseType;
import com.sdm.core.Setting;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.ErrorResponse;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.util.SecurityInstance;
import com.sdm.master.dao.TokenDAO;
import com.sdm.master.dao.UserDAO;
import com.sdm.master.entity.TokenEntity;
import com.sdm.master.entity.UserEntity;
import com.sdm.master.request.auth.ActivateRequest;
import com.sdm.master.request.auth.AuthRequest;
import com.sdm.master.request.auth.ChangePasswordRequest;
import com.sdm.master.request.auth.ForgetPasswordRequest;
import com.sdm.master.request.auth.RegistrationRequest;
import eu.bitwalker.useragentutils.UserAgent;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ws.rs.core.*;
import javax.ws.rs.*;
import org.apache.log4j.Logger;

/**
 * REST Web Service
 *
 * @author Htoonlin
 */
@Path("auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource extends DefaultResource {
    private static final Logger logger = Logger.getLogger(AuthResource.class.getName());
    private final int OTP_LIFE = 15;
    private UserDAO userDao;
    
    @PostConstruct
    public void onLoad() {
        userDao = new UserDAO(getHttpSession());
    }

    @HeaderParam("user-agent")
    private String userAgentString;

    private String getDeviceOS() {
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
        return userAgent.getOperatingSystem().getName();
    }

    private void sendNewPassword(UserEntity user) throws Exception {
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

    private void sendToken(UserEntity user, int mins, String type) throws Exception {
        user.setOtpToken(Globalizer.generateToken(UserEntity.TOKEN_LENGTH));
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, mins);
        user.setOtpExpired(cal.getTime());

        StringBuilder mailContent = new StringBuilder();
        mailContent.append("<p>Dear " + user.getDisplayName() + ", <br />")
                .append("Thank you for your " + type + ". <br/>")
                .append("Your OTP is : <h3>")
                .append(user.getOtpToken())
                .append("</h3>")
                .append("It will be expired in next <b>")
                .append(mins - 5)
                .append("</b> minutes. <br/></p>")
                .append("<code>\"Never give up to be a warrior.\"</code>");
        MailInfo info = new MailInfo(Setting.getInstance().MAILGUN_DEF_MAIL_SENDER,
                user.getEmail(), "OTP code to verify.",
                mailContent.toString());
        MailgunService.getInstance().sendHTML(info);
    }

    private IBaseResponse authProcess(AuthRequest request, boolean cleanToken) throws Exception {
        try {
            if (!request.isValid()) {
                return new ErrorResponse(request.getErrors());
            }

            MessageResponse message = new MessageResponse(401, ResponseType.ERROR, "USER_AUTH_FAILED",
                    "Opp! Request email or password is something wrong");
            UserEntity authUser = userDao.userAuth(request.getEmail(), request.getCryptPassword());
            if (authUser == null) {
                return new DefaultResponse(message);
            }

            if (authUser.getStatus() == UserEntity.PENDING) {
                message.setTitle("USER_PENDING");
                message.setMessage("You need to activate your account first.");
            }
            if (request.isAuth(authUser)) {
                if (authUser.getStatus() == UserEntity.ACTIVE) {
                    userDao.beginTransaction();
                    TokenDAO tokenDAO = new TokenDAO(userDao.getSession(), getHttpSession());
                    if (cleanToken) {
                        tokenDAO.cleanToken((int)authUser.getId());
                    }
                    TokenEntity authToken = tokenDAO.generateToken((int)authUser.getId(), request.getDeviceId(), this.getDeviceOS());
                    authUser.setCurrentToken(authToken);
                    userDao.commitTransaction();
                    return new DefaultResponse(authUser);
                }
            }
            return new DefaultResponse(message);
        } catch (Exception e) {
            userDao.rollbackTransaction();
            logger.error(e);
            throw e;
        }
    }

    @POST
    @Path("clean")
    public IBaseResponse cleanToken(AuthRequest request) throws Exception {
        return this.authProcess(request, true);
    }

    @PermitAll
    @POST
    public IBaseResponse authWithEmail(AuthRequest request, @HeaderParam("user-agent") String userAgent) throws Exception {
        return this.authProcess(request, false);
    }

    @PermitAll
    @POST
    @Path("register")
    public IBaseResponse userRegistration(RegistrationRequest request) throws Exception {
        try {
            Map<String, String> errors = new HashMap<>();
            if (!request.isValid()) {
                errors = request.getErrors();
                return new ErrorResponse(errors);
            }

            if (!MailgunService.getInstance().checkMail(request.getEmail())) {
                errors.put("email", "Requested email is not valid");
                return new ErrorResponse(errors);
            }

            UserEntity user = userDao.getUserByEmail(request.getEmail());
            if (user != null && user.getEmail().equalsIgnoreCase(request.getEmail())) {
                errors.put("email", "Sorry! someone already registered with this email");
                return new ErrorResponse(errors);
            }
            String password = SecurityInstance.md5String(request.getEmail(), request.getPassword());
            user = new UserEntity(request.getEmail(),
                    request.getDisplayName(), password, true,
                    request.getCountry(), UserEntity.PENDING);
            this.sendToken(user, this.OTP_LIFE, "registration.");
            userDao.insert(user, true);
            MessageResponse response = new MessageResponse(200, ResponseType.SUCCESS,
                    "REGISTRATION_SUCCESS", "Thank you for your registration. Pls check your mail for activation.");
            return new DefaultResponse(response);
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

    @PermitAll
    @Path("activate")
    @POST
    public IBaseResponse otpActivation(ActivateRequest request) throws Exception {
        try {
            if (!request.isValid()) {
                return new ErrorResponse(request.getErrors());
            }

            UserEntity user = userDao.checkToken(request.getEmail(), request.getToken());
            if (user == null) {
                return new DefaultResponse(new MessageResponse(204, ResponseType.WARNING, 
                        "NO_DATA", "There is no data for your request."));
            }
            userDao.beginTransaction();
            if (user.getOtpExpired().before(new Date())) {
                this.sendToken(user, OTP_LIFE, " activate. It is new token for your registration.");
                userDao.update(user, false);
                MessageResponse message = new MessageResponse(400, ResponseType.WARNING,
                        "TOKEN_EXPIRE", "Sorry! Your token has expired. We send new token to your email.");
                return new DefaultResponse(message);
            }

            user.setOtpToken(null);
            user.setOtpExpired(null);
            user.setStatus(UserEntity.ACTIVE);            
            userDao.update(user, false);
            TokenDAO tokenDAO = new TokenDAO(userDao.getSession(), getHttpSession());
            TokenEntity authToken = tokenDAO.generateToken((int)user.getId(), request.getDeviceId(), this.getDeviceOS());
            user.setCurrentToken(authToken);
            userDao.commitTransaction();
            return new DefaultResponse(user);
        } catch (Exception e) {         
            userDao.rollbackTransaction();
            logger.error(e);
            throw e;
        }
    }

    @Path("changePassword")
    @POST
    public IBaseResponse changePassword(ChangePasswordRequest request) throws Exception {
        try {
            Map<String, String> errors = new HashMap<>();
            if (!request.isValid()) {
                errors = request.getErrors();
                return new ErrorResponse(errors);
            }

            String oldPassword = SecurityInstance.md5String(request.getEmail(), request.getOldPassword());
            UserEntity user = userDao.userAuth(request.getEmail(), oldPassword);

            if (user == null || ((int)user.getId() != (int)getUserId())) {
                return new DefaultResponse(new MessageResponse(204, ResponseType.WARNING, 
                        "NO_DATA", "There is no data for your request."));
            }

            if (!(user.getEmail().equalsIgnoreCase(request.getEmail())
                    && user.getPassword().equals(oldPassword))) {
                MessageResponse message = new MessageResponse(400, ResponseType.WARNING,
                        "INVALID_OLD_PASSWORD", "Hey! your old password is wrong. pls try again.");
                return new DefaultResponse(message);
            }
            String newPassword = SecurityInstance.md5String(request.getEmail(), request.getNewPassword());
            user.setPassword(newPassword);
            user.setVersion(user.getVersion() + 1);
            userDao.update(user, true);
            MessageResponse message = new MessageResponse(202, ResponseType.SUCCESS,
                    "UPDATED", "We updated the new password on your request successfully.");
            return new DefaultResponse(message);
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

    /**
     * Has Security Error on Forget Password 
     * Step 1: Request Forget Password with new Device 
     * Step 2: Use token and Auth/Activate 
     * Step 3: It will generate Token for new Device Step 4: Use generated
     * token for Forgot Password Step 5: Hacked!
     * @param request
     */
    @PermitAll
    @Path("forgetPassword")
    @POST
    public IBaseResponse forgetPassword(ForgetPasswordRequest request) throws Exception {
        try {
            Map<String, String> errors = new HashMap<>();
            if (!request.isValid()) {
                errors = request.getErrors();
                return new ErrorResponse(errors);
            }

            if (!MailgunService.getInstance().checkMail(request.getEmail())) {
                errors.put("email", "Requested email is not valid");
                return new ErrorResponse(errors);
            }

            UserEntity user = userDao.getUserByEmail(request.getEmail());
            if (user == null) {
                return new DefaultResponse(new MessageResponse(204, ResponseType.WARNING, 
                        "NO_DATA", "There is no data for your request."));
            }
            MessageResponse message = new MessageResponse(200, ResponseType.SUCCESS,
                    "PASSWORD_GENERATED", "Hi, We generated new password for your account. Pls check your email for generated password.");
            TokenDAO tokenDao = new TokenDAO(userDao.getSession(), getHttpSession());
            TokenEntity tokenInfo = tokenDao.getTokenByUserInfo((int)user.getId(), request.getDeviceId(), this.getDeviceOS());
            if (tokenInfo != null && tokenInfo.getToken().length() == 36) {
                this.sendNewPassword(user);
            } else {
                this.sendToken(user, this.OTP_LIFE, " password request.");
                message.setTitle("PASSWORD_TOKEN_GENERATED");
                message.setMessage("Hi, We send token to request for new password. Pls check your email for token.");
            }
            userDao.update(user, true);
            return new DefaultResponse(message);
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

    @PermitAll
    @Path("generatePassword")
    @POST
    public IBaseResponse generatePassword(ActivateRequest request) throws Exception {
        try {
            if (!request.isValid()) {
                return new ErrorResponse(request.getErrors());
            }

            UserEntity user = userDao.checkToken(request.getEmail(), request.getToken());
            if (user == null) {
                return new DefaultResponse(new MessageResponse(204, ResponseType.WARNING,
                        "NO_DATA", "There is no data for your request."));
            }
            if (user.getOtpExpired().before(new Date())) {
                this.sendToken(user, OTP_LIFE, " activate. It is new token for your registration.");
                userDao.update(user, true);
                MessageResponse message = new MessageResponse(400, ResponseType.WARNING,
                        "TOKEN_EXPIRE", "Sorry! Your token has expired. We send new token to your email.");
                return new DefaultResponse(message);
            }
            this.sendNewPassword(user);
            user.setOtpToken(null);
            user.setOtpExpired(null);
            userDao.update(user, true);            
            MessageResponse message = new MessageResponse(200, ResponseType.SUCCESS,
                    "PASSWORD_GENERATED", "Hi, We generated new password for your account. Pls check your email for generated password.");
            return new DefaultResponse(message);
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }
}
