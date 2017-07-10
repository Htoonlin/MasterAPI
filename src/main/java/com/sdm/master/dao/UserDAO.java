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

	public UserDAO(long userId) {
		super(UserEntity.class.getName(), userId);
		LOG.info("Start DAO");
	}

	public UserDAO(Session session, long userId) {
		super(session, UserEntity.class.getName(), userId);
	}

	public UserEntity userAuth(String email, String password) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("email", email);
		params.put("password", password);
		return super.fetchOne(AUTH_BY_EMAIL, params);
	}

	public UserEntity checkToken(String email, String token) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("email", email);
		params.put("token", token);
		return super.fetchOne(GET_USER_BY_TOKEN, params);
	}

	public UserEntity getUserByEmail(String email) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("email", email);
		return super.fetchOne(SELECT_BY_EMAIL, params);
	}
}
