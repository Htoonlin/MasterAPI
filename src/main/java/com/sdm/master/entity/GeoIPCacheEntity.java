/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Htoonlin
 */
@Entity(name = "GeoIPCacheEntity")
@Table(name = "tbl_geoip_cache")
public class GeoIPCacheEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3720713983743379695L;

    @Id
    @Column(name = "ipAddress", columnDefinition = "varchar(50)", nullable = false, unique = true)
    private String ipAddress;

    @Column(name = "countryCode", columnDefinition = "char(2)", nullable = false)
    private String countryCode;

    @Column(name = "region", columnDefinition = "varchar(20)", nullable = true)
    private String region;

    @Column(name = "regionName", columnDefinition = "varchar(500)", nullable = true)
    private String regionName;

    @Column(name = "zip", columnDefinition = "varchar(50)", nullable = true)
    private String zip;

    @Column(name = "city", columnDefinition = "varchar(500)", nullable = true)
    private String city;

    @Column(name = "lat", columnDefinition = "decimal(10, 8)", nullable = true)
    private double lat;

    @Column(name = "lon", columnDefinition = "decimal(11, 8)", nullable = true)
    private double lon;

    @Column(name = "timezone", columnDefinition = "varchar(255)", nullable = true)
    private String timezone;

    @Column(name = "isp", columnDefinition = "varchar(500)", nullable = true)
    private String isp;

    @Column(name = "association", columnDefinition = "varchar(500)", nullable = true)
    private String as;

    public GeoIPCacheEntity() {

    }

    public GeoIPCacheEntity(Map<String, Object> data) {
        if (data.get("status").toString().equalsIgnoreCase("success")) {
            this.ipAddress = data.get("query").toString();
            this.countryCode = data.get("countryCode").toString();
            this.region = data.get("region").toString();
            this.regionName = data.get("regionName").toString();
            this.zip = data.get("zip").toString();
            this.city = data.get("city").toString();
            this.lat = (double) data.get("lat");
            this.lon = (double) data.get("lon");
            this.timezone = data.get("timezone").toString();
            this.isp = data.get("isp").toString();
            this.as = data.get("as").toString();
        }
    }

    public HashMap<String, Object> getResponse() {
        HashMap<String, Object> response = new HashMap<>();
        response.put("status", "cache");
        response.put("query", this.ipAddress);
        response.put("country_code", this.countryCode);
        response.put("country", getCountryName());
        response.put("region", this.region);
        response.put("region_name", this.regionName);
        response.put("zip", this.zip);
        response.put("city", this.city);
        response.put("lat", this.lat);
        response.put("lon", this.lon);
        response.put("timezone", this.timezone);
        response.put("isp", this.isp);
        response.put("as", this.as);

        return response;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getCountryName() {
        Locale locale = new Locale("", this.countryCode);
        return locale.getDisplayCountry();
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
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

    public String getAs() {
        return as;
    }

    public void setAs(String as) {
        this.as = as;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        GeoIPCacheEntity other = (GeoIPCacheEntity) obj;
        if (ipAddress == null) {
            if (other.ipAddress != null) {
                return false;
            }
        } else if (!ipAddress.equals(other.ipAddress)) {
            return false;
        }
        return true;
    }

}
