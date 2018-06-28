/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sdm.core.request.IBaseRequest;
import java.util.Date;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Htoonlin
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ChangePasswordRequest implements IBaseRequest {

    /**
     *
     */
    private static final long serialVersionUID = 6890755299063523487L;
    private String user;

    @NotBlank(message = "User field can't be blank.")
    @Size(min = 6, max = 255)
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private String oldPassword;

    @NotBlank(message = "Password field can't be blank.")
    @Size(min = 6, max = 255)
    public String getOldPassword() {
        return this.oldPassword;
    }

    public void setOldPassword(String value) {
        this.oldPassword = value;
    }

    private String newPassword;

    @NotBlank(message = "New password field can't be blank.")
    @Size(min = 6, max = 255)
    public String getNewPassword() {
        return this.newPassword;
    }

    public void setNewPassword(String value) {
        this.newPassword = value;
    }

    private String token;

    /**
     * Get the value of token
     *
     * @return the value of token
     */
    public String getToken() {
        return token;
    }

    /**
     * Set the value of token
     *
     * @param token new value of token
     */
    public void setToken(String token) {
        this.token = token;
    }

    private Date timestamp;

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
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = prime * result + ((token == null) ? 0 : token.hashCode());
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
        ChangePasswordRequest other = (ChangePasswordRequest) obj;
        if (user == null) {
            if (other.user != null) {
                return false;
            }
        } else if (!user.equals(other.user)) {
            return false;
        }
        if (token == null) {
            if (other.token != null) {
                return false;
            }
        } else if (!token.equals(other.token)) {
            return false;
        }
        return true;
    }

}
