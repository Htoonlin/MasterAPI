/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.hibernate.entity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdm.core.Globalizer;
import com.sdm.core.Setting;
import com.sdm.core.request.IBaseRequest;
import com.sdm.core.response.model.UIProperty;

/**
 *
 * @author Htoonlin
 */
public class DefaultEntity implements Serializable, IBaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1235673932545866165L;

	private Date timestamp;

	public Date getTimestamp() {
		return this.timestamp;
	}

	@Override
	public void setTimeStamp(long date) {
		this.timestamp = new Date(date);
	}

	private Map<String, String> errors;

	protected void addError(String key, String value) {
		if (errors == null) {
			errors = new HashMap<>();
		}
		errors.put(key, value);
	}

	@Override
	public Map<String, String> getErrors() {
		return errors;
	}

	@Override
	public boolean isValid() {
		String env = Setting.getInstance().get(Setting.SECURITY_TIMESTAMP_LIFE, "beta");
		if (!env.equalsIgnoreCase("dev")) {
			if (timestamp == null || !Globalizer.validTimeStamp(timestamp)) {
				addError("timestamp", "Invalid timestamp.");
				return false;
			}
		}
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<DefaultEntity>> violoationSet = validator.validate(this);
		for (ConstraintViolation<DefaultEntity> v : violoationSet) {
			String propertyName = Globalizer.camelToLowerUnderScore(v.getPropertyPath().toString());
			addError(propertyName, v.getMessage());
		}

		return violoationSet.isEmpty();
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
					property.setNullable(column.nullable());
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
}
