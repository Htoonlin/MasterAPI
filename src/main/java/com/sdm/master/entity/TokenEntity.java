package com.sdm.master.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sdm.Constants;
import com.sdm.core.Setting;
import com.sdm.core.hibernate.entity.DefaultEntity;
import com.sdm.core.ui.UIInputType;
import com.sdm.core.ui.UIStructure;
import com.sdm.core.util.SecurityManager;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * TokenEntity generated by hbm2java
 */
@Audited
@DynamicUpdate(value = true)
@Entity(name = "TokenEntity")
@Table(name = "tbl_user_token")
@JsonPropertyOrder(value = {"token", "deviceId", "device_os", "token_expired"})
public class TokenEntity extends DefaultEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -7999643701327132659L;

    @Id
    @UIStructure(order = 0, label = "Token", readOnly = true)
    @Column(name = "token", unique = true, nullable = false, length = 36)
    private String token;

    @UIStructure(order = 1, label = "User ID", inputType = UIInputType.number)
    @Column(name = "userId", nullable = false)
    private int userId;

    @UIStructure(order = 2, label = "Device-ID")
    @Column(name = "deviceId", nullable = false, length = 255)
    private String deviceId;

    @UIStructure(order = 3, label = "Device-OS")
    @Column(name = "deviceOS", nullable = false, length = 10)
    private String deviceOs;

    @NotAudited
    @Temporal(TemporalType.TIMESTAMP)
    @UIStructure(order = 4, label = "Lasted Login")
    @Column(name = "lastedLogin", nullable = false, length = 19, updatable = true)
    private Date lastLogin;

    @NotAudited
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

    @JsonIgnore
    public String generateJWT(String userAgent) {
        String jwtKey = Setting.getInstance().get(Setting.JWT_KEY, SecurityManager.generateJWTKey());
        String compactJWT = Jwts.builder().setSubject(Constants.USER_PREFIX + this.userId).setIssuer(userAgent)
                .setIssuedAt(new Date()).setExpiration(this.tokenExpired).setId(this.token)
                .claim("device_id", this.deviceId).claim("device_os", this.deviceOs)
                .compressWith(CompressionCodecs.DEFLATE).signWith(SignatureAlgorithm.HS512, jwtKey).compact();

        return compactJWT;
    }

    public String getId() {
        return this.getToken();
    }

    public void setId(String token) {
        this.setToken(token.toString());
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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
