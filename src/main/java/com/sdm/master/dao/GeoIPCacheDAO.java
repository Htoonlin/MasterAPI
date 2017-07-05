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
    private static final String ENTITY = "GeoIPCacheEntity";

    public GeoIPCacheDAO() {
        super(ENTITY, 0);
    }

    public GeoIPCacheDAO(Session session) {
        super(session, ENTITY, 0);
    }
}
