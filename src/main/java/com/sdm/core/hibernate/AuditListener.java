/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.hibernate;

import com.sdm.core.Globalizer;
import com.sdm.core.hibernate.entity.AuditEntity;
import org.apache.log4j.Logger;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import org.hibernate.envers.RevisionListener;

/**
 *
 * @author Htoonlin
 */
public class AuditListener implements RevisionListener {

    private static final Logger LOG = Logger.getLogger(AuditListener.class.getName());

    @Inject
    HttpSession session;

    @Override
    public void newRevision(Object o) {
        try {
            long userId = (long) this.session.getAttribute(Globalizer.SESSION_USER_ID);
            AuditEntity entity = (AuditEntity) o;
            entity.setUserId(userId);
        } catch (Exception e) {
            LOG.error("There is no session. <" + e.getLocalizedMessage() + ">");
        }
    }

}
