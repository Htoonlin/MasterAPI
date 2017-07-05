/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.hibernate;

import com.sdm.core.hibernate.entity.AuditEntity;
import java.util.Date;
import org.hibernate.envers.RevisionListener;

/**
 *
 * @author Htoonlin
 */
public class AuditListener implements RevisionListener {

    @Override
    public void newRevision(Object o) {
        AuditEntity entity = (AuditEntity) o;
        entity.setAuditAt(new Date());
        entity.setUserId(0);
    }

}
