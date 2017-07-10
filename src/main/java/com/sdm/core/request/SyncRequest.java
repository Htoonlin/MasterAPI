/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.request;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.sdm.core.hibernate.entity.DefaultEntity;

/**
 *
 * @author Htoonlin
 */
public class SyncRequest<T extends Serializable> extends DefaultEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8588501696126454404L;
	private long lastSync;
	private List<T> insert;
	private List<T> update;
	private List<T> remove;

	public SyncRequest() {
	}

	@NotNull
	public long getLastSync() {
		return lastSync;
	}

	public void setLastSync(long lastSync) {
		this.lastSync = lastSync;
	}

	public List<T> getInsert() {
		return insert;
	}

	public void setInsert(List<T> insert) {
		this.insert = insert;
	}

	public List<T> getUpdate() {
		return update;
	}

	public void setUpdate(List<T> update) {
		this.update = update;
	}

	public List<T> getRemove() {
		return remove;
	}

	public void setRemove(List<T> remove) {
		this.remove = remove;
	}
}
