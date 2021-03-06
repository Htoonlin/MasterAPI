/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.util;

import com.sdm.Constants;
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
import org.apache.log4j.Logger;

/**
 *
 * @author Htoonlin
 */
public class AccessService implements IAccessManager {

    private static final Logger LOG = Logger.getLogger(AccessService.class.getName());

    private TokenEntity currentToken;

    @Override
    public boolean validateToken(Claims request) {
        if (request == null) {
            return false;
        }

        if (request.getId().length() != 36) {
            return false;
        }

        String token = request.getId();
        String deviceId = request.get("device_id").toString();
        String deviceOS = request.get("device_os").toString();
        long userId = Long.parseLong(request.getSubject().substring(Constants.USER_PREFIX.length()).trim());

        TokenDAO tokenDao = new TokenDAO(null);
        try {
            currentToken = tokenDao.fetchById(request.getId());
        } catch (Exception ex) {
            LOG.error(ex);
            return false;
        }

        if (currentToken == null) {
            return false;
        }

        if (currentToken.getTokenExpired().before(new Date())) {
            return false;
        }

        boolean result = (currentToken.getToken().equalsIgnoreCase(token)
                && currentToken.getDeviceId().equalsIgnoreCase(deviceId)
                && currentToken.getDeviceOs().equalsIgnoreCase(deviceOS)
                && currentToken.getUserId() == userId);

        if (result) {
            currentToken.setLastLogin(new Date());
            try {
                tokenDao.update(currentToken, true);
            } catch (Exception e) {
                LOG.error(e);
                result = false;
            }
        }
        return result;
    }

    @Override
    public boolean checkPermission(Claims request, Method method, String httpMethod, Class<?> resourceClass) {
        long authUserId = Long.parseLong(request.getSubject().substring(Constants.USER_PREFIX.length()).trim());

        if (currentToken == null || currentToken.getUserId() != authUserId) {
            return false;
        }

        // Check User Status
        UserDAO userDAO = new UserDAO(null);
        UserEntity user;
        try {
            user = userDAO.fetchById(authUserId);
        } catch (Exception ex) {
            LOG.error(ex);
            return false;
        }

        if (user == null || user.getStatus() != UserEntity.ACTIVE) {
            return false;
        }

        // Skip Permission for ROOT USER
        String rootIds = Setting.getInstance().get(Setting.ROOT_ID, "1");
        String[] roots = rootIds.split("/([^,|\\s]+)/g");
        for (String root : roots) {
            if (Long.parseLong(root) == user.getId()) {
                return true;
            }
        }

        // Check Permission by User Roles
        //String className = method.getDeclaringClass().getName();
        String className = resourceClass.getName();
        PermissionDAO permissionDAO = new PermissionDAO(userDAO.getSession(), null);
        boolean permission = false;
        for (RoleEntity role : user.getRoles()) {
            permission = permissionDAO.checkRole(role.getId(), className, method.getName(), httpMethod);
            if (permission) {
                break;
            }
        }

        return true;
    }
}
