/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.dao;

import com.sdm.core.hibernate.dao.RestDAO;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *
 * @author Htoonlin
 */
public class GeoIPCacheDAO extends RestDAO {

    private static final Logger LOG = Logger.getLogger(GeoIPCacheDAO.class.getName());

    public GeoIPCacheDAO() {
        super(0);
    }

    public GeoIPCacheDAO(Session session) {
        super(session, 0);
    }

    @Override
    protected boolean useVersion() {
        return false;
    }

    @Override
    protected boolean useLog() {
        return false;
    }

    @Override
    protected boolean useTimeStamp() {
        return false;
    }

    @Override
    protected boolean useSoftDelete() {
        return false;
    }

    @Override
    protected String getEntityName() {
        return "GeoIPCacheEntity";
    }
}
