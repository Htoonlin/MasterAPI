/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sdm.core.request.query.Sort;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Htoonlin
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder(value = {"total", "count", "current_page", "page_size", "page_count", "sort", "data"})
public class PaginationResponse implements IResponseContent, Serializable {

    private static final long serialVersionUID = 1L;

    private long total;
    private int currentPage;
    private int pageSize;
    private Map<String, Sort> sort;

    public PaginationResponse(long total, int currentPage, int pageSize, Map<String, Sort> sort, List data) {
        this.total = total;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.data = data;
        this.sort = sort;
    }

    public int getPageCount() {
        return (int) Math.ceil((double) total / pageSize);
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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Map<String, Sort> getSort() {
        return sort;
    }

    public void setSort(Map<String, Sort> sort) {
        this.sort = sort;
    }

    private List data;

    public List getData() {
        return this.data;
    }

    public void setData(List value) {
        this.data = value;
    }

    @Override
    public int getResponseCode() {
        return 200;
    }

    @Override
    public ResponseType getResponseStatus() {
        return ResponseType.SUCCESS;
    }
}
