/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import com.sdm.core.resource.DefaultResource;
import com.sdm.core.resource.UserAllowed;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.model.MessageModel;
import com.sdm.core.util.SecurityManager;
import com.sdm.master.dao.UserDAO;
import com.sdm.master.entity.UserEntity;
import com.sdm.master.request.ChangePasswordRequest;
import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 * REST Web Service
 *
 * @author Htoonlin
 */
@UserAllowed
@Path("/me")
public class ProfileResource extends DefaultResource {

    private static final Logger LOG = Logger.getLogger(ProfileResource.class.getName());

    private UserDAO userDAO;

    @PostConstruct
    protected void init() {
        userDAO = new UserDAO(this);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse getProfile() {
        DefaultResponse response = this.validateCache();
        // Cache validation
        if (response != null) {
            return response;
        }

        UserEntity user = userDAO.fetchById(getCurrentUserId());
        if (user == null) {
            throw new NullPointerException("There is no user. (or) User is not active.");
        }

        response = new DefaultResponse<>(user);
        response.setHeaders(this.buildCache());
        return response;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse setProfile(@Valid UserEntity request) {
        try {
            UserEntity currentUser = userDAO.fetchById(getCurrentUserId());
            if (currentUser == null) {
                throw new NullPointerException("There is no user. (or) User is not active.");
            }

            currentUser.setDisplayName(request.getDisplayName());
            currentUser = userDAO.update(currentUser, true);

            this.modifiedResource();
            return new DefaultResponse<>(currentUser);
        } catch (NullPointerException e) {
            LOG.error(e);
            throw e;
        }
    }

    @Path("changePassword")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse changePassword(ChangePasswordRequest request) {
        try {
            MessageModel message = new MessageModel(202, "Changed password",
                    "We updated the new password on your request successfully.");

            String oldPassword = SecurityManager.hashString(request.getOldPassword());
            UserEntity user = userDAO.userAuth(request.getUser(), oldPassword);

            if (user == null || user.getId() != getCurrentUserId()) {
                throw new NullPointerException("There is no user (or) old password is wrong. Pls try again.");
            }

            String newPassword = SecurityManager.hashString(request.getNewPassword());
            user.setPassword(newPassword);
            userDAO.update(user, true);
            this.modifiedResource();
            return new DefaultResponse<>(message);
        } catch (NullPointerException e) {
            LOG.error(e);
            throw e;
        }
    }
}
