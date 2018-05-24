/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sdm.core.request.IBaseRequest;
import com.sdm.core.util.SecurityManager;
import com.sdm.master.entity.UserEntity;
import java.util.Date;
import java.util.regex.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Htoonlin
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuthRequest implements IBaseRequest {

    /**
     *
     */
    private static final long serialVersionUID = -341416570638461653L;

    private String user;

    private String password;

    private String deviceId;

    private Date timestamp;

    @NotBlank(message = "DeviceID can't be blank.")
    @Size(min = 10, max = 255)
    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String value) {
        this.deviceId = value;
    }

    @NotBlank(message = "Email can't be blank.")
    @Size(min = 6, max = 255)
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    
    /*
    @Email(message = "Invalid email format.")
    @NotBlank(message = "Email can't be blank.")
    @Size(min = 6, max = 255)
    public String getEmail() {
        return this.email;
    }*/

    @NotBlank(message = "Password can't be blank.")
    @Size(min = 2, max = 255)
    public String getPassword() {
        return this.password;
    }

    /*
    public void setEmail(String value) {
        this.email = value;
    }*/

    public void setPassword(String value) {
        this.password = value;
    }

    public String getCryptPassword() {
        return SecurityManager.hashString(this.user, this.password);
    }

    public boolean isAuth(UserEntity authUser) {
        if (authUser == null) {
            return false;
        }
        
        Pattern pattern=Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        boolean isEmail = pattern.matcher(user).matches();
        
        String cryptPassword = this.getCryptPassword();
        if(isEmail)
            return (authUser.getEmail().equalsIgnoreCase(this.user) && authUser.getPassword().equals(cryptPassword));
        else{
            return (authUser.getUserName().equalsIgnoreCase(this.user) && authUser.getuPassword().equals(cryptPassword));
        }
    }

    @Override
    public Date getTimestamp() {
        return this.timestamp;
    }

    @Override
    public void setTimestamp(long date) {
        this.timestamp = new Date(date);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AuthRequest other = (AuthRequest) obj;
        if (deviceId == null) {
            if (other.deviceId != null) {
                return false;
            }
        } else if (!deviceId.equals(other.deviceId)) {
            return false;
        }
        if (user == null) {
            if (other.user != null) {
                return false;
            }
        } else if (!user.equals(other.user)) {
            return false;
        }
        if (password == null) {
            if (other.password != null) {
                return false;
            }
        } else if (!password.equals(other.password)) {
            return false;
        }
        return true;
    }

}
