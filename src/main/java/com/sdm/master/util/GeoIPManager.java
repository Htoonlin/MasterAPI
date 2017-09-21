/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.sdm.core.Globalizer;
import com.sdm.master.dao.GeoIPCacheDAO;

/**
 *
 * @author Htoonlin
 */
public class GeoIPManager {

    private static final Logger LOG = Logger.getLogger(GeoIPManager.class.getName());

    private static String callAPI = "http://ip-api.com/json/";

    private GeoIPCacheDAO cacheDAO;

    public GeoIPManager() {
        this.cacheDAO = new GeoIPCacheDAO();
    }

    private HashMap<String, Object> requestInfo(String ipAddress) throws IOException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(callAPI + ipAddress);
        Response response = target.request().accept(MediaType.APPLICATION_JSON).get(Response.class);
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            String responseString = response.readEntity(String.class);
            return Globalizer.jsonMapper().readValue(responseString, HashMap.class);
        }
        return null;
    }

    public HashMap<String, Object> lookupInfo(String ipAddress) {
        try {
            if (cacheDAO != null) {
                Map<String, Object> info = cacheDAO.fetchById(ipAddress);
                if (info == null) {
                    info = requestInfo(ipAddress);

                    if (info != null) {
                        cacheDAO.insert(info, true);
                    }
                }
                return new HashMap<>(info);
            } else {
                return requestInfo(ipAddress);
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }

}
