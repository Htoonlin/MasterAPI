package com.sdm.core.hibernate.audit;

import org.apache.log4j.Logger;
import org.hibernate.envers.RevisionListener;

import com.sdm.core.hibernate.entity.AuditEntity;

public class AuditListener implements RevisionListener {

	private static final Logger LOG = Logger.getLogger(AuditListener.class);

	@Override
	public void newRevision(Object entity) {
		AuditEntity auditEntity = (AuditEntity) entity;
		int userId = AuditStorage.INSTANCE.get();
		auditEntity.setUserId(userId);
		LOG.debug("Successfully created the audit log by <" + userId + ">.");
	}
}
