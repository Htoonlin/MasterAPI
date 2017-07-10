/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.request.model;

import java.io.Serializable;

import com.sdm.core.util.MySQLManager;

/**
 *
 * @author Htoonlin
 */
public class Condition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8334415693924288546L;
	private Logical logic;
	private Operator operator;
	private String column;
	private Object value;

	public String defaultSQL() {
		return logic.getValue() + " " + MySQLManager.quoteName(column) + " " + operator.getValue() + " ?";
	}

	public Condition() {
	}

	public Condition(Logical logic, Operator operator, String column, Object value) {
		this.logic = logic;
		this.operator = operator;
		this.column = column;
		this.value = value;
	}

	public Logical getLogic() {
		return logic;
	}

	public void setLogic(Logical logic) {
		this.logic = logic;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
