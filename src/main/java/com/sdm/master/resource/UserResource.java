/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Path;

import org.apache.log4j.Logger;

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
import com.sdm.master.entity.UserEntity;
import com.sdm.master.util.AuthMailSend;

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

	@PostConstruct
	protected void init() {
		if (this.userDAO == null) {
			userDAO = new UserDAO(getUserId());
		}
	}

	@Override
	protected RestDAO getDAO() {
		return this.userDAO;
	}

	@Override
	public IBaseResponse create(@Valid UserEntity request) {
		UserEntity user = userDAO.getUserByEmail(request.getEmail());
		if (user != null && user.getEmail().equalsIgnoreCase(request.getEmail())) {
			InvalidRequestException invalidRequest = new InvalidRequestException();
			invalidRequest.addError("email", "Sorry! someone already registered with this email", request.getEmail());
			throw invalidRequest;
		}

		String rawPassword = request.getPassword();
		String password = SecurityManager.hashString(request.getEmail(), rawPassword);
		request.setPassword(password);
		request.setStatus('A');
		UserEntity createdUser = userDAO.insert(request, true);
		this.modifiedResource();

		// Send Welcome mail to User
		AuthMailSend mailSend = new AuthMailSend(mailManager, templateManager);
		mailSend.welcomeUser(createdUser, rawPassword);

		return new DefaultResponse<UserEntity>(201, ResponseType.SUCCESS, createdUser);
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

			request.setEmail(dbEntity.getEmail());
			request.setPassword(dbEntity.getPassword());
			request.setFacebookId(dbEntity.getFacebookId());

			userDAO.update(request, true);
			this.modifiedResource();

			return new DefaultResponse<UserEntity>(202, ResponseType.SUCCESS, request);
		} catch (Exception e) {
			LOG.error(e);
			throw e;
		}
	}

	@Override
	protected Logger getLogger() {
		return UserResource.LOG;
	}
}
