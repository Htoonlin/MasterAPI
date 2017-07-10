/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 * @author Htoonlin
 */
@JsonPropertyOrder(value = { "path", "method", "resource_class", "resource_method" })
public class RouteInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6757987371372221883L;
	private String resourceClass;
	private String resourceMethod;
	private String path;
	private String method;

	public String getResourceClass() {
		return resourceClass;
	}

	public void setResourceClass(String resourceClass) {
		this.resourceClass = resourceClass;
	}

	public String getResourceMethod() {
		return resourceMethod;
	}

	public void setResourceMethod(String resourceMethod) {
		this.resourceMethod = resourceMethod;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
