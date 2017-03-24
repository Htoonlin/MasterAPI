/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Htoonlin
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuthorizeRequest extends DefaultRequest implements Serializable {

    private long userId;
    
    private String deviceId;

    private String deviceOS;

    private String token;

    @NotNull
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @NotNull
    @Size(min = 10, max = 255)
    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String value) {
        this.deviceId = value;
    }

    public String getDeviceOS() {
        return this.deviceOS;
    }

    public void setDeviceOS(String value) {
        this.deviceOS = value;
    }

    @NotNull
    @Size(min = 36, max = 36)
    public String getToken() {
        return this.token;
    }

    public void setToken(String value) {
        this.token = value;
    }
}
