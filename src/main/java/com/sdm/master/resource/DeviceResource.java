/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import com.sdm.core.Globalizer;
import com.sdm.core.resource.DefaultResource;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.master.dao.TokenDAO;
import com.sdm.master.dao.UserDAO;
import com.sdm.master.entity.TokenEntity;
import com.sdm.master.entity.UserEntity;
import com.sdm.master.request.DeviceRequest;
import io.grpc.netty.shaded.io.netty.util.internal.StringUtil;
import java.sql.SQLException;
import java.util.Date;
import java.util.Random;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author htoonlin
 */
@Path("devices")
public class DeviceResource extends DefaultResource {

    private static final int MAX_USERNAME = 32;
    private static final int MIN_USERNAME = 16;

    private void setExtras(DeviceRequest request, UserEntity user) {
        if (!StringUtil.isNullOrEmpty(request.getBrand())) {
            user.addExtra("brand", request.getBrand());
        }

        if (!StringUtil.isNullOrEmpty(request.getCarrier())) {
            user.addExtra("carrier", request.getCarrier());
        }

        if (!StringUtil.isNullOrEmpty(request.getManufacture())) {
            user.addExtra("manufacture", request.getManufacture());
        }
    }

    @PermitAll
    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse deviceRegister(@Valid DeviceRequest request) throws SQLException {
        Random rnd = new Random();
        int size = rnd.nextInt((MAX_USERNAME - MIN_USERNAME) + 1) + MIN_USERNAME;

        String userName = request.getOs() + "_"
                + Globalizer.getDateString("yyyyMMddHHmmss", new Date()) + "_"
                + Globalizer.generateToken("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 8);

        String displayName = request.getName();
        String rawPassword = Globalizer.generateToken(size);
        String password = com.sdm.core.util.SecurityManager.hashString(rawPassword);

        UserDAO userDAO = new UserDAO(this);
        TokenDAO tokenDAO = new TokenDAO(userDAO.getSession(), this);

        //Check Device Registration
        TokenEntity existToken = tokenDAO.getTokenByDeviceInfo(request.getUniqueId(), request.getOs());
        if (existToken != null) {
            userDAO.beginTransaction();
            try {
                UserEntity existUser = userDAO.fetchById(existToken.getUserId());
                // Generate and store JWT
                existToken.setFirebaseToken(request.getFirebaseToken());
                tokenDAO.generateToken(existToken);

                existUser.setDisplayName(displayName);
                this.setExtras(request, existUser);
                userDAO.update(existUser, false);

                userDAO.commitTransaction();
                
                existUser.setCurrentToken(existToken.generateJWT(request.getUserAgent()));
                return new DefaultResponse(201, ResponseType.SUCCESS, existUser);
            } catch (Exception ex) {
                userDAO.rollbackTransaction();
                throw ex;
            }
        }

        UserEntity user = new UserEntity(userName, displayName, password, UserEntity.ACTIVE);
        this.setExtras(request, user);

        userDAO.beginTransaction();
        try {
            userDAO.insert(user, false);

            TokenEntity token = new TokenEntity();
            token.setUserId(user.getId());
            token.setDeviceId(request.getUniqueId());
            token.setDeviceOs(request.getOs());
            token.setFirebaseToken(request.getFirebaseToken());
            
            TokenEntity authToken = tokenDAO.generateToken(token);
            
            // Generate and store JWT
            user.setCurrentToken(authToken.generateJWT(request.getUserAgent()));

            userDAO.commitTransaction();
            this.modifiedResource();

            return new DefaultResponse(201, ResponseType.SUCCESS, user);
        } catch (SQLException ex) {
            userDAO.rollbackTransaction();
            getLogger().error(ex);
            throw ex;
        }
    }
}
