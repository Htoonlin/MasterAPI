/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sdm.core.Globalizer;
import com.sdm.core.response.IResponseContent;
import com.sdm.core.response.ResponseType;
import java.io.Serializable;
import org.apache.log4j.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

/**
 *
 * @author Htoonlin
 */
public class GeoIPManager implements Serializable, IResponseContent {

    private static final Logger LOG = Logger.getLogger(GeoIPManager.class.getName());

    private static final long serialVersionUID = 1L;
    private static String callAPI = "http://ip-api.com/json/";

    private String country;
    private String countryCode;
    private String region;
    private String regionName;
    private String city;
    private String zip;
    private Double lat;
    private Double lon;
    private String timezone;
    private String isp;
    private String org;
    private String as;
    private String query;

    public GeoIPManager(String ipAddress) {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(callAPI + ipAddress);
            Response response = target.request().accept(MediaType.APPLICATION_JSON).get(Response.class);
            if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                JSONObject responseObject = new JSONObject(response.readEntity(String.class));
                if (responseObject.has("country")) {
                    this.country = responseObject.getString("country");
                }

                if (responseObject.has("countryCode")) {
                    this.countryCode = responseObject.getString("countryCode");
                }

                if (responseObject.has("region")) {
                    this.region = responseObject.getString("region");
                }

                if (responseObject.has("regionName")) {
                    this.regionName = responseObject.getString("regionName");
                }

                if (responseObject.has("city")) {
                    this.city = responseObject.getString("city");
                }

                if (responseObject.has("zip")) {
                    this.zip = responseObject.getString("zip");
                }

                if (responseObject.has("lat")) {
                    this.lat = responseObject.getDouble("lat");
                }

                if (responseObject.has("lon")) {
                    this.lon = responseObject.getDouble("lon");
                }

                if (responseObject.has("timezone")) {
                    this.timezone = responseObject.getString("timezone");
                }

                if (responseObject.has("isp")) {
                    this.isp = responseObject.getString("isp");
                }

                if (responseObject.has("org")) {
                    this.org = responseObject.getString("org");
                }

                if (responseObject.has("as")) {
                    this.as = responseObject.getString("as");
                }

                if (responseObject.has("query")) {
                    this.query = responseObject.getString("query");
                }
            }
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getRegion() {
        return region;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getCity() {
        return city;
    }

    public String getZip() {
        return zip;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getIsp() {
        return isp;
    }

    public String getOrg() {
        return org;
    }

    public String getAs() {
        return as;
    }

    public String getQuery() {
        return query;
    }

    @Override
    public int getResponseCode() {
        return 200;
    }

    @Override
    public ResponseType getResponseStatus() {
        return ResponseType.INFO;
    }

}
