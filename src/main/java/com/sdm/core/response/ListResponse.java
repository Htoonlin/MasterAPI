/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 * @author Htoonlin
 */
@JsonPropertyOrder({ "code", "status", "count", "content", "timestamp" })
public class ListResponse<T extends Serializable> extends DefaultResponse<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 522782444980983172L;

	public ListResponse(List<T> data) {
		this.data = data;
	}

	public int getCount() {
		return this.data.size();
	}

	private List<T> data;

	public void addData(T entity) {
		if (this.data == null) {
			this.data = new ArrayList<>();
		}
		this.data.add(entity);
	}

	@Override
	public List<T> getContent() {
		return this.data;
	}

	public void setContent(List<T> value) {
		this.data = value;
	}
}
