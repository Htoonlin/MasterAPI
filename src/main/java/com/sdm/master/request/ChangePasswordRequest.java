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
public class ChangePasswordRequest extends DefaultRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6890755299063523487L;
	private String email;

	@Email(message = "Invalid email format.")
	@NotBlank(message = "Email field can't be blank.")
	@Size(min = 6, max = 255)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String value) {
		this.email = value;
	}

	private String oldPassword;

	@NotBlank(message = "Password field can't be blank.")
	@Size(min = 6, max = 255)
	public String getOldPassword() {
		return this.oldPassword;
	}

	public void setOldPassword(String value) {
		this.oldPassword = value;
	}

	private String newPassword;

	@NotBlank(message = "New password field can't be blank.")
	@Size(min = 6, max = 255)
	public String getNewPassword() {
		return this.newPassword;
	}

	public void setNewPassword(String value) {
		this.newPassword = value;
	}

	private String token;

	/**
	 * Get the value of token
	 *
	 * @return the value of token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Set the value of token
	 *
	 * @param token
	 *            new value of token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
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
		ChangePasswordRequest other = (ChangePasswordRequest) obj;
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
