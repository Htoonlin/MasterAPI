package com.sdm.master.entity;

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

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdm.core.hibernate.entity.DefaultEntity;
import com.sdm.core.ui.UIInputType;
import com.sdm.core.ui.UIStructure;

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
	@Formula(value = "concat(name, state, icon, type)")
	private String search;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@UIStructure(order = 0, label = "#", readOnly = true)
	@Column(name = "id", unique = true, nullable = false, columnDefinition = "MEDIUMINT UNSIGNED")
	private int id;

	@UIStructure(order = 1, label = "Name")
	@Column(name = "name", columnDefinition = "varchar(50)", length = 50, nullable = false)
	private String name;

	@UIStructure(order = 2, label = "Description", inputType = UIInputType.textarea)
	@Column(name = "description", columnDefinition = "varchar(255)", length = 255, nullable = true)
	private String description;

	@UIStructure(order = 3, label = "State")
	@Column(name = "state", columnDefinition = "varchar(500)", length = 500, nullable = false)
	private String state;

	@UIStructure(order = 4, label = "Icon", inputType = UIInputType.image)
	@Column(name = "icon", columnDefinition = "varchar(50)", length = 50, nullable = false)
	private String icon;

	/**
	 * Supported Types : module, toggle, link
	 */
	@UIStructure(order = 4, label = "Type")
	@Column(name = "type", columnDefinition = "varchar(10)", length = 10, nullable = false)
	private String type;

	@UIStructure(order = 5, label = "priority", inputType = UIInputType.number)
	@Column(name = "priority", columnDefinition = "INT", nullable = false)
	private int priority;

	@UIStructure(order = 6, label = "Is separator?", inputType = UIInputType.checkbox)
	@Column(name = "isDivider", columnDefinition = "bit(1)", nullable = false)
	private boolean separator;

	@UIStructure(order = 7, label = "Parent Menu", inputType = UIInputType.number, hideInGrid = true)
	@NotAudited
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentId", columnDefinition = "INT UNSIGNED", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	@NotFound(action = NotFoundAction.IGNORE)
	private Set<MenuEntity> children;

	@UIStructure(order = 8, label = "Roles", inputType = UIInputType.objectlist, hideInGrid = true)
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "tbl_menu_permission", joinColumns = {
			@JoinColumn(name = "menuId", columnDefinition = "MEDIUMINT UNSIGNED") }, inverseJoinColumns = {
					@JoinColumn(name = "roleId", columnDefinition = "MEDIUMINT UNSIGNED") })
	@NotFound(action = NotFoundAction.IGNORE)
	private Set<RoleEntity> roles;

	public MenuEntity() {

	}

	public MenuEntity(int id, String name, String description, String state, String icon, String type, int priority,
			boolean separator) {
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MenuEntity other = (MenuEntity) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
