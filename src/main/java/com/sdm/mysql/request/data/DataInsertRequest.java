/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.mysql.request.data;

import com.sdm.core.request.DefaultRequest;
import java.util.Map;

/**
 *
 * @author Htoonlin
 */
public class DataInsertRequest extends DefaultRequest {

    private Map<String, Object> data;
    private boolean generatedId;

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public boolean isGeneratedId() {
        return generatedId;
    }

    public void setGeneratedId(boolean generatedId) {
        this.generatedId = generatedId;
    }

}
