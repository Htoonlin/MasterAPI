/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.dao;

import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.master.entity.UserEntity;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *
 * @author Htoonlin
 */
public class UserDAO extends RestDAO {

    private static final Logger LOG = Logger.getLogger(UserDAO.class.getName());

    public UserDAO(int userId) {
        super(UserEntity.class.getName(), userId);
        LOG.info("Start DAO");
    }

    public UserDAO(Session session, int userId) {
        super(session, UserEntity.class.getName(), userId);
    }

    @Deprecated
    public UserEntity userAuthByFacebook(String facebookId) {
        Map<String, Object> params = new HashMap<>();
        params.put("facebookId", facebookId);
        return super.fetchOneByNamedQuery("UserEntity.AUTH_BY_FACEBOOK", params);
    }

    @Deprecated
    public UserEntity userAuth(String email, String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        return super.fetchOneByNamedQuery("UserEntity.AUTH_BY_EMAIL", params);
    }

    @Deprecated
    public UserEntity checkToken(String email, String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("token", token);
        return super.fetchOneByNamedQuery("UserEntity.GET_USER_BY_TOKEN", params);
    }

    @Deprecated
    public UserEntity getUserByEmail(String email) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        return super.fetchOneByNamedQuery("UserEntity.SELECT_BY_EMAIL", params);
    }
}
