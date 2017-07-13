/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.request;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.sdm.core.Globalizer;
import com.sdm.core.Setting;

/**
 *
 * @author Htoonlin
 */
public class DefaultRequest implements IBaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8881597082622307013L;
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
		String env = Setting.getInstance().get(Setting.SYSTEM_ENV, "beta");
		if (!env.equalsIgnoreCase("dev")) {
			if (timestamp == null || !Globalizer.validTimeStamp(timestamp)) {
				addError("timestamp", "Invalid timestamp.");
				return false;
			}
		}
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<DefaultRequest>> violoationSet = validator.validate(this);
		for (ConstraintViolation<DefaultRequest> v : violoationSet) {
			String propertyName = Globalizer.camelToLowerUnderScore(v.getPropertyPath().toString());
			addError(propertyName, v.getMessage());
		}

		return violoationSet.isEmpty();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((errors == null) ? 0 : errors.hashCode());
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
		DefaultRequest other = (DefaultRequest) obj;
		if (errors == null) {
			if (other.errors != null)
				return false;
		} else if (!errors.equals(other.errors))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}

}
