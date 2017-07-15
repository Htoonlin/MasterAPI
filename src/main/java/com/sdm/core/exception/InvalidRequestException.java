package com.sdm.core.exception;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.WebApplicationException;

public class InvalidRequestException extends WebApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1809767050392811636L;

	private Map<String, String> errors;

	public InvalidRequestException(Map<String, String> errors) {
		super();
		this.errors = errors;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void addError(String property, String value) {
		if (this.errors == null) {
			this.errors = new HashMap<>();
		}
		this.errors.put(property, value);
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}
}
