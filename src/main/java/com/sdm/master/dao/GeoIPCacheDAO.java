/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.dao;

import com.sdm.core.hibernate.dao.DefaultDAO;
import com.sdm.core.response.MapResponse;
import com.sdm.core.util.IGeoIPCache;
import com.sdm.master.entity.GeoIPCacheEntity;
import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *
 * @author Htoonlin
 */
public class GeoIPCacheDAO extends DefaultDAO<GeoIPCacheEntity> implements IGeoIPCache {

    private static final Logger LOG = Logger.getLogger(GeoIPCacheDAO.class.getName());

    
    public GeoIPCacheDAO() {
        super(GeoIPCacheEntity.class);
    }

    public GeoIPCacheDAO(Session session) {
        super(session, GeoIPCacheEntity.class);
    }

    @Override
    public MapResponse<String, Object> getInfoByIP(String ipAddress) {
        GeoIPCacheEntity entity = mainSession.get(this.entityClass, ipAddress);
        if (entity == null) {
            return null;
        }
        return entity.getResponse();
    }

    @Override
    public void saveInfo(Map<String, Object> info) throws Exception {
        try {
            GeoIPCacheEntity entity = new GeoIPCacheEntity(info);
            beginTransaction();
            mainSession.save(entity);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            LOG.error(e);
            throw e;
        }
    }

}
