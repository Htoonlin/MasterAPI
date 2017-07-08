/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Htoonlin
 */
@Entity
@Table(name = "tbl_notification")
public class NotifyEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "id", columnDefinition = "CHAR(36)", nullable = false, unique = true)
    private String id;

    @Column(name = "receiverId", columnDefinition = "INT UNSIGNED", nullable = false)
    private long receiverId;

    @Column(name = "title", columnDefinition = "VARCHAR(255)", nullable = false)
    private String title;

    @Column(name = "message", columnDefinition = "VARCHAR(500)", nullable = false)
    private String message;

    @Column(name = "type", columnDefinition = "CHAR(1)", nullable = false)
    private char type;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, updatable = false, length = 19)
    private Date createdAt;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_at", columnDefinition = "DATETIME", nullable = false, updatable = false, length = 19)
    private Date expiredAt;

    @Column(name = "isRead", columnDefinition = "BIT(1)", nullable = false)
    private boolean read;

    @Column(name = "created_by", columnDefinition = "INT UNSIGNED", nullable = false)
    private long createdBy;

    public NotifyEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

}
