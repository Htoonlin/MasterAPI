/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.mysql.model.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sdm.mysql.model.SQLModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Htoonlin
 */
@JsonIgnoreProperties(value = {"row_counts_col", "all_columns", "group_by_column", "hasAggregate"})
public class Query extends SQLModel implements Serializable {

    public final String ROW_COUNTS_COL = "total_rows";
    private String table;
    private String alias;
    private List<Join> joins;
    private List<Column> columns;
    private boolean allColumns;
    private List<Condition> where;
    private List<Condition> having;
    private Map<String, Sort> sort;
    private List<Object> paramValues;
    private int page;
    private int size;

    private String groupByColumn = "";
    private boolean hasAggregate;

    private String buildColumn() {
        String sql = "";
        if (columns != null && columns.size() > 0) {
            if (allColumns) {
                sql += "*,";
            }
            for (Column column : columns) {
                if (column.getAggregate() == null
                        || column.getAggregate().equals(Aggregate.NON)) {
                    groupByColumn += " " + column.getName() + ",";
                } else {
                    hasAggregate = true;
                }
                sql += " " + column.defaultSQL() + ",";
            }
            groupByColumn = cleanLastChar(groupByColumn, ",");
            sql = cleanLastChar(sql, ",");
        } else {
            sql += "*";
        }
        return sql;
    }

    private String buildFrom() {
        //Table Name
        String sql = " FROM " + quoteName(table);
        //Table alias name
        if (this.alias != null && !this.alias.isEmpty()) {
            sql += " AS " + quoteName(alias);
        }
        return sql;
    }

    private String buildWhere() {
        //SQL Injection for other conditions
        String sql = " WHERE 1 = 1";

        //Build WHERE Conditions
        if (where != null && where.size() > 0) {
            for (Condition condition : where) {
                sql += " " + condition.defaultSQL();
                paramValues.add(condition.getValue());
            }
        }

        return sql;
    }

    private String buildJoins() {
        String sql = "";
        if (this.joins != null && this.joins.size() > 0) {
            for (Join join : joins) {
                sql += " " + join.defaultSQL();
            }
        }
        return sql;
    }

    private String buildGroupBy() {
        String sql = "";
        if (hasAggregate) {
            sql += " GROUP BY " + groupByColumn;
        }
        return sql;
    }

    private String buildHaving() {
        String sql = "";
        //Build Having Condition
        if (hasAggregate && having != null && having.size() > 0) {
            sql += " HAVING";
            for (Condition condition : having) {
                sql += " " + condition.defaultSQL();
                paramValues.add(condition.getValue());
            }
        }
        return sql;
    }

    private String buildOrderBy() {
        String sql = "";
        if (sort != null && sort.size() > 0) {
            sql += " ORDER BY";
            for (String string : sort.keySet()) {
                sql += " " + string + " " + sort.get(string).toString() + ", ";
            }
            sql = cleanLastChar(sql, ", ");
        }
        return sql;
    }

    private String buildLimit() {
        if (page <= 0) {
            page = 1;
        }
        int start = size * (page - 1);
        return " LIMIT " + start + "," + size;
    }

    @Override
    public String defaultSQL() {
        String sql = "SELECT " + buildColumn() + " " + buildFrom();
        sql += buildJoins();
        sql += buildWhere();
        sql += buildGroupBy() + buildHaving();
        sql += buildOrderBy();
        sql += buildLimit();
        return sql;
    }

    public String rowCountSQL() {
        String sql = "SELECT COUNT(*) AS " + quoteName(ROW_COUNTS_COL) + " " + buildFrom();
        sql += buildJoins();
        sql += buildWhere();
        sql += buildGroupBy() + buildHaving();
        return sql;
    }

    public Query() {
        paramValues = new ArrayList<>();
        this.page = 1;
        this.size = 10;
    }

    public Query(String table) {
        this();
        this.table = table;
    }

    public List<Object> getParamValues() {
        return paramValues;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public boolean isAllColumns() {
        return allColumns;
    }

    public void setAllColumns(boolean allColumns) {
        this.allColumns = allColumns;
    }

    public List<Condition> getWhere() {
        return where;
    }

    public void setWhere(List<Condition> where) {
        this.where = where;
    }

    public List<Condition> getHaving() {
        return having;
    }

    public void setHaving(List<Condition> having) {
        this.having = having;
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
