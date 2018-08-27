package com.sdm.core.hibernate.audit;

import com.sdm.core.hibernate.entity.AuthInfo;
import org.apache.log4j.Logger;

public final class AuditStorage {

    private static final Logger LOG = Logger.getLogger(AuditStorage.class.getName());

    public static AuditStorage INSTANCE = new AuditStorage();

    private static final ThreadLocal<AuthInfo> storage = new ThreadLocal<>();

    public void set(AuthInfo value) {
        if(value != null){
            LOG.info("Set Auth => " + value.getAuthToken());
            storage.set(value);
        }
    }

    public void clean() {
        AuthInfo value = this.get();
        LOG.info("Set Auth => " + value.getAuthToken());
        storage.remove();
    }

    public AuthInfo get() {
        AuthInfo auth = storage.get();
        if (auth == null) {
            return new AuthInfo(0, "public-token", "no_device");
        }
        return auth;
    }
}
