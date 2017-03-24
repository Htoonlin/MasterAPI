/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sdm.core.database.entity.RestEntity;
import com.sdm.core.ui.UIStructure;
import com.sdm.core.util.FileManager;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Formula;

/**
 *
 * @author Htoonlin
 */
@Entity
@Table(name = "tbl_file")
public class FileEntity extends RestEntity<Double> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final char STORAGE = 'S';
    public static final char EXTERNAL = 'E';
    public static final char TRASH = 'T';

    @JsonIgnore
    @Formula(value = "concat(name, extension, type)")
    private String search;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "BIGINT(20) UNSIGNED")
    @UIStructure(order = 0, label = "#", readOnly = true)
    private double id;

    @UIStructure(order = 1, label = "Name")
    @Column(name = "name", columnDefinition = "varchar(255)", length = 255, nullable = false)
    private String name;

    @UIStructure(order = 2, label = "Ext.")
    @Column(name = "extension", columnDefinition = "varchar(10)", length = 10, nullable = false)
    private String extension;

    @UIStructure(order = 3, label = "Type")
    @Column(name = "type", columnDefinition = "varchar(50)", length = 50, nullable = false)
    private String type;

    @JsonIgnore
    @Column(name = "size", columnDefinition = "bigint", nullable = false)
    private long fileSize;

    @JsonIgnore
    @Column(name = "storagePath", columnDefinition = "varchar(1000)", length = 1000, nullable = false)
    private String storagePath;

    @UIStructure(order = 4, label = "External URL")
    @Column(name = "externalURL", columnDefinition = "varchar(1000)", length = 1000, nullable = false)
    private String externalURL;

    @JsonIgnore
    @Column(name = "publicToken", columnDefinition = "char(12)", length = 12, nullable = false)
    private String publicToken;

    @UIStructure(order = 5, label = "Status")
    @Column(name = "status", columnDefinition = "char(1)", length = 1, nullable = false)
    private char status;

    public FileEntity() {
        this.status = STORAGE;
    }

    public FileEntity(int id, String name, String extension, String type, long fileSize, String storagePath, String externalURL) {
        if (externalURL == null || externalURL.length() <= 0) {
            this.status = STORAGE;
        } else {
            this.status = EXTERNAL;
        }
        this.id = id;
        this.name = name;
        this.extension = extension;
        this.type = type;
        this.fileSize = fileSize;
        this.storagePath = storagePath;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    @Override
    public Double getId() {
        return id;
    }

    @Override
    public void setId(Double id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long size) {
        this.fileSize = size;
    }

    @JsonGetter("size")
    public String getSize() {
        return FileManager.byteSize(fileSize);
    }

    @JsonSetter("size")
    public void setSize() {

    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getExternalURL() {
        return externalURL;
    }

    public void setExternalURL(String externalURL) {
        this.externalURL = externalURL;
    }

    public String getPublicToken() {
        return publicToken;
    }

    public void setPublicToken(String publicToken) {
        this.publicToken = publicToken;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    @JsonGetter("public_url")
    public String getPublicURL() {
        if (externalURL != null && !externalURL.isEmpty()) {
            return externalURL;
        }

        return FileManager.publicFileURL(this.publicToken, this.extension);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.id) ^ (Double.doubleToLongBits(this.id) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FileEntity other = (FileEntity) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
