/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sdm.core.hibernate.entity.DefaultEntity;

/**
 *
 * @author Htoonlin
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RegistrationRequest extends DefaultEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;

    private String displayName;

    private String password;

    private String country;

    @NotNull(message = "Display name is required.")
    @Size(min = 1, max = 255)
    public String getDisplayName() {
        return this.displayName;
    }
    
    public void setDisplayName(String value) {
        this.displayName = value;
    }
    
    @Email(message = "Ivalid email format.")
    @NotBlank(message = "Email can't be blank.")
    @Size(min = 6, max = 255)
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String value) {
        this.email = value;
    }

    @NotNull(message = "Password is required.")
    @Size(min = 6, max = 255)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String value) {
        this.password = value;
    }

    @NotNull(message = "Country is required.")
    @Size(min = 2, max = 2, message = "Invalid country code.")
    public String getCountry() {
        return this.country;
    }  

    public void setCountry(String value) {
        this.country = value;
    }
}
