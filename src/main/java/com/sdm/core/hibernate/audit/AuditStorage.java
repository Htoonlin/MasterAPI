package com.sdm.core.hibernate.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sdm.core.Globalizer;
import com.sdm.core.hibernate.entity.AuthInfo;
import org.apache.log4j.Logger;

public final class AuditStorage {

    private static final Logger LOG = Logger.getLogger(AuditStorage.class.getName());

    public static AuditStorage INSTANCE = new AuditStorage();

    private static final ThreadLocal<AuthInfo> storage = new ThreadLocal<>();

    public void set(AuthInfo value) {
        try {
            LOG.info("Set Auth " + Globalizer.jsonMapper().writeValueAsString(value));
            storage.set(value);
        } catch (JsonProcessingException ex) {
            LOG.error(ex);
        }
    }

    public void clean() {
        LOG.info("Clean Auth Info from local storage.");
        storage.remove();
    }

    public AuthInfo get() {
        AuthInfo auth = storage.get();
        if(auth == null){
            return new AuthInfo(0, "public-token", "no_device");
        }
        return auth;
    }
}
