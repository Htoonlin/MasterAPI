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
    int getVersion();
    
    void setVersion(int version);
    
    int getCreatedBy();

    void setCreatedBy(int userId);

    int getModifiedBy();

    void setModifiedBy(int userId);
}
