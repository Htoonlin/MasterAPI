/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import com.sdm.core.Globalizer;
import com.sdm.core.util.mail.MailgunService;
import com.sdm.core.response.MessageResponse;
import com.sdm.core.resource.DefaultResource;
import com.sdm.core.response.ResponseType;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.ErrorResponse;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.util.SecurityInstance;
import com.sdm.core.util.TemplateManager;
import com.sdm.master.dao.TokenDAO;
import com.sdm.master.dao.UserDAO;
import com.sdm.master.entity.TokenEntity;
import com.sdm.master.entity.UserEntity;
import com.sdm.master.request.auth.ActivateRequest;
import com.sdm.master.request.auth.AuthRequest;
import com.sdm.master.request.auth.ChangePasswordRequest;
import com.sdm.master.request.auth.RegistrationRequest;
import com.sdm.master.util.AuthMailSend;
import eu.bitwalker.useragentutils.UserAgent;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ws.rs.core.*;
import javax.ws.rs.*;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;

/**
 * REST Web Service
 *
 * @author Htoonlin
 */
@Path("auth")
public class AuthResource extends DefaultResource {

    private static final Logger logger = Logger.getLogger(AuthResource.class.getName());
    private UserDAO userDao;
    private AuthMailSend mailSend;

    public AuthResource() {
        mailSend = new AuthMailSend();
    }

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
                        tokenDAO.cleanToken((int) authUser.getId());
                    }
                    TokenEntity authToken = tokenDAO.generateToken((int) authUser.getId(), request.getDeviceId(), this.getDeviceOS());
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse cleanToken(AuthRequest request) throws Exception {
        return this.authProcess(request, true);
    }

    @PermitAll
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse authWithEmail(AuthRequest request, @HeaderParam("user-agent") String userAgent) throws Exception {
        return this.authProcess(request, false);
    }

    @PermitAll
    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
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
            mailSend.activateLink(user, userAgentString, getBaseURI());
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
    @GET
    @Path("activate")
    @Produces("text/html")
    public Response linkActivation(@DefaultValue("") @QueryParam("token") String request) throws Exception {
        VelocityContext data = new VelocityContext();
        data.put("current_year", Globalizer.getDateString("yyyy", new Date()));
        try {
            request = SecurityInstance.base64Decode(request);
            ActivateRequest activateRequest = Globalizer.jsonMapper().readValue(request, ActivateRequest.class);
            activateRequest.setTimeStamp((new Date()).getTime());
            DefaultResponse response = (DefaultResponse) this.otpActivation(activateRequest);
            if (response.getContent() instanceof UserEntity) {
                data.put("title", "Activation Success");
                data.put("message", "<p>Your account is ready. Thank you for your registration.</p>");
            } else {
                data.put("title", "Activation Failed");
                data.put("message", "<p class=\"text-warning\">Sorry! Your activation token is <strong>Invalid.</strong></p>");
            }
        } catch (Exception e) {
            logger.error(e);
            data.put("title", "SERVER ERROR");
            data.put("message", "<p class=\"text-danger\">" + e.getLocalizedMessage() + "</p>");
        }
        String output = TemplateManager.mergeTemplate("auth-message.vm", data);
        return Response.ok(output).build();
    }

    @PermitAll
    @Path("activate")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
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
                mailSend.activateLink(user, userAgentString, getBaseURI());
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
            TokenEntity authToken = tokenDAO.generateToken((int) user.getId(), request.getDeviceId(), this.getDeviceOS());
            user.setCurrentToken(authToken);
            userDao.commitTransaction();
            return new DefaultResponse(user);
        } catch (Exception e) {
            userDao.rollbackTransaction();
            logger.error(e);
            throw e;
        }
    }

    @PermitAll
    @Path("resetPassword")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse resetPassword(ChangePasswordRequest request) throws Exception {
        try {
            if (!request.isValid()) {
                return new ErrorResponse(request.getErrors());
            }
            MessageResponse message = new MessageResponse(400, ResponseType.WARNING, 
                    "INVALID_TOKEN", "Sorry! Requested token is invalid or expired.");
            String token = request.getExtra().get("token").toString();
            UserEntity user = userDao.userAuth(request.getEmail(), request.getOldPassword());

            if (user == null) {
                return new DefaultResponse(new MessageResponse(204, ResponseType.WARNING,
                        "NO_DATA", "There is no data for your request."));
            }

            if (!user.getOtpToken().equals(token) || user.getOtpExpired().before(new Date())) {
                mailSend.forgetPasswordLink(user, getBaseURI());
                userDao.update(user, true);
                message = new MessageResponse(400, ResponseType.WARNING,
                        "TOKEN_EXPIRE", "Sorry! Your token has expired. We send new link to your email.");
            } else if (user.getEmail().equalsIgnoreCase(request.getEmail())
                    && user.getPassword().equals(request.getOldPassword())
                    && user.getOtpToken().equals(token)) {
                String newPassword = SecurityInstance.md5String(request.getEmail(), request.getNewPassword());
                user.setPassword(newPassword);
                user.setVersion(user.getVersion() + 1);
                userDao.update(user, true);
                message = new MessageResponse(202, ResponseType.SUCCESS,
                        "UPDATED", "We updated the new password on your request successfully.");
            }

            return new DefaultResponse(message);
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

    @PermitAll
    @Path("forgetPassword/{email}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public DefaultResponse forgetPassword(@PathParam("email") String email) throws Exception {
        MessageResponse message;
        try {
            if (!MailgunService.getInstance().checkMail(email)) {
                message = new MessageResponse(400, ResponseType.WARNING, "INVALID_EMAIL", "Invalid email address.");
            } else {
                UserEntity user = userDao.getUserByEmail(email);
                if (user == null) {
                    message = new MessageResponse(400, ResponseType.WARNING, "INVALID_EMAIL", "Invalid email address.");
                } else {
                    mailSend.forgetPasswordLink(user, getBaseURI());
                    userDao.update(user, true);
                    message = new MessageResponse(200, ResponseType.SUCCESS, "SUCCESS", "We send the reset password link to your e-mail.");
                }
            }
            return new DefaultResponse(message);
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }
}
