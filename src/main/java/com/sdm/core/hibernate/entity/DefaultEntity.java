/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.hibernate.entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdm.core.request.IBaseRequest;
import com.sdm.core.ui.UIProperty;
import com.sdm.core.ui.UIStructure;

/**
 *
 * @author Htoonlin
 */
public class DefaultEntity implements IBaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1235673932545866165L;
	
	private Date timestamp;

	public Date getTimestamp() {
		return this.timestamp;
	}

	@Override
	public void setTimestamp(long date) {
		this.timestamp = new Date(date);
	}

	@JsonIgnore
	public HashMap<String, Object> getQueries() {
		HashMap<String, Object> queries = new HashMap<>();
		NamedQueries namedQueries = this.getClass().getAnnotation(NamedQueries.class);
		if (namedQueries != null) {
			for (NamedQuery query : namedQueries.value()) {
				queries.put(query.name(), query.query());
			}
		}

		for (NamedQuery query : this.getClass().getAnnotationsByType(NamedQuery.class)) {
			queries.put(query.name(), query.query());
		}

		return queries;
	}

	@JsonIgnore
	public List<UIProperty> getStructure() {
		List<UIProperty> properties = new ArrayList<>();
		for (Field field : this.getClass().getDeclaredFields()) {
			// Check has annotations
			if (field.getAnnotations().length <= 0) {
				continue;
			}

			// Check JsonIgnore || Transient
			if (field.getAnnotation(JsonIgnore.class) != null || field.getAnnotation(Transient.class) != null) {
				continue;
			}

			UIProperty property = new UIProperty();
			// General info
			property.setName(field.getName());
			property.setType(field.getType().getSimpleName());

			if (field.getAnnotation(Id.class) != null) {
				property.setPrimary(true);
			}

			// UI Info
			UIStructure structure = field.getAnnotation(UIStructure.class);
			if (structure != null) {
				property.setInputType(structure.inputType());
				property.setLabel(structure.label());
				property.setHideInGrid(structure.hideInGrid());
				property.setReadOnly(structure.readOnly());
				property.setOrderIndex(structure.order());
			}

			// Db Info
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				if (column.nullable()) {
					property.setRequired(column.nullable());
				}
				property.setLength(column.length());
			}

			// Validations Info
			properties.add(property);
		}

		Collections.sort(properties, new Comparator<UIProperty>() {
			@Override
			public int compare(UIProperty t1, UIProperty t2) {
				return Integer.compare(t1.getOrderIndex(), t2.getOrderIndex());
			}
		});

		return properties;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
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
		DefaultEntity other = (DefaultEntity) obj;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}

}
