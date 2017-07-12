/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.sdm.core.resource.DefaultResource;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.ErrorResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.model.MessageModel;
import com.sdm.core.util.SecurityManager;
import com.sdm.master.dao.UserDAO;
import com.sdm.master.entity.UserEntity;
import com.sdm.master.request.ChangePasswordRequest;

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
		UserEntity user = userDAO.fetchById(getUserId());
		if (user == null) {
			MessageModel message = new MessageModel(204, "Invalid User", "There is no user. (or) User is not active.");
			return new DefaultResponse<>(message);
		}
		return new DefaultResponse<UserEntity>(user);
	}

	@RolesAllowed("user")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public IBaseResponse setProfile(UserEntity request) throws Exception {
		try {
			UserEntity currentUser = userDAO.fetchById(getUserId());
			if (currentUser == null) {
				MessageModel message = new MessageModel(204, "Invalid User",
						"There is no user. (or) User is not active.");
				return new DefaultResponse<>(message);
			}
			request.setPassword(currentUser.getPassword());

			/*
			 * if (!request.isValid()) { return new ErrorResponse(request.getErrors()); }
			 */
			currentUser.setDisplayName(request.getDisplayName());
			currentUser.setOnline(request.isOnline());
			currentUser.setCountryCode(request.getCountryCode());
			currentUser.setProfileImage(request.getProfileImage());
			currentUser = userDAO.update(currentUser, true);
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
			if (!request.isValid()) {
				Map<String, String> errors = request.getErrors();
				return new ErrorResponse(errors);
			}

			MessageModel message = new MessageModel(202, "Changed password",
					"We updated the new password on your request successfully.");

			String oldPassword = SecurityManager.hashString(request.getEmail(), request.getOldPassword());
			UserEntity user = userDAO.userAuth(request.getEmail(), oldPassword);

			if (user == null || user.getId() != getUserId()) {
				message = new MessageModel(204, "Invalid User", "There is no user. (or) User is not active.");
				return new DefaultResponse<>(message);
			}

			if (!(user.getEmail().equalsIgnoreCase(request.getEmail()) && user.getPassword().equals(oldPassword))) {
				message = new MessageModel(400, "Invalid Password", "Hey! your old password is wrong. pls try again.");
				return new DefaultResponse<>(message);
			}
			String newPassword = SecurityManager.hashString(request.getEmail(), request.getNewPassword());
			user.setPassword(newPassword);
			userDAO.update(user, true);
			return new DefaultResponse<>(message);
		} catch (Exception e) {
			LOG.error(e);
			throw e;
		}
	}
}
