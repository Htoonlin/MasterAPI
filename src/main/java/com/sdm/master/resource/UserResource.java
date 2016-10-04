/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import com.sdm.core.Setting;
import com.sdm.core.util.mail.MailInfo;
import com.sdm.core.util.mail.MailgunService;
import com.sdm.core.response.ResponseType;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.MessageResponse;
import com.sdm.core.resource.RestResource;
import com.sdm.core.response.ErrorResponse;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.util.SecurityInstance;
import com.sdm.master.dao.UserDAO;
import com.sdm.master.entity.UserEntity;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;

/**
 * REST Web Service
 *
 * @author Htoonlin
 */
@Path("user")
public class UserResource extends RestResource<UserEntity, Integer> {

    private static final Logger LOG = Logger.getLogger(UserResource.class.getName());

    private UserDAO userDAO;

    @PostConstruct
    public void onLoad() {
        userDAO = new UserDAO(getHttpSession());
    }

    private void sendWelcome(UserEntity user, String rawPassword) throws Exception {
        StringBuilder mailContent = new StringBuilder();
        mailContent.append("<h3 style=\"color:#5cb85c\">Welcome!</h3>")
                .append("<p>Site admin created the account for you. <br />")
                .append("e-mail: <b>" + user.getEmail() + "</b><br />")
                .append("name : <b>" + user.getDisplayName() + "</b><br />")
                .append("password : <b>" + rawPassword + "</b></p>")
                .append("<p style=\"color:#d9534f\">Note: don't forget to delete this email or change to new password.</p>")
                .append("<p>It is system generated mail.</p>");

        MailInfo info = new MailInfo(Setting.getInstance().MAILGUN_DEF_MAIL_SENDER, user.getEmail(), "Welcome!", mailContent.toString());
        MailgunService.getInstance().sendHTML(info);
    }

    @Override
    public IBaseResponse create(UserEntity request) throws Exception {
        try {
            Map<String, String> errors = new HashMap<>();
            if (!request.isValid()) {
                errors = request.getErrors();
                return new ErrorResponse(errors);
            }
            if (!MailgunService.getInstance().checkMail(request.getEmail())) {
                errors.put("INVALID_EMAIL", "Sorry! Requested email is not valid.");
                return new ErrorResponse(errors);
            }

            UserEntity user = userDAO.getUserByEmail(request.getEmail());
            if (user != null && user.getEmail().equalsIgnoreCase(request.getEmail())) {
                errors.put("email", "Sorry! someone already registered with this email");
                return new ErrorResponse(errors);
            }
            String rawPassword = request.getPassword();
            String password = SecurityInstance.md5String(request.getEmail(), rawPassword);
            request.setPassword(password);
            request.setStatus('A');
            userDAO.insert(request, true);
            this.sendWelcome(request, rawPassword);
            MessageResponse message = new MessageResponse(201, ResponseType.SUCCESS,
                    "CREATED", "We created new record on your request successfully.");
            return new DefaultResponse(message);
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }

    @Override
    public IBaseResponse update(UserEntity request, Integer id) throws Exception {
        try {
            request.setPassword("generate_secret");
            if (!request.isValid()) {
                return new ErrorResponse(request.getErrors());
            }
            UserEntity entity = userDAO.fetchById(id);
            if (entity == null || entity.getId() != request.getId()) {
                return new DefaultResponse(new MessageResponse(204, ResponseType.WARNING,
                        "NO_DATA", "There is no data for your request."));
            }
            request.setPassword(entity.getPassword());
            request.setVersion(entity.getVersion() + 1);
            userDAO.update(request, true);
            MessageResponse message = new MessageResponse(202, ResponseType.SUCCESS,
                    "UPDATED", "We updated the record with your request successfully.");
            return new DefaultResponse(message);
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }
}
