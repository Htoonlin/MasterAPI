/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Htoonlin
 * 
 */
public class SyncContent<T extends Serializable> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9168849350481369905L;
	private final long syncTime;
	private List<T> insert;
	private List<T> update;
	private List<T> remove;

	public SyncContent() {
		this.syncTime = (new Date()).getTime();
		insert = new ArrayList<>();
		update = new ArrayList<>();
		remove = new ArrayList<>();
	}

	public long getSyncTime() {
		return syncTime;
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
