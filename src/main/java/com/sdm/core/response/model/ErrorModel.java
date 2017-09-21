package com.sdm.core.response.model;

import java.io.Serializable;

import javax.validation.ConstraintViolation;

public class ErrorModel implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7819189941075195509L;

	private String message;
	private Object invalidValue;

	public ErrorModel() {

	}

	public ErrorModel(String message, Object value) {
		this.message = message;
		this.invalidValue = value;
	}

	public ErrorModel(ConstraintViolation<?> error) {
		this.message = error.getMessage();
		this.invalidValue = error.getInvalidValue();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getInvalidValue() {
		return invalidValue;
	}

	public void setInvalidValue(Object invalidValue) {
		this.invalidValue = invalidValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((invalidValue == null) ? 0 : invalidValue.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
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
		ErrorModel other = (ErrorModel) obj;
		if (invalidValue == null) {
			if (other.invalidValue != null) {
				return false;
			}
		} else if (!invalidValue.equals(other.invalidValue)) {
			return false;
		}
		if (message == null) {
			if (other.message != null) {
				return false;
			}
		} else if (!message.equals(other.message)) {
			return false;
		}
		return true;
	}

}
