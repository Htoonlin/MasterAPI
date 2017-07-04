/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.hibernate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdm.core.request.DefaultRequest;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author htoonlin
 */
@MappedSuperclass
public class RestEntity extends DefaultRequest implements Serializable{

    private static final long serialVersionUID = 1L;

    public RestEntity() {
    }

    //@JsonProperty(value = "version", index = 1000)    
    @JsonIgnore
    @Column(name = "version", columnDefinition = "INT UNSIGNED", nullable = false)
    private long version;

    //@JsonProperty(value = "created_by", index = 1001)
    @JsonIgnore
    @Column(name = "created_by", columnDefinition = "INT UNSIGNED", nullable = false)
    private long createdBy;

    //@JsonProperty(value = "modified_by", index = 1002)
    @JsonIgnore
    @Column(name = "modified_by", columnDefinition = "INT UNSIGNED", nullable = true)
    private long modifiedBy;

    //@JsonProperty(value = "created_at", index = 1003)
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, updatable = false, length = 19)
    private Date createdAt;

    //@JsonProperty(value = "modified_at", index = 1004)
    @JsonIgnore
    @Temporal(TemporalType.DATE)
    @Column(name = "modified_at", columnDefinition = "DATETIME on update CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = true, updatable = true, length = 19)
    private Date modifiedAt;

    //@JsonProperty(value = "deleted_at", index = 1005)
    @JsonIgnore
    @Temporal(TemporalType.DATE)
    @Column(name = "deleted_at", columnDefinition = "DATETIME", nullable = true, updatable = true, length = 19)
    private Date deletedAt;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }
}
