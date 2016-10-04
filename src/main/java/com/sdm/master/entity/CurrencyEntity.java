/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdm.core.database.entity.RestEntity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Htoonlin
 */
@Entity
@Table(name = "tbl_currency")
@XmlRootElement(name = "currency")
@XmlAccessorType(XmlAccessType.FIELD)
public class CurrencyEntity extends RestEntity<Integer> implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @Formula(value = "concat(sign, code, name)")
    private String search;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "sign", columnDefinition = "varchar(5)", nullable = false)
    private String sign;

    @Column(name = "code", columnDefinition = "char(3)", unique = true, nullable = false)
    private String code;

    @Column(name = "name", columnDefinition = "varchar(100)", nullable = false)
    private String name;

    @Column(name = "currentRate", columnDefinition = "float", nullable = false)
    private float currentRate;

    @Column(name = "isActive", columnDefinition = "bit(1)", nullable = false)
    private boolean active;

    public CurrencyEntity(int id, String sign, String code, String name, float currentRate) {
        this();
        this.sign = sign;
        this.code = code;
        this.name = name;
        this.currentRate = currentRate;
    }
    
    public CurrencyEntity(String sign, String code, String name, float currentRate) {
        this();
        this.sign = sign;
        this.code = code;
        this.name = name;        
        this.currentRate = currentRate;
    }

    public CurrencyEntity() {
        this.active = true;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
   
    @Override
    public Integer getId() {
        return id;
    }
    
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @NotBlank(message = "Sign shouldn't be blank.")
    @Size(min = 1, max = 5)
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @NotBlank(message = "Code shouldn't be blank.")
    @Size(min = 3, max = 3)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @NotBlank(message = "Name shouldn't be blank.")
    @Size(min = 1, max = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Min(value = 1, message = "Current rate shouldn't be blank.")
    public float getCurrentRate() {
        return currentRate;
    }

    public void setCurrentRate(float currentRate) {
        this.currentRate = currentRate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.id;
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
        final CurrencyEntity other = (CurrencyEntity) obj;
        return true;
    }
}
