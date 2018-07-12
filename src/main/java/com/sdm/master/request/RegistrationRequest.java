/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sdm.core.request.IBaseRequest;
import java.util.Date;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Htoonlin
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RegistrationRequest implements IBaseRequest {

    /**
     *
     */
    private static final long serialVersionUID = -6887685772544814783L;

    private String email;

    private String userName;
    
    private String displayName;

    private String password;

    private Date timestamp;

    @Pattern(regexp = "[a-zA-Z0-9_\\.]+", 
            message = "Sorry! invalid user name, allow char (a-zA-Z0-9) and special char (`.` and `_`). Eg./ mg_hla.09")
    @NotBlank(message = "User name/E-mail is required.")
    @Size(min = 1, max = 255)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    @NotBlank(message = "Display name is required.")
    @Size(min = 1, max = 255)
    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String value) {
        this.displayName = value;
    }

    @Email(message = "Ivalid email format.")
    @Size(min = 6, max = 255)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String value) {
        this.email = value;
    }

    @NotBlank(message = "Password is required.")
    @Size(min = 6, max = 255)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String value) {
        this.password = value;
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
        result = prime * result + ((email == null) ? 0 : email.hashCode());
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
        RegistrationRequest other = (RegistrationRequest) obj;
        if (email == null) {
            if (other.email != null) {
                return false;
            }
        } else if (!email.equals(other.email)) {
            return false;
        }
        return true;
    }

}
