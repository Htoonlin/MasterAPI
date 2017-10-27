/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Htoonlin
 */
public interface IBaseRequest extends Serializable {

    @JsonSetter("timestamp")
    void setTimestamp(long date);

    Date getTimestamp();
}
