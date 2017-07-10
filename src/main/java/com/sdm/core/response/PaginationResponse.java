/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sdm.core.request.QueryRequest;

/**
 *
 * @author Htoonlin
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder(value = { "code", "status", "total", "count", "current_page", "page_size", "page_count", "query",
		"content", "extra", "timestamp" })
public class PaginationResponse<T extends Serializable> extends DefaultResponse<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5020402341964835561L;
	private List<T> data;
	private long total;
	private int currentPage;
	private int pageSize;
	private QueryRequest query;

	public PaginationResponse() {
	}

	public PaginationResponse(List<T> data, long total, int currentPage, int pageSize) {
		this.data = data;
		this.total = total;
		this.currentPage = currentPage;
		this.pageSize = pageSize;
	}

	public PaginationResponse(List<T> data, long total, int currentPage, int pageSize, QueryRequest query) {
		this.data = data;
		this.total = total;
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.query = query;
	}

	public double getPageCount() {
		return Math.ceil((double) total / pageSize);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCount() {
		return this.data.size();
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public QueryRequest getQuery() {
		return query;
	}

	public void setQuery(QueryRequest query) {
		this.query = query;
	}

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
