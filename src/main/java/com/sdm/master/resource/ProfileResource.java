/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import com.sdm.core.exception.InvalidRequestException;
import com.sdm.core.resource.DefaultResource;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.model.MessageModel;
import com.sdm.core.util.SecurityManager;
import com.sdm.master.dao.UserDAO;
import com.sdm.master.entity.UserEntity;
import com.sdm.master.request.ChangePasswordRequest;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
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
@Path("/me")
public class ProfileResource extends DefaultResource {

    private static final Logger LOG = Logger.getLogger(ProfileResource.class.getName());

    private UserDAO userDAO;

    @PostConstruct
    protected void init() {
        userDAO = new UserDAO(getUserId());
    }

    @RolesAllowed("user")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse getProfile() throws Exception {
        DefaultResponse response = this.validateCache();
        // Cache validation
        if (response != null) {
            return response;
        }

        UserEntity user = userDAO.fetchById(getUserId());
        if (user == null) {
            MessageModel message = new MessageModel(204, "Invalid User", "There is no user. (or) User is not active.");
            return new DefaultResponse<>(message);
        }
        response = new DefaultResponse<UserEntity>(user);
        response.setHeaders(this.buildCache());
        return response;
    }

    @RolesAllowed("user")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse setProfile(@Valid UserEntity request) throws Exception {
        try {
            UserEntity currentUser = userDAO.fetchById(getUserId());
            if (currentUser == null) {
                MessageModel message = new MessageModel(204, "Invalid User",
                        "There is no user. (or) User is not active.");
                return new DefaultResponse<>(message);
            }
            request.setPassword(currentUser.getPassword());
            request.setEmail(currentUser.getEmail());
            request.setFacebookId(currentUser.getFacebookId());

            currentUser = userDAO.update(request, true);
            this.modifiedResource();
            return new DefaultResponse<UserEntity>(currentUser);
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }

    @RolesAllowed("user")
    @Path("changePassword")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse changePassword(ChangePasswordRequest request) throws Exception {
        try {
            MessageModel message = new MessageModel(202, "Changed password",
                    "We updated the new password on your request successfully.");

            String oldPassword = SecurityManager.hashString(request.getEmail(), request.getOldPassword());
            UserEntity user = userDAO.userAuth(request.getEmail(), oldPassword);

            if (user == null || user.getId() != getUserId()) {
                message = new MessageModel(204, "Invalid User", "There is no user. (or) User is not active.");
                return new DefaultResponse<>(message);
            }

            if (!(user.getEmail().equalsIgnoreCase(request.getEmail()) && user.getPassword().equals(oldPassword))) {
                throw new InvalidRequestException("old_password",  
                        "Hey! your old password is wrong. pls try again.", 
                        request.getOldPassword()); 
            }
            String newPassword = SecurityManager.hashString(request.getEmail(), request.getNewPassword());
            user.setPassword(newPassword);
            userDAO.update(user, true);
            this.modifiedResource();
            return new DefaultResponse<>(message);
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }
}
