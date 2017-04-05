/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.mysql.model.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 *
 * @author Htoonlin
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Boolean.class, name = "boolean"),
    @JsonSubTypes.Type(value = DateTime.class, name = "datetime"),
    @JsonSubTypes.Type(value = Float.class, name = "float"),
    @JsonSubTypes.Type(value = Integer.class, name = "integer"),
    @JsonSubTypes.Type(value = List.class, name = "list"),
    @JsonSubTypes.Type(value = Text.class, name = "text"),
    @JsonSubTypes.Type(value = Relation.class, name = "relation")
})
public interface PropertyType {
    @JsonIgnore
    public String defaultSQL();

    public boolean validType(Object value);
}
