/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.dao;

import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.master.entity.UserEntity;
import java.util.HashMap;
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

    /**
     * 'user' parameter must be email/username.
     *
     * @param user
     * @return
     */
    public UserEntity checkUser(String user) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("user", user);
        return super.fetchOneByNamedQuery("UserEntity.CHECK_USER", params);
    }
    
    public UserEntity userAuth(String user, String password){
        HashMap<String, Object> params = new HashMap<>();
        params.put("user", user);
        params.put("password", user);
        return super.fetchOneByNamedQuery("UserEntity.AUTH_BY_USER", params);
    }
    
    public UserEntity userAuthByFacebook(String facebookId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("facebookId", facebookId);
        return super.fetchOneByNamedQuery("UserEntity.AUTH_BY_FACEBOOK", params);
    }
    
    public UserEntity checkToken(String email, String token) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("token", token);
        return super.fetchOneByNamedQuery("UserEntity.GET_USER_BY_TOKEN", params);
    }
}
