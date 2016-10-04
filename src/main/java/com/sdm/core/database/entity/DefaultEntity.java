/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.database.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Htoonlin
 */
@MappedSuperclass
public class DefaultEntity implements Serializable, ILogEntity, ITimestampEntity {

    private static final long serialVersionUID = 1L;

    public DefaultEntity() {
    }
    
    @JsonProperty(value = "version", index = 1000)
    @Column(name = "version", columnDefinition = "int", nullable = false)
    private int version;

    @JsonProperty(value = "created_by", index = 1001)
    @Column(name = "created_by", columnDefinition = "int", nullable = false)
    private int createdBy;

    @JsonProperty(value = "modified_by", index = 1002)
    @Column(name = "modified_by", columnDefinition = "int", nullable = true)
    private int modifiedBy;

    @JsonProperty(value = "created_at", index = 1003)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false, length = 19)
    private Date createdAt;

    @JsonProperty(value = "modified_at", index = 1004)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at", nullable = true, updatable = true, length = 19)
    private Date modifiedAt;

    @JsonProperty(value = "deleted_at", index = 1005)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at", columnDefinition = "datetime", nullable = true, updatable = true, length = 19)
    private Date deletedAt;

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public int getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public int getModifiedBy() {
        return modifiedBy;
    }

    @Override
    public void setModifiedBy(int modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Date getModifiedAt() {
        return modifiedAt;
    }

    @Override
    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @Override
    public Date getDeletedAt() {
        return deletedAt;
    }

    @Override
    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }
}
