<%@ page language="java" contentType="text/x-java-source,java; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core" %>

package com.sdm.${moduleName}.entity;    
    
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.ws.rs.core.UriBuilder;

import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdm.core.hibernate.entity.DefaultEntity;
import com.sdm.core.response.LinkModel;
import com.sdm.core.ui.UIStructure;
import com.sdm.${moduleName}.resource.${name}Resource;   

<core:if test="${auditable}">@Audited</core:if>
@Entity(name = "${moduleName}_${name}Entity")
@Table(name = "${tableName}")
public class ${name}Entity extends DefaultEntity implements Serializable {
	private static final long serialVersionUID = 1L;
}