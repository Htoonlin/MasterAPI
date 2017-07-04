/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Htoonlin

 */
public class SyncResponse implements Serializable {

    private final long syncTime;
    private List insert;
    private List update;
    private List remove;

    public SyncResponse() {
        this.syncTime = (new Date()).getTime();
        insert = new ArrayList<>();
        update = new ArrayList<>();
        remove = new ArrayList<>();
    }

    public long getSyncTime() {
        return syncTime;
    }

    public List getInsert() {
        return insert;
    }

    public void setInsert(List insert) {
        this.insert = insert;
    }

    public List getUpdate() {
        return update;
    }

    public void setUpdate(List update) {
        this.update = update;
    }

    public List getRemove() {
        return remove;
    }

    public void setRemove(List remove) {
        this.remove = remove;
    }

}
