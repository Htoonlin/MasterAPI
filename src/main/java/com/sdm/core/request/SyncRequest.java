/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.request;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Htoonlin
 */
public class SyncRequest extends DefaultRequest implements Serializable {

    private long lastSync;
    private List insert;
    private List update;
    private List remove;

    public SyncRequest() {}

    @NotNull
    public long getLastSync() {
        return lastSync;
    }

    public void setLastSync(long lastSync) {
        this.lastSync = lastSync;
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
