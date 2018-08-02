/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sdm.core.Globalizer;
import com.sdm.core.di.IMailManager;
import com.sdm.core.di.ITemplateManager;
import com.sdm.core.exception.InvalidRequestException;
import com.sdm.core.resource.DefaultResource;
import com.sdm.core.resource.UserAllowed;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.model.MessageModel;
import com.sdm.core.util.SecurityManager;
import com.sdm.facebook.service.AuthService;
import com.sdm.master.dao.TokenDAO;
import com.sdm.master.dao.UserDAO;
import com.sdm.master.entity.TokenEntity;
import com.sdm.master.entity.UserEntity;
import com.sdm.master.request.ActivateRequest;
import com.sdm.master.request.AuthRequest;
import com.sdm.master.request.ChangePasswordRequest;
import com.sdm.master.request.FacebookAuthRequest;
import com.sdm.master.request.RegistrationRequest;
import com.sdm.master.util.AuthMailSend;
import eu.bitwalker.useragentutils.UserAgent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Htoonlin
 */
@Path("auth")
public class AuthResource extends DefaultResource {

    @Inject
    private ITemplateManager templateManager;

    @Inject
    private IMailManager mailManager;

    @Context
    private HttpServletRequest servletRequest;

    @HeaderParam("user-agent")
    private String userAgentString;

    private String getDeviceOS() {
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
        return userAgent.getOperatingSystem().getName();
    }

    private IBaseResponse authProcess(AuthRequest request, boolean cleanToken) throws SQLException {
        UserDAO userDao = new UserDAO(this);
        try {
            UserEntity authUser = userDao.userAuth(request.getUser(), request.getCryptPassword());
            if (authUser == null || authUser.getStatus() != UserEntity.ACTIVE || !request.isAuth(authUser)) {
                throw new InvalidRequestException("request",
                        "Opp! Request email or password is something wrong",
                        request);
            }

            userDao.beginTransaction();
            TokenDAO tokenDAO = new TokenDAO(userDao.getSession(), this);
            if (cleanToken) {
                tokenDAO.cleanToken(authUser.getId());
            }
            TokenEntity authToken = tokenDAO.generateToken(authUser.getId(), request.getDeviceId(),
                    this.getDeviceOS());

            // Generate and store JWT
            String token = authToken.generateJWT(userAgentString);

            authUser.setCurrentToken(token);
            userDao.commitTransaction();

            return new DefaultResponse<>(authUser);
        } catch (SQLException e) {
            userDao.rollbackTransaction();
            getLogger().error(e);
            throw e;
        }
    }

    @UserAllowed
    @POST
    @Path("clean")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse cleanToken(AuthRequest request) throws Exception {
        return this.authProcess(request, true);
    }

    @PermitAll
    @POST
    @Path("facebook")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse facebookAuth(@Valid FacebookAuthRequest request) throws SQLException {
        UserDAO userDao = new UserDAO(this);
        try {
            userDao.beginTransaction();

            getLogger().info("requesting to facebook by token.");
            AuthService facebookAuth = new AuthService(request.getAccessToken(), userDao);
            UserEntity userEntity = facebookAuth.authByFacebook();
            getLogger().info("Finished request to facebook by token.");

            if (userEntity == null) {
                throw new InvalidRequestException("request",
                        "Opp! Request email or password is something wrong",
                        request);
            }

            TokenDAO tokenDAO = new TokenDAO(userDao.getSession(), this);
            TokenEntity authToken = tokenDAO.generateToken(userEntity.getId(), request.getDeviceId(),
                    this.getDeviceOS());

            // Generate and store JWT
            String token = authToken.generateJWT(userAgentString);

            userEntity.setCurrentToken(token);
            userDao.commitTransaction();
            return new DefaultResponse<>(userEntity);
        } catch (SQLException ex) {
            getLogger().error(ex);
            userDao.rollbackTransaction();
            throw ex;
        }
    }

    @PermitAll
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse authWithEmail(@Valid AuthRequest request,
            @HeaderParam("user-agent") String userAgent) throws Exception {
        return this.authProcess(request, false);
    }

    @PermitAll
    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse userRegistration(@Valid RegistrationRequest request) throws JsonProcessingException {
        UserDAO userDao = new UserDAO(this);
        //Check user by user name
        UserEntity user = userDao.checkUser(request.getUserName());
        if (user != null && user.getUserName().equalsIgnoreCase(request.getUserName())) {
            throw new InvalidRequestException("username", "Sorry! someone already registered with this username",
                    request.getUserName());
        }

        //Check user by user email
        user = userDao.checkUser(request.getEmail());
        if (user != null && user.getEmail().equalsIgnoreCase(request.getEmail())) {
            throw new InvalidRequestException("email", "Sorry! someone already registered with this email",
                    request.getEmail());
        }

        String password = SecurityManager.hashString(request.getPassword());
        user = new UserEntity(request.getEmail(), request.getUserName(), request.getDisplayName(), password,
                true, UserEntity.PENDING);
        if (user.hasEmail()) {
            AuthMailSend mailSend = new AuthMailSend(mailManager, servletRequest, templateManager);
            mailSend.activateLink(user, userAgentString);
        }
        
        userDao.insert(user, true);

        MessageModel message = new MessageModel(200, "Registration Success",
                "Thank you for your registration.");

        return new DefaultResponse<>(message);
    }

    @PermitAll
    @GET
    @Path("activate")
    @Produces(MediaType.TEXT_HTML)
    public Response linkActivation(@DefaultValue("") @QueryParam("token") String request) {

        Map<String, Object> data = new HashMap<>();
        data.put("current_year", Globalizer.getDateString("yyyy", new Date()));
        try {
            request = SecurityManager.base64Decode(request);
            ActivateRequest activateRequest = Globalizer.jsonMapper().readValue(request, ActivateRequest.class);
            activateRequest.setTimestamp((new Date()).getTime());
            IBaseResponse response = this.otpActivation(activateRequest);
            if (response instanceof MessageModel) {
                data.put("title", response.getStatus());
                data.put("message", "<p class=\"text-warning\">" + response.getContent() + "</p>");
            } else if (response.getContent() instanceof UserEntity) {
                data.put("title", "Activation Success");
                data.put("message", "<p>Your account is ready. Thank you for your registration.</p>");
            } else {
                data.put("title", "Activation Failed");
                data.put("message",
                        "<p class=\"text-warning\">Sorry! Your activation token is <strong>Invalid.</strong></p>");
            }
        } catch (IOException | SQLException e) {
            getLogger().error(e);
            data.put("title", "SERVER ERROR");
            data.put("message", "<p class=\"text-danger\">" + e.getLocalizedMessage() + "</p>");
        }

        String output = templateManager.buildTemplate("auth-message.vm", data);
        return Response.ok(output).build();
    }

    @PermitAll
    @Path("activate")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse otpActivation(@Valid ActivateRequest request) throws JsonProcessingException, SQLException {
        UserDAO userDao = new UserDAO(this);
        try {
            UserEntity user = userDao.checkToken(request.getEmail(), request.getToken());
            if (user == null) {
                throw new NullPointerException("There is no data for your request.");
            }

            if (user.getOtpExpired().before(new Date())) {
                AuthMailSend mailSend = new AuthMailSend(mailManager, servletRequest, templateManager);
                mailSend.activateLink(user, userAgentString);
                userDao.update(user, true);
                throw new InvalidRequestException("token",
                        "Sorry! Your token has expired. We send new token to your email.",
                        request.getToken());
            }

            userDao.beginTransaction();
            user.setOtpToken(null);
            user.setOtpExpired(null);
            user.setStatus(UserEntity.ACTIVE);
            userDao.update(user, false);
            TokenDAO tokenDAO = new TokenDAO(userDao.getSession(), this);
            TokenEntity authToken = tokenDAO.generateToken(user.getId(), request.getDeviceId(), this.getDeviceOS());

            // Generate and store JWT
            String token = authToken.generateJWT(userAgentString);

            user.setCurrentToken(token);
            userDao.commitTransaction();
            return new DefaultResponse<>(user);
        } catch (JsonProcessingException | SQLException e) {
            userDao.rollbackTransaction();
            getLogger().error(e);
            throw e;
        }
    }

    @PermitAll
    @Path("resetPassword")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse resetPasswordWithToken(@DefaultValue("") @QueryParam("token") String request) throws IOException {
        UserDAO userDao = new UserDAO(this);

        try {
            request = SecurityManager.base64Decode(request);
            ActivateRequest activateRequest = Globalizer.jsonMapper().readValue(request, ActivateRequest.class);
            UserEntity user = userDao.checkToken(activateRequest.getEmail(), activateRequest.getToken());
            if (user == null) {
                throw new NullPointerException("There is no data for your request.");
            }

            if (!user.getOtpToken().equals(activateRequest.getToken()) || user.getOtpExpired().before(new Date())) {
                AuthMailSend mailSend = new AuthMailSend(mailManager, servletRequest, templateManager);
                mailSend.forgetPasswordLink(user);
                userDao.update(user, true);
                throw new InvalidRequestException("token",
                        "Sorry! Your token has expired. We send new token to your email.",
                        activateRequest.getToken());
            }

            String rawPassword = Globalizer.generateToken(12);

            user.setOtpExpired(null);
            user.setOtpToken(null);
            user.setPassword(SecurityManager.hashString(rawPassword));
            AuthMailSend mailSend = new AuthMailSend(mailManager, servletRequest, templateManager);
            mailSend.welcomeUser(user, rawPassword, "Created new password!");
            userDao.update(user, true);
            
        } catch (IOException e) {
            getLogger().error(e);
            throw e;
        }

        MessageModel message = new MessageModel(202, "OK", "We created new password and send mail to you.");
        return new DefaultResponse<>(message);
    }

    @PermitAll
    @Path("resetPassword")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse resetPassword(@Valid ChangePasswordRequest request) throws JsonProcessingException {
        UserDAO userDao = new UserDAO(this);

        try {
            String token = request.getToken();
            UserEntity user = userDao.checkToken(request.getUser(), request.getToken());
            if (user == null) {
                throw new NullPointerException("There is no data for your request.");
            }

            if (!user.getOtpToken().equals(token) || user.getOtpExpired().before(new Date())) {
                AuthMailSend mailSend = new AuthMailSend(mailManager, servletRequest, templateManager);
                mailSend.forgetPasswordLink(user);
                userDao.update(user, true);
                throw new InvalidRequestException("token",
                        "Sorry! Your token has expired. We send new token to your email.",
                        request.getToken());
            }

            String newPassword = SecurityManager.hashString(request.getNewPassword());

            user.setOtpExpired(null);
            user.setOtpToken(null);
            user.setPassword(newPassword);
            userDao.update(user, true);
            MessageModel message = new MessageModel(202, "OK", "We updated the new password on your request successfully.");

            return new DefaultResponse<>(message);
        } catch (JsonProcessingException e) {
            getLogger().error(e);
            throw e;
        }
    }

    @PermitAll
    @Path("forgetPassword/{email}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse forgetPassword(@PathParam("email") String email) throws JsonProcessingException {
        UserDAO userDao = new UserDAO(this);
        try {
            if (!mailManager.checkMail(email)) {
                throw new InvalidRequestException("email", "Invalid email address.", email);
            }

            UserEntity user = userDao.checkUser(email);
            if (user == null) {
                throw new InvalidRequestException("email", "Invalid email address.", email);
            }

            AuthMailSend mailSend = new AuthMailSend(mailManager, servletRequest, templateManager);
            mailSend.forgetPasswordLink(user);
            userDao.update(user, true);
            MessageModel message = new MessageModel(201, "Send Mail", "We send the reset password link to your e-mail.");
            return new DefaultResponse<>(message);
        } catch (JsonProcessingException e) {
            getLogger().error(e);
            throw e;
        }
    }
}
