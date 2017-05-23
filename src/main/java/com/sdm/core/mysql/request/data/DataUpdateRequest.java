/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.mysql.request.data;

import com.sdm.core.mysql.model.query.Condition;
import com.sdm.core.request.DefaultRequest;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Htoonlin
 */
public class DataUpdateRequest extends DefaultRequest {

    private Map<String, Object> data;
    private List<Condition> conditions;

    public DataUpdateRequest() {
    }

    public DataUpdateRequest(Map<String, Object> data, List<Condition> conditions) {
        this.data = data;
        this.conditions = conditions;
    }

    @NotNull
    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @NotNull
    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

}
