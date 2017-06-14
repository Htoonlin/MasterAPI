/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sdm.core.request.DefaultRequest;
import com.sdm.core.util.SecurityManager;
import com.sdm.master.entity.UserEntity;
import java.io.Serializable;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Htoonlin
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuthRequest extends DefaultRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;

    private String password;

    private String deviceId;

    @NotBlank(message = "DeviceID can't be blank.")
    @Size(min = 10, max = 255)
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
    
    @NotBlank(message = "Password can't be blank.")
    @Size(min = 2, max = 255)
    public String getPassword() {
        return this.password;
    }

    public void setEmail(String value) {
        this.email = value;
    }

    public void setPassword(String value) {
        this.password = value;
    }

    public String getCryptPassword() {
        return SecurityManager.md5String(this.email, this.password);
    }

    public boolean isAuth(UserEntity authUser) {
        if (authUser == null) {
            return false;
        }
        String cryptPassword = this.getCryptPassword();
        return (authUser.getEmail().equalsIgnoreCase(this.email)
                && authUser.getPassword().equals(cryptPassword));
    }
}
