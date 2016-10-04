/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.request.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sdm.core.request.DefaultRequest;
import java.io.Serializable;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Htoonlin
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ActivateRequest extends DefaultRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;

    private String token;

    private String deviceId;

    @NotBlank(message = "DeviceID can't be blank.")
    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String value) {
        this.deviceId = value;
    }

    @Email(message = "Ivalid email format.")
    @NotBlank(message = "Email can't be blank.")
    @Size(min = 6, max = 255)
    public String getEmail() {
        return this.email;
    }

    @NotBlank(message = "Token can't be blank.")
    @Size(min = 6, max = 255)
    public String getToken() {
        return this.token;
    }

    public void setEmail(String value) {
        this.email = value;
    }

    public void setToken(String value) {
        this.token = value;
    }
}
