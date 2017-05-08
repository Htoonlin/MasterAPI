/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import com.sdm.core.response.MessageResponse;
import com.sdm.core.resource.DefaultResource;
import com.sdm.core.response.ResponseType;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.ErrorResponse;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.ListResponse;
import com.sdm.core.util.SecurityInstance;
import com.sdm.master.dao.NotificationDAO;
import com.sdm.master.dao.UserDAO;
import com.sdm.master.entity.NotifyEntity;
import com.sdm.master.entity.UserEntity;
import com.sdm.master.request.ChangePasswordRequest;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.apache.log4j.Logger;

/**
 * REST Web Service
 *
 * @author Htoonlin
 */
@Path("/me")
public class ProfileResource extends DefaultResource {

    private static final Logger logger = Logger.getLogger(ProfileResource.class.getName());

    private UserDAO userDAO;

    @PostConstruct
    public void onLoad() {
        userDAO = new UserDAO(getHttpSession());
    }

    @RolesAllowed("user")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse getProfile() throws Exception {
        UserEntity user = userDAO.fetchById(getUserId());
        if (user == null) {
            return new DefaultResponse(new MessageResponse(204, ResponseType.WARNING,
                    "NO_DATA", "There is no user. (or) User is not active."));
        }
        return new DefaultResponse(user);
    }

    @RolesAllowed("user")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse setProfile(UserEntity request) throws Exception {
        try {
            UserEntity currentUser = userDAO.fetchById(getUserId());
            if (currentUser == null) {
                return new DefaultResponse(new MessageResponse(204, ResponseType.WARNING,
                        "NO_DATA", "There is no user. (or) User is not active."));
            }
            request.setPassword(currentUser.getPassword());
            if (!request.isValid()) {
                return new ErrorResponse(request.getErrors());
            }
            currentUser.setDisplayName(request.getDisplayName());
            currentUser.setOnline(request.isOnline());
            currentUser.setCountryCode(request.getCountryCode());
            currentUser.setProfileImage(request.getProfileImage());
            currentUser.setVersion(currentUser.getVersion() + 1);
            userDAO.update(currentUser, true);
            MessageResponse message = new MessageResponse(202, ResponseType.SUCCESS,
                    "UPDATED", "We updated the record with your request successfully.");
            return new DefaultResponse(message);
        } catch (Exception e) {
            logger.error(e);
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
            if (!request.isValid()) {
                Map<String, String> errors = request.getErrors();
                return new ErrorResponse(errors);
            }

            String oldPassword = SecurityInstance.md5String(request.getEmail(), request.getOldPassword());
            UserEntity user = userDAO.userAuth(request.getEmail(), oldPassword);

            if (user == null || !user.getId().equals(getUserId())) {
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
            userDAO.update(user, true);
            MessageResponse message = new MessageResponse(202, ResponseType.SUCCESS,
                    "UPDATED", "We updated the new password on your request successfully.");
            return new DefaultResponse(message);
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

    @RolesAllowed("user")
    @GET
    @Path("alerts")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse getAlerts() throws Exception {
        try {
            UserEntity user = userDAO.fetchById(getUserId());
            if (user == null) {
                return new DefaultResponse(new MessageResponse(204, ResponseType.WARNING,
                        "NO_DATA", "There is no data for your request."));
            }
            NotificationDAO notifyDAO = new NotificationDAO();
            List<NotifyEntity> alerts = notifyDAO.getNotificationsByUserId(getUserId());
            return new DefaultResponse(new ListResponse(alerts));
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }
}
