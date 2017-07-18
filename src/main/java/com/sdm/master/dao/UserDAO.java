/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.core.util.SecurityManager;
import com.sdm.facebook.response.User;
import com.sdm.master.entity.UserEntity;

/**
 *
 * @author Htoonlin
 */
public class UserDAO extends RestDAO {

	private static final Logger LOG = Logger.getLogger(UserDAO.class.getName());

	private final String SELECT_BY_EMAIL = "from UserEntity u WHERE u.email = :email";
	private final String GET_USER_BY_TOKEN = "from UserEntity u WHERE u.email = :email AND u.otpToken = :token";
	private final String AUTH_BY_EMAIL = "FROM UserEntity u WHERE u.email = :email AND u.password = :password";
	private final String AUTH_BY_FACEBOOK = "FROM UserEntity u WHERE u.facebookId = :facebookId";

	public UserDAO(int userId) {
		super(UserEntity.class.getName(), userId);
		LOG.info("Start DAO");
	}

	public UserDAO(Session session, int userId) {
		super(session, UserEntity.class.getName(), userId);
	}

	public UserEntity facebookMigrate(User facebookUser, boolean autoCommit) {
		UserEntity userEntity = getUserByEmail(facebookUser.getEmail());
		if (userEntity == null) {
			String randomPassword = SecurityManager.randomPassword(32);
			// New user registration with random password
			userEntity = new UserEntity();
			userEntity.setFacebookId(facebookUser.getId());
			userEntity.setDisplayName(facebookUser.getName());
			userEntity.setEmail(facebookUser.getEmail());
			userEntity.setPassword(SecurityManager.hashString(facebookUser.getEmail(), randomPassword));
			userEntity.setCountryCode(facebookUser.getLocale());
			userEntity.setStatus(UserEntity.ACTIVE);
			return this.insert(userEntity, autoCommit);
		} else {
			userEntity.setFacebookId(facebookUser.getId());
			return this.update(userEntity, autoCommit);
		}
	}

	public UserEntity userAuthByFacebook(String facebookId) {
		Map<String, Object> params = new HashMap<>();
		params.put("facebookId", facebookId);
		return super.fetchOne(AUTH_BY_FACEBOOK, params);
	}

	public UserEntity userAuth(String email, String password) {
		Map<String, Object> params = new HashMap<>();
		params.put("email", email);
		params.put("password", password);
		return super.fetchOne(AUTH_BY_EMAIL, params);
	}

	public UserEntity checkToken(String email, String token) {
		Map<String, Object> params = new HashMap<>();
		params.put("email", email);
		params.put("token", token);
		return super.fetchOne(GET_USER_BY_TOKEN, params);
	}

	public UserEntity getUserByEmail(String email) {
		Map<String, Object> params = new HashMap<>();
		params.put("email", email);
		return super.fetchOne(SELECT_BY_EMAIL, params);
	}
}
