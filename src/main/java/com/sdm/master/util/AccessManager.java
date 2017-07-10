/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.util;

import java.lang.reflect.Method;
import java.util.Date;

import javax.annotation.security.RolesAllowed;

import org.apache.log4j.Logger;

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

/**
 *
 * @author Htoonlin
 */
public class AccessManager implements IAccessManager {

	private static final Logger LOG = Logger.getLogger(AccessManager.class.getName());

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
		int userId = Integer.parseInt(request.getSubject().substring(Globalizer.AUTH_SUBJECT_PREFIX.length()).trim());

		TokenDAO tokenDao = new TokenDAO(userId);
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
				&& currentToken.getDeviceOs().equalsIgnoreCase(deviceOS) && currentToken.getUserId() == userId);

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
	public boolean checkPermission(Claims request, Method method, String httpMethod) {
		int authUserId = Integer
				.parseInt(request.getSubject().substring(Globalizer.AUTH_SUBJECT_PREFIX.length()).trim());

		if (currentToken == null || currentToken.getUserId() != authUserId) {
			return false;
		}

		// Check User Status
		UserDAO userDAO = new UserDAO(authUserId);
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

		// Set User is online
		user.setOnline(true);
		try {
			userDAO.update(user, true);
		} catch (Exception e) {
			LOG.error(e);
			return false;
		}

		// Skip Permission for ROOT USER
		if ((user.getId()) == Setting.getInstance().ROOT_ID) {
			return true;
		}

		// Skip Permission for User Methods => user
		RolesAllowed roles = method.getAnnotation(RolesAllowed.class);
		if (roles != null) {
			for (String role : roles.value()) {
				if (role.equalsIgnoreCase("user")) {
					return true;
				}
			}
		}

		// Check Permission by User Roles
		String className = method.getDeclaringClass().getName();
		PermissionDAO permissionDAO = new PermissionDAO(userDAO.getSession(), authUserId);
		boolean permission = false;
		for (RoleEntity role : user.getRoles()) {
			permission = permissionDAO.checkRole(role.getId(), className, method.getName(), httpMethod);
			if (permission) {
				break;
			}
		}
		if (!permission) {
			return false;
		}

		return true;
	}
}
