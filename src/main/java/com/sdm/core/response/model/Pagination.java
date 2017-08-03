/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sdm.core.response.LinkModel;

/**
 *
 * @author Htoonlin
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder(value = { "total", "count", "&pagination_links", "current_page", "page_size", "page_count", "data" })
public class Pagination<T extends Serializable> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5020402341964835561L;
	private List<T> data;
	private long total;
	private int currentPage;
	private int pageSize;

	private Map<String, LinkModel> paginationLinks;

	public Pagination() {
	}

	public Pagination(List<T> data, long total, int currentPage, int pageSize) {
		this.data = data;
		this.total = total;
		this.currentPage = currentPage;
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return (int) Math.ceil((double) total / pageSize);
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

	@JsonGetter("&pagination_links")
	public Map<String, LinkModel> getLinks() {
		return paginationLinks;
	}

	public void genreateLinks(Class<?> resourceClass) {
		UriBuilder builder = UriBuilder.fromResource(resourceClass).queryParam("page", "{pageId}");
		this.paginationLinks = new HashMap<>();
		String href = builder.build(this.currentPage).toString();
		this.paginationLinks.put("self", new LinkModel(href));

		href = builder.build(1).toString();
		this.paginationLinks.put("first", new LinkModel(href));

		if (this.currentPage > 1) {
			href = builder.build(this.currentPage - 1).toString();
			this.paginationLinks.put("prev", new LinkModel(href));
		}

		if (this.currentPage < getPageCount()) {
			href = builder.build(this.currentPage + 1).toString();
			this.paginationLinks.put("next", new LinkModel(href));
		}

		href = builder.build(getPageCount()).toString();
		this.paginationLinks.put("last", new LinkModel(href));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + currentPage;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + pageSize;
		result = prime * result + (int) (total ^ (total >>> 32));
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
		Pagination other = (Pagination) obj;
		if (currentPage != other.currentPage)
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (pageSize != other.pageSize)
			return false;
		if (total != other.total)
			return false;
		return true;
	}
}
