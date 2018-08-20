/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.dao;

import com.sdm.core.Globalizer;
import com.sdm.core.hibernate.audit.IAuthListener;
import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.master.entity.TokenEntity;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    public TokenDAO(IAuthListener listener) {
        super(TokenEntity.class.getName(), listener);
        LOG.info("Start TokenDAO");
    }

    public TokenDAO(Session session, IAuthListener listener) {
        super(session, TokenEntity.class.getName(), listener);
    }

    public List<TokenEntity> getTokensByUserId(long userId){
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        return (List<TokenEntity>) super.fetchByNamedQuery("TokenEntity.GET_TOKEN_BY_USERID", params);
    }
    
    public void cleanToken(long userId) {
        List<TokenEntity> tokens = this.getTokensByUserId(userId);
        super.beginTransaction();
        for (TokenEntity token : tokens) {
            this.delete(token, false);
        }
        super.commitTransaction();
    }

    public TokenEntity getTokenByUserInfo(long userId, String deviceId, String deviceOS) throws SQLException {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("deviceId", deviceId);
        params.put("deviceOS", deviceOS);
        return super.fetchOneByNamedQuery("TokenEntity.CHECK_USER", params);
    }

    public TokenEntity getTokenByDeviceInfo(String deviceId, String deviceOS) throws SQLException {
        Map<String, Object> params = new HashMap<>();
        params.put("deviceId", deviceId);
        params.put("deviceOS", deviceOS);
        return super.fetchOneByNamedQuery("TokenEntity.CHECK_DEVICE", params);
    }

    public TokenEntity generateToken(TokenEntity token) throws SQLException {
        token.setTokenExpired(Globalizer.getTokenExpired());
        token.setLastLogin(new Date());
        
        TokenEntity existToken = this.getTokenByUserInfo(token.getUserId(), token.getDeviceId(), token.getDeviceOs());
        if (existToken == null) {
            token.setToken(UUID.randomUUID().toString());
            return super.insert(token, false);
        }else{
            token.setId(existToken.getId());
            return super.update(token, false);
        }

    }
}
