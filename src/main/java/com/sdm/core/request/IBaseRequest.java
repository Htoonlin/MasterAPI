/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.request;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 *
 * @author Htoonlin
 */
public interface IBaseRequest extends Serializable {
	@JsonSetter("timestamp")
	void setTimeStamp(long date);

	@JsonIgnore
	Map<String, String> getErrors();

	@JsonIgnore
	boolean isValid();
}
