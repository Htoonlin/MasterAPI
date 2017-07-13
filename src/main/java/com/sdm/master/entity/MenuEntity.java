package com.sdm.master.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdm.core.hibernate.entity.DefaultEntity;
import com.sdm.core.hibernate.entity.UIStructure;

@Audited
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
	@Column(name = "id", unique = true, nullable = false, columnDefinition = "INT UNSIGNED")
	private long id;

	@UIStructure(order = 1, label = "Name", inputType = "text")
	@Column(name = "name", columnDefinition = "varchar(50)", length = 50, nullable = false)
	private String name;

	@UIStructure(order = 2, label = "Description", inputType = "text")
	@Column(name = "name", columnDefinition = "varchar(255)", length = 255, nullable = false)
	private String description;

	@UIStructure(order = 2, label = "State", inputType = "text")
	@Column(name = "state", columnDefinition = "varchar(500)", length = 500, nullable = false)
	private String state;

	@UIStructure(order = 3, label = "Icon", inputType = "icon")
	@Column(name = "icon", columnDefinition = "varchar(50)", length = 50, nullable = false)
	private String icon;

	/**
	 * Supported Types : module, toggle, link
	 */
	@UIStructure(order = 4, label = "Type", inputType = "text")
	@Column(name = "type", columnDefinition = "varchar(10)", length = 10, nullable = false)
	private String type;

	@UIStructure(order = 5, label = "Is separator?", inputType = "checkbox")
	@Column(name = "name", columnDefinition = "bit(1)", nullable = false)
	private boolean separator;

	public MenuEntity() {

	}

	public MenuEntity(String search, long id, String name, String state, String icon, String type, boolean separator) {
		super();
		this.search = search;
		this.id = id;
		this.name = name;
		this.state = state;
		this.icon = icon;
		this.type = type;
		this.separator = separator;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public boolean isSeparator() {
		return separator;
	}

	public void setSeparator(boolean separator) {
		this.separator = separator;
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
