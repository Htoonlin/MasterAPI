/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.util;

import com.sdm.core.Globalizer;
import com.sdm.master.dao.UserDAO;
import com.sdm.master.entity.UserEntity;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.log4j.Logger;

/**
 *
 * @author Htoonlin
 */
public class SessionListener implements HttpSessionListener {

    private static final Logger logger = Logger.getLogger(SessionListener.class.getName());

    private int sessionRecording(String prefix, HttpSessionEvent hse) {
        HttpSession session = hse.getSession();        
        int userId = 0;
        if (session.getAttribute(Globalizer.SESSION_USER_ID) != null) {
            userId = (int) session.getAttribute(Globalizer.SESSION_USER_ID);
        }
        logger.debug(prefix + "{userId:" + userId + ", SessionId:" + session.getId() + "}");
        return userId;
    }

    @Override
    public void sessionCreated(HttpSessionEvent hse) {
        sessionRecording("Create new session.", hse);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent hse) {
        int userId = sessionRecording("Destroyed a session.", hse);
        UserDAO userDAO = new UserDAO(hse.getSession());
        try {
            UserEntity user = userDAO.fetchById(userId);
            if (user != null) {
                user.setOnline(false);
                userDAO.update(user, true);
            }
        } catch (Exception e) {
            logger.error(e);
        }        
    }

}
