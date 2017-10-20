/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.dao;

import com.sdm.core.Globalizer;
import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.master.entity.TokenEntity;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *
 * @author Htoonlin
 */
public class TokenDAO extends RestDAO {

    private static final Logger LOG = Logger.getLogger(TokenDAO.class.getName());

    public TokenDAO(int userId) {
        super(TokenEntity.class.getName(), userId);
        LOG.info("Start TokenDAO");
    }

    public TokenDAO(Session session, int userId) {
        super(session, TokenEntity.class.getName(), userId);
    }

    @Deprecated
    public void cleanToken(int userId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        super.executeByNamedQuery("TokenEntity.CLEAN_TOKEN", params);
    }

    @Deprecated
    public TokenEntity getTokenByUserInfo(int userId, String deviceId, String deviceOS) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("deviceId", deviceId);
        params.put("deviceOS", deviceOS);
        return super.fetchOneByNamedQuery("TokenEntity.CHECK_USER", params);
    }

    @Deprecated
    public void extendToken(String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("expired", Globalizer.getTokenExpired());
        params.put("token", token);
        super.executeByNamedQuery("TokenEntity.UPDATE_EXPIRED_BY_TOKEN", params);
    }

    public TokenEntity generateToken(int userId, String deviceId, String deviceOS) throws Exception {
        boolean isNew = false;
        TokenEntity token = this.getTokenByUserInfo(userId, deviceId, deviceOS);
        if (token == null) {
            isNew = true;
            token = new TokenEntity();
            token.setUserId(userId);
            token.setDeviceId(deviceId);
            token.setDeviceOs(deviceOS);
            token.setToken(UUID.randomUUID().toString());
            token.setTokenExpired(Globalizer.getTokenExpired());
        }

        token.setTokenExpired(Globalizer.getTokenExpired());
        token.setLastLogin(new Date());
        if (isNew) {
            return super.insert(token, false);
        } else {
            return super.update(token, false);
        }
    }
}
