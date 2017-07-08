/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdm.core.hibernate.entity.DefaultEntity;
import com.sdm.core.hibernate.entity.UIStructure;

/**
 *
 * @author Htoonlin
 */
@Entity(name = "RoleEntity")
@Table(name = "tbl_role")
public class RoleEntity extends DefaultEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
    @Formula(value = "concat(name, description)")    
    private String search;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @UIStructure(order = 0, label = "#", readOnly = true)
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "MEDIUMINT UNSIGNED")
    private int id;

    @UIStructure(order = 1, label = "Name")
    @Column(name = "name", columnDefinition = "varchar(255)", length = 255, nullable = false)
    private String name;

    @UIStructure(order = 2, label = "Description")
    @Column(name = "description", columnDefinition = "varchar(500)", length = 500, nullable = false)
    private String description;

    @JsonIgnore    
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId", columnDefinition = "MEDIUMINT UNSIGNED")
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<PermissionEntity> permissions;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NotBlank
    @Size(min = 1, max = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Size(min = 0, max = 500)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<PermissionEntity> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionEntity> permissions) {
        this.permissions = permissions;
    }

    @JsonGetter("permission_count")
    public int getPermissionCount() {
        return this.permissions.size();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + this.id;
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
        final RoleEntity other = (RoleEntity) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
