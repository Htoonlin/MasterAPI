package com.sdm.generate.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdm.core.Globalizer;

public class ObjectModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7804109111579775365L;

	private String name;
	private String tableName;
	private String moduleName;
	private boolean auditable;
	private Set<PropertyModel> properties;
	
	@JsonIgnore
	public Map<String, Object> getTemplateData(){
		Map<String, Object> data = new HashMap<>();
		String packageName = "com.sdm." + moduleName.toLowerCase() + ".entity";
		String resourceName = "com.sdm." + moduleName.toLowerCase() + ".resource." + Globalizer.capitalize(name) + "Resource";
		String entityName = Globalizer.capitalize(name) + "Entity";
		String serial = Long.toString(System.currentTimeMillis()) + "L";
		if(this.properties != null) {
			
		}
		 
		data.put("moduleName", moduleName.toLowerCase());
		data.put("name", this.name);
		data.put("auditable", this.auditable);
		data.put("tableName", this.tableName);
		return data;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public boolean isAuditable() {
		return auditable;
	}

	public void setAuditable(boolean auditable) {
		this.auditable = auditable;
	}

	public Set<PropertyModel> getProperties() {
		return properties;
	}

	public void setProperties(Set<PropertyModel> properties) {
		this.properties = properties;
	}

	public void addProperty(PropertyModel property) {
		if (this.properties == null) {
			this.properties = new HashSet<>();
		}
		this.properties.add(property);
	}
}
