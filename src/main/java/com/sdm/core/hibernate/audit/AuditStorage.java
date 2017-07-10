package com.sdm.core.hibernate.audit;

import org.apache.log4j.Logger;


public class AuditStorage{
	private static final Logger LOG = Logger.getLogger(AuditStorage.class.getName());
			
	private static AuditStorage instance;
	
	public static synchronized AuditStorage getInstance() {
		if(instance == null) {
			instance = new AuditStorage();
			LOG.info("Create new audit storage.");
		}
		return instance;
	}
	
	private static final ThreadLocal<Long> storage = new ThreadLocal<>();
	
	public void set(Long value) {
		storage.set(value);
	}
	
	public void clean() {
		storage.remove();
	}
	
	public Long get() {
		return storage.get();
	}
}
