/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.util;

import com.sdm.core.Globalizer;
import com.sdm.core.Setting;
import com.sdm.core.di.IAccessManager;
import com.sdm.core.request.AuthorizeRequest;
import com.sdm.master.dao.PermissionDAO;
import com.sdm.master.dao.TokenDAO;
import com.sdm.master.dao.UserDAO;
import com.sdm.master.entity.RoleEntity;
import com.sdm.master.entity.TokenEntity;
import com.sdm.master.entity.UserEntity;
import java.lang.reflect.Method;
import java.util.Date;
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
    public boolean validateToken(AuthorizeRequest request) {
        if (!request.isValid()) {
            logger.error(request.getErrors());
            return false;
        }

        if (request.getToken() == null || request.getToken().length() != 36) {
            return false;
        }
        TokenDAO tokenDao = new TokenDAO(httpSession);
        String token = request.getToken();
        currentToken = tokenDao.fetchById(token);
        if (currentToken == null) {
            return false;
        }

        if (currentToken.getTokenExpired().before(new Date())) {
            return false;
        }
        boolean result = (currentToken.getDeviceId().equalsIgnoreCase(request.getDeviceId())
                && currentToken.getDeviceOs().equalsIgnoreCase(request.getDeviceOS())
                && currentToken.getUserId() == request.getUserId());

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
    public boolean checkPermission(AuthorizeRequest request, Method method, String httpMethod) {
        if (currentToken == null || currentToken.getUserId() != request.getUserId()) {
            return false;
        }
        //Check User Status
        UserDAO userDAO = new UserDAO(httpSession);
        UserEntity user = userDAO.fetchById(request.getUserId());
        if (user == null || user.getStatus() != UserEntity.ACTIVE || user.getDeletedAt() != null) {
            return false;
        }

        //Skip Permission for ROOT USER
        if (((int) user.getId()) == Setting.getInstance().ROOT_ID) {
            return true;
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
