/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdm.core.database.entity.RestEntity;
import com.sdm.core.ui.UIStructure;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Htoonlin
 */
@Entity
@Table(name = "tbl_permission")
public class PermissionEntity extends RestEntity<Integer> implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @JsonIgnore
    @Formula(value = "concat(resourceClass, resourceMethod, requestMethod)")
    private String search;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @UIStructure(order = 0, label = "#", readOnly = true)
    @Column(name = "id", columnDefinition = "bigint", nullable = false, unique = true)
    private int id;
    
    @UIStructure(order = 1, label = "Role-ID")
    @Column(name = "roleId", columnDefinition = "int(11)", nullable = false)
    private int roleId;
    
    @UIStructure(order = 2, label = "Class")
    @Column(name = "resourceClass", columnDefinition = "varchar(255)", nullable = false)
    private String resourceClass;    
    
    @UIStructure(order = 3, label = "Method")
    @Column(name = "resourceMethod", columnDefinition = "varchar(255)", nullable = false)
    private String resourceMethod;
    
    @UIStructure(order = 4, label = "Http-Method")
    @Column(name = "requestMethod", columnDefinition = "varchar(10)", nullable = false)
    private String requestMethod;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
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
        hash = 53 * hash + this.id;
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
