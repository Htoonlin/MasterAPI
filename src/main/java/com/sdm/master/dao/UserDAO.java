/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.dao;

import com.sdm.core.database.dao.RestDAO;
import com.sdm.master.entity.UserEntity;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;

/**
 *
 * @author Htoonlin
 */
public class UserDAO extends RestDAO<UserEntity>{

    private static final Logger logger = Logger.getLogger(UserDAO.class.getName());

    private final String SELECT_BY_EMAIL = "from UserEntity u WHERE u.email = :email AND u.deletedAt IS NULL";
    private final String GET_USER_BY_TOKEN = "from UserEntity u WHERE u.email = :email AND u.otpToken = :token AND u.deletedAt IS NULL";
    private final String AUTH_BY_EMAIL = "FROM UserEntity u WHERE u.email = :email AND u.password = :password AND u.deletedAt IS NULL";

    public UserDAO(HttpSession httpSession) {
        super(UserEntity.class, httpSession);
    }

    public UserDAO(Session session, HttpSession httpSession) {
        super(session, UserEntity.class, httpSession);
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
