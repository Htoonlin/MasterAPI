/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sdm.master.entity;

import com.sdm.core.hibernate.entity.DefaultEntity;
import com.sdm.core.ui.UIInputType;
import com.sdm.core.ui.UIStructure;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

/**
 *
 * @author YOETHA
 */

@Audited
@DynamicUpdate(value = true)
@Entity(name = "UserExtraEntity")
@Table(name = "tbl_user_extra")
public class UserExtraEntity extends DefaultEntity implements Serializable {

    private static final long serialVersionUID = -8674657300840972240L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @UIStructure(order = 0, label = "#", readOnly = true)
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "MEDIUMINT UNSIGNED")
    private int id;

    @UIStructure(order = 1, label = "User Id", readOnly = true)
    @Column(name = "userId", nullable = false, columnDefinition = "MEDIUMINT UNSIGNED")
    private int userId;

    @UIStructure(order = 2, label = "Name", inputType = UIInputType.text)
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @UIStructure(order = 1, label = "Value", inputType = UIInputType.text)
    @Column(name = "value", nullable = false, length = 255)
    private String value;
    
    @UIStructure(order = 1, label = "Extra", inputType = UIInputType.text)
    @Column(name = "extra", nullable = false, length = 255)
    private String extra;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
    
    public UserExtraEntity() {

    }

    public UserExtraEntity(int userId, String name, String value) {
        this.userId = userId;
        this.name = name;
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final UserExtraEntity other = (UserExtraEntity) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
