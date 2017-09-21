/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util.mail.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Htoonlin
 */
@JsonIgnoreProperties({"parts", "did_you_mean"})
public class ValidateResponse {

    private boolean is_valid;
    private String address;

    @JsonCreator
    public ValidateResponse(@JsonProperty("is_valid") boolean valid, @JsonProperty("address") String address) {
        this.is_valid = valid;
        this.address = address;
    }

    public boolean isValid() {
        return is_valid;
    }

    public void setValid(boolean valid) {
        this.is_valid = valid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
