/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.request;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sdm.core.request.DefaultRequest;

/**
 *
 * @author Htoonlin
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ActivateRequest extends DefaultRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4541648496908228267L;

	private String email;

	private String token;

	private String deviceId;

	@NotBlank(message = "DeviceID can't be blank.")
	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String value) {
		this.deviceId = value;
	}

	@Email(message = "Ivalid email format.")
	@NotBlank(message = "Email can't be blank.")
	@Size(min = 6, max = 255)
	public String getEmail() {
		return this.email;
	}

	@NotBlank(message = "Token can't be blank.")
	@Size(min = 6, max = 255)
	public String getToken() {
		return this.token;
	}

	public void setEmail(String value) {
		this.email = value;
	}

	public void setToken(String value) {
		this.token = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActivateRequest other = (ActivateRequest) obj;
		if (deviceId == null) {
			if (other.deviceId != null)
				return false;
		} else if (!deviceId.equals(other.deviceId))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}

}
