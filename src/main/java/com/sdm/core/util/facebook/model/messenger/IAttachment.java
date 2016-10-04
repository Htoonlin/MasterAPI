/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util.facebook.model.messenger;

import com.fasterxml.jackson.annotation.JsonGetter;

/**
 *
 * @author Htoonlin
 */
public interface IAttachment{
    @JsonGetter("type")
    String getType();
    
    @JsonGetter("payload")
    Object getPayload();
}
