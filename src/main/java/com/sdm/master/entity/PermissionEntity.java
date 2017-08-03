/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.core.UriBuilder;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdm.core.hibernate.entity.DefaultEntity;
import com.sdm.core.response.LinkModel;
import com.sdm.core.ui.UIInputType;
import com.sdm.core.ui.UIStructure;
import com.sdm.master.resource.PermissionResource;

/**
 *
 * @author Htoonlin
 */
@Audited
@DynamicUpdate(value = true)
@Entity(name = "PermissionEntity")
@Table(name = "tbl_permission")
public class PermissionEntity extends DefaultEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 231291254158536747L;

	@JsonIgnore
	@NotAudited
	@Formula(value = "concat(resourceClass, resourceMethod, requestMethod)")
	private String search;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@UIStructure(order = 0, label = "#", readOnly = true)
	@Column(name = "id", columnDefinition = "INT UNSIGNED", nullable = false, unique = true)
	private long id;

	@UIStructure(order = 1, label = "Role-ID", inputType = UIInputType.number)
	@Column(name = "roleId", columnDefinition = "MEDIUMINT UNSIGNED", nullable = false)
	private int roleId;

	@UIStructure(order = 2, label = "Class")
	@Column(name = "resourceClass", columnDefinition = "varchar(255)", length = 255, nullable = false)
	private String resourceClass;

	@UIStructure(order = 3, label = "Method")
	@Column(name = "resourceMethod", columnDefinition = "varchar(255)", length = 255, nullable = false)
	private String resourceMethod;

	@UIStructure(order = 4, label = "Http-Method")
	@Column(name = "requestMethod", columnDefinition = "varchar(10)", length = 10, nullable = false)
	private String requestMethod;

	@JsonGetter("&detail_link")
	public LinkModel getSelfLink() {
		String selfLink = UriBuilder.fromResource(PermissionResource.class).path(Long.toString(this.id)).build()
				.toString();
		return new LinkModel(selfLink);
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	@NotBlank
	@Size(min = 1, max = 255)
	public String getResourceClass() {
		return resourceClass;
	}

	public void setResourceClass(String resourceClass) {
		this.resourceClass = resourceClass;
	}

	@NotBlank
	@Size(min = 1, max = 255)
	public String getResourceMethod() {
		return resourceMethod;
	}

	public void setResourceMethod(String resourceMethod) {
		this.resourceMethod = resourceMethod;
	}

	@NotBlank
	@Size(min = 1, max = 10)
	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
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
		final PermissionEntity other = (PermissionEntity) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}
}
