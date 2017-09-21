/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 * @author Htoonlin
 */
@JsonPropertyOrder({ "count", "data" })
public class ListModel<T extends Serializable> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 522782444980983172L;

	public ListModel() {

	}

	public ListModel(List<T> data) {
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

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
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
		ListModel other = (ListModel) obj;
		if (data == null) {
			if (other.data != null) {
				return false;
			}
		} else if (!data.equals(other.data)) {
			return false;
		}
		return true;
	}
}
