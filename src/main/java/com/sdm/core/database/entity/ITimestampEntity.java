/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.database.entity;

import java.util.Date;

/**
 *
 * @author Htoonlin
 */
public interface ITimestampEntity {

    Date getCreatedAt();

    void setCreatedAt(Date value);

    Date getModifiedAt();

    void setModifiedAt(Date value);

    Date getDeletedAt();

    void setDeletedAt(Date value);
}
