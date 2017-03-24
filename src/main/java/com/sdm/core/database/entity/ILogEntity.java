/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.database.entity;

/**
 *
 * @author Htoonlin
 */
public interface ILogEntity {
    long getVersion();
    
    void setVersion(long version);
    
    long getCreatedBy();

    void setCreatedBy(long userId);

    long getModifiedBy();

    void setModifiedBy(long userId);
}
