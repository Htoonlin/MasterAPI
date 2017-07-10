package com.sdm.core.hibernate.audit;

import org.apache.log4j.Logger;

public final class AuditStorage {
	private static final Logger LOG = Logger.getLogger(AuditStorage.class.getName());

	public static AuditStorage INSTANCE = new AuditStorage();

	private static final ThreadLocal<Integer> storage = new ThreadLocal<>();

	public void set(Integer value) {
		LOG.info("Set User ID " + value);
		storage.set(value);
	}

	public void clean() {
		LOG.info("Clean User ID from local storage.");
		storage.remove();
	}

	public Integer get() {
		return storage.get();
	}
}
