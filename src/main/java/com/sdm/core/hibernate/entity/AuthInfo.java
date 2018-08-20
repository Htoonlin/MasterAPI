/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.hibernate.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author htoonlin
 */
@MappedSuperclass
public class AuthInfo implements Serializable {

    @Column(name = "id", nullable = false, columnDefinition = "MEDIUMINT UNSIGNED")
    private long userId;

    @Column(name = "token", nullable = false, length = 36)
    private String authToken;

    @Column(name = "device", nullable = false, length = 500)
    private String device;

    public AuthInfo() {
    }

    public AuthInfo(long userId, String authToken, String device) {
        this.userId = userId;
        this.authToken = authToken;
        this.device = device;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

}
