/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import com.sdm.core.di.IMailManager;
import com.sdm.core.di.ITemplateManager;
import com.sdm.core.exception.InvalidRequestException;
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
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Path;

/**
 * REST Web Service
 *
 * @author Htoonlin
 */
@Path("users")
public class UserResource extends RestResource<UserEntity, Integer> {

    @Inject
    ITemplateManager templateManager;

    @Inject
    IMailManager mailManager;

    @Override
    public IBaseResponse create(@Valid UserEntity request) {
        UserDAO userDAO = new UserDAO(getDAO().getSession(), this);
        try {
            //Check user by userName
            UserEntity user = userDAO.checkUser(request.getUserName());
            if (user != null && user.getUserName().equalsIgnoreCase(request.getUserName())) {
                throw new InvalidRequestException("user name", 
                        "Sorry! someone already registered with this user name", 
                        request.getUserName());
            }

            //Validate user name.
            Pattern pattern = Pattern.compile("[a-zA-Z0-9_\\.]+");
            boolean isValid = pattern.matcher(request.getUserName()).matches();
            if (request.getUserName().contains(" ") || !isValid) {
                throw new InvalidRequestException("user_name", 
                        "Sorry! invalid user name, allow char (a-zA-Z0-9) and special char (`.` and `_`). Eg./ mg_hla.09", 
                        request.getUserName());
            }

            //Check user by email.
            if (request.hasEmail()) {
                user = userDAO.checkUser(request.getEmail());
                if (user != null && user.getEmail().equalsIgnoreCase(request.getEmail())) {
                    throw new InvalidRequestException("email", 
                            "Sorry! someone already registered with this email", 
                            request.getEmail());
                }
            }

            String rawPassword = request.getPassword();
            request.setPassword(SecurityManager.hashString(rawPassword));
            request.setStatus('A');
            userDAO.beginTransaction();
            UserEntity createdUser = userDAO.insert(request, false);

            UserExtraDAO extraDAO = new UserExtraDAO(getDAO().getSession(), this);

            Set<UserExtraEntity> extras = request.getExtra();
            for (UserExtraEntity extra : extras) {
                extra.setUserId(createdUser.getId());
                extraDAO.insert(extra, false);
            }

            userDAO.commitTransaction();

            this.modifiedResource();

            // Send Welcome mail to User
            if (request.hasEmail()) {
                AuthMailSend mailSend = new AuthMailSend(mailManager, templateManager);
                mailSend.welcomeUser(createdUser, rawPassword);
            }

            return new DefaultResponse<>(201, ResponseType.SUCCESS, createdUser);
        } catch (InvalidRequestException ex) {
            userDAO.rollbackTransaction();
            getLogger().error(ex);
            throw ex;
        }
    }

    @Override
    public IBaseResponse update(@Valid UserEntity request, Integer id) {

        UserDAO userDAO = new UserDAO(getDAO().getSession(), this);

        try {
            UserEntity dbEntity = this.checkData(id);
            if (!Objects.equals(dbEntity.getId(), request.getId())) {
                throw new InvalidRequestException("id", "Invalid request ID.", id);
            }

            userDAO.beginTransaction();

            request.setUserName(dbEntity.getUserName());
            request.setEmail(dbEntity.getEmail());
            request.setPassword(dbEntity.getPassword());
            request.setFacebookId(dbEntity.getFacebookId());

            userDAO.update(request, false);

            Set<UserExtraEntity> extras = request.getExtra();

            UserExtraDAO extraDAO = new UserExtraDAO(getDAO().getSession(), this);
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

            return new DefaultResponse<>(202, ResponseType.SUCCESS, request);
        } catch (InvalidRequestException | NullPointerException e) {
            userDAO.rollbackTransaction();
            getLogger().error(e);
            throw e;
        }
    }

    @Override
    public IBaseResponse remove(Integer id) {
        UserDAO userDAO = new UserDAO(getDAO().getSession(), this);
        try {
            UserEntity entity = this.checkData(id);

            userDAO.beginTransaction();

            UserExtraDAO extraDAO = new UserExtraDAO(getDAO().getSession(), this);
            //Delete All Extras
            List<UserExtraEntity> extras = extraDAO.getUserExtraByUser(id);
            for (UserExtraEntity extra : extras) {
                extraDAO.delete(extra, false);
            }

            userDAO.delete(entity, false);

            userDAO.commitTransaction();
            this.modifiedResource();

            MessageModel message = new MessageModel(202, "Deleted", "We deleted the record with your request successfully.");
            return new DefaultResponse<>(202, ResponseType.SUCCESS, message);
        } catch (Exception e) {
            userDAO.rollbackTransaction();
            getLogger().error(e);
            throw e;
        }
    }
}
