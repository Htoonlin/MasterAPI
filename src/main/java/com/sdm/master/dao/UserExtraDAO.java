/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.dao;

import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.master.entity.UserExtraEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *
 * @author YOETHA
 */
public class UserExtraDAO extends RestDAO {

    private static final String EXTRA_BYUSERID = " FROM UserExtraEntity U WHERE U.userId=:id";

    private static final Logger LOG = Logger.getLogger(UserDAO.class.getName());

    public UserExtraDAO(int userId) {
        super(UserExtraEntity.class.getName(), userId);
        LOG.info("Start DAO");
    }

    public UserExtraDAO(Session session, int userId) {
        super(session, UserExtraEntity.class.getName(), userId);
    }

    public List<UserExtraEntity> getUserExtraByUser(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return (List<UserExtraEntity>) this.fetch(EXTRA_BYUSERID, params);
    }
}
