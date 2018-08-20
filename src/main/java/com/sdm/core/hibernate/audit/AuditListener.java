package com.sdm.core.hibernate.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sdm.core.Globalizer;
import com.sdm.core.hibernate.entity.AuditEntity;
import com.sdm.core.hibernate.entity.AuthInfo;
import org.apache.log4j.Logger;
import org.hibernate.envers.RevisionListener;

public class AuditListener implements RevisionListener {

    private static final Logger LOG = Logger.getLogger(AuditListener.class);

    @Override
    public void newRevision(Object entity) {
        AuditEntity auditEntity = (AuditEntity) entity;
        AuthInfo authInfo = AuditStorage.INSTANCE.get();
        auditEntity.setUserId(authInfo.getUserId());
        auditEntity.setAuthToken(authInfo.getAuthToken());
        auditEntity.setDevice(authInfo.getDevice());
        
        try {
            LOG.info("Successfully created Audit: " + Globalizer.jsonMapper().writeValueAsString(auditEntity));
        } catch (JsonProcessingException ex) {
            LOG.info("Successfull created.");
        }
    }
}
