/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.util;

import com.sdm.core.Globalizer;
import com.sdm.core.Setting;
import com.sdm.core.di.IAccessManager;
import com.sdm.master.dao.PermissionDAO;
import com.sdm.master.dao.TokenDAO;
import com.sdm.master.dao.UserDAO;
import com.sdm.master.entity.RoleEntity;
import com.sdm.master.entity.TokenEntity;
import com.sdm.master.entity.UserEntity;
import io.jsonwebtoken.Claims;
import java.lang.reflect.Method;
import java.util.Date;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author Htoonlin
 */
public class AccessManager implements IAccessManager {

    private static final Logger logger = Logger.getLogger(AccessManager.class.getName());

    private TokenEntity currentToken;

    @Inject
    private HttpSession httpSession;

    @Override
    public boolean validateToken(Claims request) {
        if (request == null) {
            return false;
        }

        if (request.getId().length() != 36) {
            return false;
        }

        TokenDAO tokenDao = new TokenDAO(httpSession);
        currentToken = tokenDao.fetchById(request.getId());
        if (currentToken == null) {
            return false;
        }

        if (currentToken.getTokenExpired().before(new Date())) {
            return false;
        }

        String token = request.getId();
        String deviceId = request.get("device_id").toString();
        String deviceOS = request.get("device_os").toString();
        long userId = Long.parseLong(request.getSubject().substring(Globalizer.AUTH_SUBJECT_PREFIX.length()).trim());

        boolean result = (currentToken.getToken().equalsIgnoreCase(token)
                && currentToken.getDeviceId().equalsIgnoreCase(deviceId)
                && currentToken.getDeviceOs().equalsIgnoreCase(deviceOS)
                && currentToken.getUserId() == userId);

        if (result) {
            currentToken.setLastLogin(new Date());
            currentToken.setTokenExpired(Globalizer.getTokenExpired());
            try {
                tokenDao.update(currentToken, true);
            } catch (Exception e) {
                logger.error(e);
                result = false;
            }
        }
        return result;
    }

    @Override
    public boolean checkPermission(Claims request, Method method, String httpMethod) {
        long authUserId = Long.parseLong(request.getSubject().substring(Globalizer.AUTH_SUBJECT_PREFIX.length()).trim());

        if (currentToken == null || currentToken.getUserId() != authUserId) {
            return false;
        }
        //Check User Status
        UserDAO userDAO = new UserDAO(httpSession);
        UserEntity user = userDAO.fetchById(authUserId);
        if (user == null || user.getStatus() != UserEntity.ACTIVE || user.getDeletedAt() != null) {
            return false;
        }

        //Skip Permission for ROOT USER
        if (((long) user.getId()) == Setting.getInstance().ROOT_ID) {
            return true;
        }

        //Skip Permission for User Methods => user
        RolesAllowed roles = method.getAnnotation(RolesAllowed.class);
        if (roles != null) {
            for (String role : roles.value()) {
                if (role.equalsIgnoreCase("user")) {
                    return true;
                }
            }
        }

        //Check Permission by User Roles
        String className = method.getDeclaringClass().getName();
        PermissionDAO permissionDAO = new PermissionDAO(userDAO.getSession(), httpSession);
        boolean permission = false;
        for (RoleEntity role : user.getRoles()) {
            permission = permissionDAO.checkRole((int) role.getId(), className, method.getName(), httpMethod);
            if (permission) {
                break;
            }
        }
        if (!permission) {
            return false;
        }

        //Set User is online
        user.setOnline(true);
        try {
            userDAO.update(user, true);
        } catch (Exception e) {
            logger.error(e);
            return false;
        }

        return true;
    }
}
