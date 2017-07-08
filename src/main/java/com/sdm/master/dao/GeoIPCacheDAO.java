/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.master.entity.GeoIPCacheEntity;

/**
 *
 * @author Htoonlin
 */
public class GeoIPCacheDAO extends RestDAO {

    private static final Logger LOG = Logger.getLogger(GeoIPCacheDAO.class.getName());

    public GeoIPCacheDAO() {
        super(GeoIPCacheEntity.class.getName(), 0);
        LOG.info("Start DAO");
    }

    public GeoIPCacheDAO(Session session) {
        super(session, GeoIPCacheEntity.class.getName(), 0);
    }
}
