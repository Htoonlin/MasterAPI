/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.util;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import com.sdm.core.Globalizer;
import com.sdm.master.dao.UserDAO;
import com.sdm.master.entity.UserEntity;

/**
 *
 * @author Htoonlin
 */
@WebListener
public class SessionListener implements HttpSessionListener {

	private static final Logger LOG = Logger.getLogger(SessionListener.class.getName());

	private int sessionRecording(String prefix, HttpSessionEvent hse) {
		HttpSession session = hse.getSession();
		int userId = 0;
		if (session.getAttribute(Globalizer.SESSION_USER_ID) != null) {
			userId = (int) session.getAttribute(Globalizer.SESSION_USER_ID);
		}
		LOG.info(prefix + "{userId:" + userId + ", SessionId:" + session.getId() + "}");
		return userId;
	}

	@Override
	public void sessionCreated(HttpSessionEvent hse) {
		sessionRecording("Create new session.", hse);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent hse) {
		int userId = sessionRecording("Destroyed a session.", hse);
		UserDAO userDAO = new UserDAO(userId);
		try {
			UserEntity user = userDAO.fetchById(userId);
			if (user != null) {
				user.setOnline(false);
				userDAO.update(user, true);
			}
		} catch (Exception e) {
			LOG.error(e);
		}
	}

}
