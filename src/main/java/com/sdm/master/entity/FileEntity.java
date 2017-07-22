/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sdm.core.hibernate.entity.DefaultEntity;
import com.sdm.core.hibernate.entity.UIStructure;
import com.sdm.core.util.FileManager;

/**
 *
 * @author Htoonlin
 */
@Audited
@Entity(name = "FileEntity")
@Table(name = "tbl_file")
public class FileEntity extends DefaultEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2692423129475255385L;
	public static final char STORAGE = 'S';
	public static final char EXTERNAL = 'E';
	public static final char TRASH = 'T';

	@JsonIgnore
	@NotAudited
	@Formula(value = "concat(name, extension, type)")
	private String search;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, columnDefinition = "BIGINT UNSIGNED")
	@UIStructure(order = 0, label = "#", readOnly = true)
	private BigInteger id;

	@UIStructure(order = 1, label = "Owner ID")
	@Column(name = "owner", columnDefinition = "MEDIUMINT UNSIGNED", nullable = false)
	private int ownerId;

	@NotBlank
	@UIStructure(order = 2, label = "Name")
	@Column(name = "name", columnDefinition = "varchar(255)", length = 255, nullable = false)
	private String name;

	@NotBlank
	@UIStructure(order = 3, label = "Ext.")
	@Column(name = "extension", columnDefinition = "varchar(10)", length = 10, nullable = false)
	private String extension;

	@UIStructure(order = 4, label = "Type")
	@Column(name = "type", columnDefinition = "varchar(50)", length = 50, nullable = false)
	private String type;

	@JsonIgnore
	@Column(name = "size", columnDefinition = "INT UNSIGNED", nullable = false)
	private long fileSize;

	@JsonIgnore
	@Column(name = "storagePath", columnDefinition = "varchar(1000)", length = 1000, nullable = true)
	private String storagePath;

	@UIStructure(order = 5, label = "External URL")
	@Column(name = "externalURL", columnDefinition = "varchar(1000)", length = 1000, nullable = true)
	private String externalUrl;

	@Column(name = "isPublic", length = 25, nullable = true)
	private boolean publicAccess;

	@UIStructure(order = 6, label = "Status")
	@Column(name = "status", columnDefinition = "char(1)", length = 1, nullable = false)
	private char status;

	public FileEntity() {
		this.status = STORAGE;
	}

	public FileEntity(BigInteger id, int ownerId, String name, String extension, String type, long fileSize,
			String storagePath, String externalUrl) {
		if (externalUrl == null || externalUrl.length() <= 0) {
			this.status = STORAGE;
		} else {
			this.status = EXTERNAL;
		}
		this.ownerId = ownerId;
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

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
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
		return externalUrl;
	}

	public void setExternalURL(String externalURL) {
		this.externalUrl = externalURL;
	}
	
	public boolean isPublicAccess() {
		return publicAccess;
	}

	public void setPublicAccess(boolean publicAccess) {
		this.publicAccess = publicAccess;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 23 * hash + Objects.hashCode(this.id);
		return hash;
	}

}
