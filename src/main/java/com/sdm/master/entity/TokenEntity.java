package com.sdm.master.entity;
// Generated 04-Mar-2016 00:48:07 by Hibernate Tools 4.3.1

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sdm.core.hibernate.entity.RestEntity;
import com.sdm.core.hibernate.entity.UIStructure;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TokenEntity generated by hbm2java
 */
@Entity
@Table(name = "tbl_user_token")
@JsonPropertyOrder(value = {"token", "deviceId", "device_os", "token_expired"})
public class TokenEntity extends RestEntity<String> implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @UIStructure(order = 0, label = "Token", readOnly = true)
    @Column(name = "token", unique = true, nullable = false, length = 36)
    private String token;

    @UIStructure(order = 1, label = "User ID")
    @Column(name = "userId", columnDefinition = "INT UNSIGNED", nullable = false)
    private long userId;

    @UIStructure(order = 2, label = "Device-ID")
    @Column(name = "deviceId", nullable = false, length = 255)
    private String deviceId;

    @UIStructure(order = 3, label = "Device-OS")
    @Column(name = "deviceOS", nullable = false, length = 10)
    private String deviceOs;

    @Temporal(TemporalType.TIMESTAMP)
    @UIStructure(order = 4, label = "Lasted Login")
    @Column(name = "lastedLogin", nullable = false, length = 19, updatable = true)
    private Date lastLogin;

    @Temporal(TemporalType.TIMESTAMP)
    @UIStructure(order = 5, label = "Expired")
    @Column(name = "tokenExpired", nullable = false, length = 19)
    private Date tokenExpired;

    public TokenEntity() {
    }

    public TokenEntity(String token, int userId, String deviceId, String deviceOs, Date lastLogin, Date tokenExpired) {
        this.token = token;
        this.userId = userId;
        this.deviceId = deviceId;
        this.deviceOs = deviceOs;
        this.lastLogin = lastLogin;
        this.tokenExpired = tokenExpired;
    }

    @Override
    public String getId() {
        return this.getToken();
    }

    @Override
    public void setId(String token) {
        this.setToken(token.toString());
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceOs() {
        return this.deviceOs;
    }

    public void setDeviceOs(String deviceOs) {
        this.deviceOs = deviceOs;
    }

    public Date getLastLogin() {
        return this.lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getTokenExpired() {
        return this.tokenExpired;
    }

    public void setTokenExpired(Date tokenExpired) {
        this.tokenExpired = tokenExpired;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.token);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TokenEntity other = (TokenEntity) obj;
        if (!Objects.equals(this.token, other.token)) {
            return false;
        }
        return true;
    }

}
