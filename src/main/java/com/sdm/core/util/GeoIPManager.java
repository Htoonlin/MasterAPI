/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sdm.core.Globalizer;
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
public class GeoIPManager implements Serializable {

    private static final Logger LOG = Logger.getLogger(GeoIPManager.class.getName());

    private static final long serialVersionUID = 1L;
    private static String callAPI = "http://ip-api.com/json/";

    private String country;
    private String countryCode;
    private String region;
    private String regionName;
    private String city;
    private String zip;
    private String lat;
    private String lon;
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
                    this.country = responseObject.getString("countryCode");
                }

                if (responseObject.has("region")) {
                    this.country = responseObject.getString("region");
                }

                if (responseObject.has("regionName")) {
                    this.country = responseObject.getString("regionName");
                }

                if (responseObject.has("city")) {
                    this.country = responseObject.getString("city");
                }

                if (responseObject.has("zip")) {
                    this.country = responseObject.getString("zip");
                }

                if (responseObject.has("lat")) {
                    this.country = responseObject.getString("lat");
                }

                if (responseObject.has("lon")) {
                    this.country = responseObject.getString("lon");
                }

                if (responseObject.has("timezone")) {
                    this.country = responseObject.getString("timezone");
                }

                if (responseObject.has("isp")) {
                    this.country = responseObject.getString("isp");
                }

                if (responseObject.has("org")) {
                    this.country = responseObject.getString("org");
                }

                if (responseObject.has("as")) {
                    this.country = responseObject.getString("as");
                }

                if (responseObject.has("query")) {
                    this.country = responseObject.getString("query");
                }
            }
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getAs() {
        return as;
    }

    public void setAs(String as) {
        this.as = as;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
