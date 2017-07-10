/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.request.model;

import java.io.Serializable;
import java.util.List;

import com.sdm.core.util.MySQLManager;

/**
 *
 * @author Htoonlin
 */
public class Join implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7753093578147409366L;
	private String table;
	private String alias;
	private JoinType join;
	private List<Condition> conditions;

	public String defaultSQL() {
		String sql = join + " JOIN";
		sql += " " + MySQLManager.quoteName(this.table);
		if (alias != null && !alias.isEmpty()) {
			sql += " AS " + MySQLManager.quoteName(this.alias);
		}

		if (conditions != null && conditions.size() > 0) {
			// Sql Inject for conditions
			sql += " ON 1 = 1";
			for (Condition condition : conditions) {
				sql += " " + condition.getLogic().getValue() + " " + condition.getColumn() + " "
						+ condition.getOperator().getValue() + condition.getValue().toString();
			}
		}

		return sql;
	}

	public Join() {
		this.join = JoinType.INNER;
	}

	public Join(String table, JoinType join, List<Condition> conditions) {
		this.table = table;
		this.join = join;
		this.conditions = conditions;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public JoinType getJoin() {
		return join;
	}

	public void setJoin(JoinType join) {
		this.join = join;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

}
