/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.request;

import java.util.Date;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sdm.core.request.IBaseRequest;

/**
 *
 * @author Htoonlin
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FacebookAuthRequest implements IBaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -637231624670219448L;

	private String accessToken;

	private String deviceId;

	private Date timestamp;

	@NotBlank(message = "DeviceID can't be blank.")
	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String value) {
		this.deviceId = value;
	}

	@NotBlank(message = "Token can't be blank.")
	@Size(min = 6, max = 255)
	public String getAccessToken() {
		return this.accessToken;
	}

	public void setAccessToken(String value) {
		this.accessToken = value;
	}

	@Override
	public Date getTimestamp() {
		return this.timestamp;
	}

	@Override
	public void setTimestamp(long date) {
		this.timestamp = new Date(date);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessToken == null) ? 0 : accessToken.hashCode());
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
		FacebookAuthRequest other = (FacebookAuthRequest) obj;
		if (accessToken == null) {
			if (other.accessToken != null)
				return false;
		} else if (!accessToken.equals(other.accessToken))
			return false;
		return true;
	}

}
