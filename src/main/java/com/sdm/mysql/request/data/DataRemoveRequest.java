/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.mysql.request.data;

import com.sdm.mysql.model.query.Condition;
import com.sdm.core.request.DefaultRequest;
import java.util.List;

/**
 *
 * @author Htoonlin
 */
public class DataRemoveRequest extends DefaultRequest {

    private boolean truncate;
    private boolean hardDelete;
    private List<Condition> conditions;

    public DataRemoveRequest() {
    }

    public DataRemoveRequest(boolean hardDelete, List<Condition> conditions) {
        this.hardDelete = hardDelete;
        this.conditions = conditions;
    }

    public boolean isTruncate() {
        return truncate;
    }

    public void setTruncate(boolean truncate) {
        this.truncate = truncate;
    }

    public boolean isHardDelete() {
        return hardDelete;
    }

    public void setHardDelete(boolean hardDelete) {
        this.hardDelete = hardDelete;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

}
