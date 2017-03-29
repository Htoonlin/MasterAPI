/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.mysql.model.type;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Htoonlin
 */
public interface PropertyType {

    public String getName();

    @JsonIgnore
    public String defaultSQL();

    public boolean validType(Object value);
}
