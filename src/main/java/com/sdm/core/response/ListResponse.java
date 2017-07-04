/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Htoonlin
 */
@JsonPropertyOrder({"count","data"})
public class ListResponse implements Serializable{
    private static final long serialVersionUID = 1L;
    
    public ListResponse(List data){
        this.data = data;
    }
       
    public int getCount(){
        return this.data.size();
    }

    private List data;

    public List getData() {
        return this.data;
    }

    public void setData(List value) {
        this.data = value;
    }
}
