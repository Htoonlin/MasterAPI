/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.request.model;

/**
 *
 * @author Htoonlin
 */
public enum Logical {
	SHOULD("OR"), MUST("AND");

	private final String value;

	private Logical(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
