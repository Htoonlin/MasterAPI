/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.request;

import com.sdm.core.request.query.Alias;
import com.sdm.core.request.query.Condition;
import com.sdm.core.request.query.Sort;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Htoonlin
 */
public class QueryRequest extends DefaultRequest implements Serializable {

    private List<Alias> columns;
    private List<Condition> query;
    private Map<String, Sort> sort;
    private int page;
    private int size;

    public QueryRequest(List<Condition> query, Map<String, Sort> sort, int page, int size) {
        this.query = query;
        this.sort = sort;
        this.page = page;
        this.size = size;
    }

    public QueryRequest() {
        page = 1;
        size = 10;
    }

    public List<Alias> getColumns() {
        return columns;
    }

    public void setColumns(List<Alias> columns) {
        this.columns = columns;
    }

    public List<Condition> getQuery() {
        return query;
    }

    public void setQuery(List<Condition> query) {
        this.query = query;
    }

    public Map<String, Sort> getSort() {
        return sort;
    }

    public void setSort(Map<String, Sort> sort) {
        this.sort = sort;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
