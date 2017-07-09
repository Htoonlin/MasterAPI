/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.sdm.core.di.IMailManager;
import com.sdm.core.di.ITemplateManager;
import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.core.resource.RestResource;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.ErrorResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.MessageResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.core.util.SecurityManager;
import com.sdm.master.dao.UserDAO;
import com.sdm.master.entity.UserEntity;
import com.sdm.master.util.AuthMailSend;

/**
 * REST Web Service
 *
 * @author Htoonlin
 */
@Path("user")
public class UserResource extends RestResource<UserEntity, Long> {

	private static final Logger LOG = Logger.getLogger(UserResource.class.getName());

	@Inject
	ITemplateManager templateManager;

	@Inject
	IMailManager mailManager;

	private UserDAO userDAO;

	@PostConstruct
	protected void init() {
		if (this.userDAO == null) {
			userDAO = new UserDAO();
		}
	}

	@Override
	protected RestDAO getDAO() {
		return this.userDAO;
	}

	@Override
	public IBaseResponse create(UserEntity request) throws Exception {
		try {
			ErrorResponse errors = new ErrorResponse();
			if (!request.isValid()) {
				errors.setContent(request.getErrors());
				return errors;
			}

			UserEntity user = userDAO.getUserByEmail(request.getEmail());
			if (user != null && user.getEmail().equalsIgnoreCase(request.getEmail())) {
				errors.addError("email", "Sorry! someone already registered with this email");
				return errors;
			}

			String rawPassword = request.getPassword();
			String password = SecurityManager.md5String(request.getEmail(), rawPassword);
			request.setPassword(password);
			request.setStatus('A');
			UserEntity createdUser = userDAO.insert(request, true);

			AuthMailSend mailSend = new AuthMailSend(mailManager, templateManager);
			mailSend.welcomeUser(user, rawPassword);
			return new DefaultResponse<UserEntity>(createdUser);

		} catch (Exception e) {
			LOG.error(e);
			throw e;
		}
	}

	@Override
	public IBaseResponse update(UserEntity request, Long id) throws Exception {
		try {
			request.setPassword("temp-password");
			request.setEmail("temp@temp.com");
			if (!request.isValid()) {
				return new ErrorResponse(request.getErrors());
			}

			UserEntity dbEntity = userDAO.fetchById(id);
			if (dbEntity == null) {
				return new MessageResponse(204, ResponseType.WARNING, "There is no data for your request.");
			} else if (!Objects.equals(dbEntity.getId(), request.getId())) {
				return new MessageResponse(400, ResponseType.WARNING, "Invalid request ID.");
			}

			request.setEmail(dbEntity.getEmail());
			request.setPassword(dbEntity.getPassword());

			userDAO.update(request, true);
			return new DefaultResponse<UserEntity>(request);
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
