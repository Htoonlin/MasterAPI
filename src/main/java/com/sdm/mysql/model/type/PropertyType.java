/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.mysql.model.type;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Htoonlin
 */
public interface PropertyType {

    public String getName();

    @JsonIgnore
    public String getSQL();

    public boolean validType(Object value);
}
