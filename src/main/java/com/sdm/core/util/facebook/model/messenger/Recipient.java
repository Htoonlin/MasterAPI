/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util.facebook.model.messenger;

import java.io.Serializable;

/**
 *
 * @author htoonlin
 */
public class Recipient implements Serializable{
    private String id;
    private String phoneNumber;    
    private String name;

    public Recipient(String id) {
        this.id = id;
    }
    
    public Recipient() {
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
