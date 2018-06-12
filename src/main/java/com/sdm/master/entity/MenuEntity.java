package com.sdm.master.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdm.core.hibernate.entity.DefaultEntity;
import com.sdm.core.response.model.LinkModel;
import com.sdm.core.ui.UIInputType;
import com.sdm.core.ui.UIStructure;
import com.sdm.master.resource.MenuResource;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.ws.rs.core.UriBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.*;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.*;
import java.util.*;
import javax.persistence.*;
import java.math.*;
import com.sdm.core.util.MyanmarFontManager;

/**
 * 
 * @Author ayeyin
 * @Since 2017-11-17 14:33:42
 */
@Audited
@DynamicUpdate(value = true)
@Entity(name = "MenuEntity")
@Table(name = "tbl_menu")
public class MenuEntity extends DefaultEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6187216010093509650L;

    @JsonIgnore
    @NotAudited
    @Formula(value = "concat(icon, name, state, type)")
    private String search;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @UIStructure(order = 0, label = "#", inputType = UIInputType.number, hideInGrid = false, readOnly = true)
    @Column(name = "id", columnDefinition = "MEDIUMINT UNSIGNED")
    private int id;

    @UIStructure(order = 1, label = "Name", inputType = UIInputType.text, hideInGrid = false, readOnly = false)
    @Column(name = "name", nullable = false, columnDefinition = "varchar(50)")
    private String name;

    @UIStructure(order = 2, label = "Description", inputType = UIInputType.textarea, hideInGrid = false, readOnly = false)
    @Column(name = "description", nullable = true, columnDefinition = "varchar(255)")
    private String description;

    @UIStructure(order = 3, label = "State", inputType = UIInputType.text, hideInGrid = false, readOnly = false)
    @Column(name = "state", nullable = false, columnDefinition = "varchar(500)")
    private String state;

    @UIStructure(order = 4, label = "Icon", inputType = UIInputType.image, hideInGrid = false, readOnly = false)
    @Column(name = "icon", nullable = false, columnDefinition = "varchar(50)")
    private String icon;

    /**
     * Supported Types : module, toggle, link
     */
    @UIStructure(order = 4, label = "Type", inputType = UIInputType.text, hideInGrid = false, readOnly = false)
    @Column(name = "type", nullable = false, columnDefinition = "varchar(10)")
    private String type;

    @UIStructure(order = 5, label = "priority", inputType = UIInputType.number, hideInGrid = false, readOnly = false)
    @Column(name = "priority", nullable = false, columnDefinition = "INT")
    private int priority;

    @UIStructure(order = 6, label = "Is separator?", inputType = UIInputType.checkbox, hideInGrid = false, readOnly = false)
    @Column(name = "isDivider", nullable = false, columnDefinition = "bit(1)")
    private boolean separator;

    @NotFound(action = NotFoundAction.IGNORE)
    @NotAudited
    @UIStructure(order = 7, label = "Parent Menu", inputType = UIInputType.number, hideInGrid = true, readOnly = false)
    @JoinColumn(name = "parentId", columnDefinition = "INT UNSIGNED", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @OneToMany(fetch = FetchType.LAZY)
    private Set<MenuEntity> children;

    @NotFound(action = NotFoundAction.IGNORE)
    @UIStructure(order = 8, label = "Roles", inputType = UIInputType.objectlist, hideInGrid = true, readOnly = false)
    @JoinTable(name = "tbl_menu_permission", joinColumns = { @JoinColumn(name = "menuId", columnDefinition = "MEDIUMINT UNSIGNED") }, inverseJoinColumns = { @JoinColumn(name = "roleId", columnDefinition = "MEDIUMINT UNSIGNED") })
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<RoleEntity> roles;

    public MenuEntity() {
    }

    public MenuEntity(int id, String name, String description, String state, String icon, String type, int priority, boolean separator) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.state = state;
        this.icon = icon;
        this.type = type;
        this.priority = priority;
        this.separator = separator;
    }

    @JsonGetter("&detail_link")
    public LinkModel getSelfLink() {
        String selfLink = UriBuilder.fromResource(MenuResource.class).path(Integer.toString(this.id)).build().toString();
        return new LinkModel(selfLink);
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isSeparator() {
        return separator;
    }

    public void setSeparator(boolean separator) {
        this.separator = separator;
    }

    public Set<MenuEntity> getChildren() {
        return children;
    }

    public void setChildren(Set<MenuEntity> children) {
        this.children = children;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MenuEntity other = (MenuEntity) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

    public boolean getSeparator() {
        return separator;
    }
}
