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
import com.sdm.core.util.ITemplateManager;
import com.sdm.core.util.SecurityInstance;
import com.sdm.master.dao.UserDAO;
import com.sdm.master.entity.UserEntity;
import com.sdm.master.util.AuthMailSend;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
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

    @Inject
    ITemplateManager templateManager;

    private UserDAO userDAO;

    @PostConstruct
    public void onLoad() {
        userDAO = new UserDAO(getHttpSession());
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

            AuthMailSend mailSend = new AuthMailSend(templateManager);
            mailSend.welcomeUser(user, rawPassword);

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
            if (entity == null || entity.getId().equals(request.getId())) {
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
