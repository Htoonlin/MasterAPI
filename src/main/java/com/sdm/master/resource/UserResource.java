/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import com.sdm.core.di.IMailManager;
import com.sdm.core.di.ITemplateManager;
import com.sdm.core.exception.InvalidRequestException;
import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.core.resource.RestResource;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.core.response.model.MessageModel;
import com.sdm.core.util.SecurityManager;
import com.sdm.master.dao.UserDAO;
import com.sdm.master.dao.UserExtraDAO;
import com.sdm.master.entity.UserEntity;
import com.sdm.master.entity.UserExtraEntity;
import com.sdm.master.util.AuthMailSend;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;

/**
 * REST Web Service
 *
 * @author Htoonlin
 */
@Path("users")
public class UserResource extends RestResource<UserEntity, Integer> {

    private static final Logger LOG = Logger.getLogger(UserResource.class.getName());

    @Inject
    ITemplateManager templateManager;

    @Inject
    IMailManager mailManager;

    private UserDAO userDAO;
    private UserExtraDAO extraDAO;

    @PostConstruct
    protected void init() {
        if (this.userDAO == null) {
            userDAO = new UserDAO(getUserId());
        }

        extraDAO = new UserExtraDAO(userDAO.getSession(), getUserId());
    }

    @Override
    protected RestDAO getDAO() {
        return this.userDAO;
    }
    
    @Override
    public IBaseResponse create(@Valid UserEntity request) {
        try {
            UserEntity user = userDAO.getUserByEmail(request.getEmail());
            if (user != null && user.getEmail().equalsIgnoreCase(request.getEmail())) {
                InvalidRequestException invalidRequest = new InvalidRequestException();
                invalidRequest.addError("email", "Sorry! someone already registered with this email", request.getEmail());
                throw invalidRequest;
            }

            Pattern pattern=Pattern.compile("[a-zA-Z0-9_\\.]+");
            boolean isValid = pattern.matcher(request.getUserName()).matches();
            LOG.debug(isValid);
            
            if (request.getUserName().contains(" ") || !isValid) {
                InvalidRequestException invalidRequest = new InvalidRequestException();
                invalidRequest.addError("user name", "Sorry! invalid user name, allow char (a-zA-Z0-9) and special char (`.` and `_`). Eg./ mg_hla.09", request.getUserName());
                throw invalidRequest;
            }

            user = userDAO.getUserByName(request.getUserName());
            if (user != null && user.getUserName().equalsIgnoreCase(request.getUserName())) {
                InvalidRequestException invalidRequest = new InvalidRequestException();
                invalidRequest.addError("user name", "Sorry! someone already registered with this user name", request.getUserName());
                throw invalidRequest;
            }

            String rawPassword = request.getPassword();
            String password = SecurityManager.hashString(request.getEmail(), rawPassword);
            String uPassword = SecurityManager.hashString(request.getUserName(), rawPassword);
            request.setPassword(password);
            request.setuPassword(uPassword);
            request.setStatus('A');
            userDAO.beginTransaction();
            UserEntity createdUser = userDAO.insert(request, false);

            Set<UserExtraEntity> extras = request.getExtra();
            for (UserExtraEntity extra : extras) {
                extra.setUserId(createdUser.getId());
                extraDAO.insert(extra, false);
            }

            userDAO.commitTransaction();

            this.modifiedResource();
            // Send Welcome mail to User
            AuthMailSend mailSend = new AuthMailSend(mailManager, templateManager);
            mailSend.welcomeUser(createdUser, rawPassword);

            return new DefaultResponse<UserEntity>(201, ResponseType.SUCCESS, createdUser);
        } catch (Exception ex) {
            userDAO.rollbackTransaction();
            getLogger().error(ex);
            throw ex;
        }
    }

    @Override
    public IBaseResponse update(@Valid UserEntity request, Integer id) {
        try {
            UserEntity dbEntity = userDAO.fetchById(id);
            if (dbEntity == null) {
                MessageModel message = new MessageModel(204, "No Data", "There is no data for your request.");
                return new DefaultResponse<>(message);
            } else if (!Objects.equals(dbEntity.getId(), request.getId())) {
                MessageModel message = new MessageModel(400, "Invalid", "Invalid request ID.");
                return new DefaultResponse<>(message);
            }

            userDAO.beginTransaction();

            request.setUserName(dbEntity.getUserName());
            request.setEmail(dbEntity.getEmail());
            request.setPassword(dbEntity.getPassword());
            request.setFacebookId(dbEntity.getFacebookId());

            userDAO.update(request, false);

            Set<UserExtraEntity> extras = request.getExtra();

            //Delete All Extras
            List<UserExtraEntity> oldExtras = extraDAO.getUserExtraByUser(dbEntity.getId());
            for (UserExtraEntity oldExtra : oldExtras) {
                extraDAO.delete(oldExtra, false);
            }

            //Insert Back All User Extras
            for (UserExtraEntity extra : extras) {
                extra.setUserId(dbEntity.getId());

                extraDAO.insert(extra, false);
            }

            userDAO.commitTransaction();
            this.modifiedResource();

            return new DefaultResponse<UserEntity>(202, ResponseType.SUCCESS, request);
        } catch (Exception e) {
            userDAO.rollbackTransaction();
            LOG.error(e);
            throw e;
        }
    }

    @Override
    public IBaseResponse remove(Integer id) {
        try {
            MessageModel message = new MessageModel(204, "No Data", "There is no data for your request.");
            UserEntity entity = userDAO.fetchById(id);
            if (entity == null) {
                return new DefaultResponse<>(message);
            }

            userDAO.beginTransaction();

            //Delete All Extras
            List<UserExtraEntity> extras = extraDAO.getUserExtraByUser(id);
            for (UserExtraEntity extra : extras) {
                extraDAO.delete(extra, false);
            }

            userDAO.delete(entity, false);

            userDAO.commitTransaction();
            this.modifiedResource();

            message = new MessageModel(202, "Deleted", "We deleted the record with your request successfully.");
            return new DefaultResponse<>(202, ResponseType.SUCCESS, message);
        } catch (Exception e) {
            userDAO.rollbackTransaction();
            getLogger().error(e);
            throw e;
        }
    }

    @Override
    protected Logger getLogger() {
        return UserResource.LOG;
    }
}
