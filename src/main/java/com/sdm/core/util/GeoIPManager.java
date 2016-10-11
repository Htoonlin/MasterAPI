/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util;

import com.sdm.core.Globalizer;
import com.sdm.core.response.MapResponse;
import java.io.IOException;
import org.apache.log4j.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Htoonlin
 */
public class GeoIPManager {

    private static final Logger LOG = Logger.getLogger(GeoIPManager.class.getName());

    private static String callAPI = "http://ip-api.com/json/";

    private IGeoIPCache cacheDAO;

    public GeoIPManager(IGeoIPCache cacheDAO) {
        this.cacheDAO = cacheDAO;
    }

    public GeoIPManager() {

    }
    
    private MapResponse<String, Object> requestInfo(String ipAddress) throws IOException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(callAPI + ipAddress);
        Response response = target.request().accept(MediaType.APPLICATION_JSON).get(Response.class);
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            String responseString = response.readEntity(String.class);
            return Globalizer.jsonMapper().readValue(responseString, MapResponse.class);
        }
        return null;
    }

    public MapResponse<String, Object> lookupInfo(String ipAddress) {
        try {
            if (cacheDAO != null) {
                MapResponse<String, Object> info = cacheDAO.getInfoByIP(ipAddress);
                if (info == null) {
                    info = requestInfo(ipAddress);

                    if (info != null) {
                        cacheDAO.saveInfo(info);
                    }
                }

                return info;
            } else {
                return requestInfo(ipAddress);
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }

}
