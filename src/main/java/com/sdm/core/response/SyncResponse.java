/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response;

import com.sdm.core.database.entity.RestEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Htoonlin
 * @param <T>
 */
public class SyncResponse<T extends RestEntity> implements IResponseContent, Serializable {

    private final long syncTime;
    private List<T> insert;
    private List<T> update;
    private List<T> remove;

    public SyncResponse() {
        this.syncTime = (new Date()).getTime();
        insert = new ArrayList<>();
        update = new ArrayList<>();
        remove = new ArrayList<>();
    }

    public long getSyncTime() {
        return syncTime;
    }

    public List<T> getInsert() {
        return insert;
    }

    public void setInsert(List<T> insert) {
        this.insert = insert;
    }

    public void addInsert(T entity) {
        insert.add(entity);
    }

    public List<T> getUpdate() {
        return update;
    }

    public void setUpdate(List<T> update) {
        this.update = update;
    }

    public void addUpdate(T entity) {
        update.add(entity);
    }

    public List<T> getRemove() {
        return remove;
    }

    public void setRemove(List<T> remove) {
        this.remove = remove;
    }

    public void addRemove(T entity) {
        remove.add(entity);
    }

    @Override
    public int getResponseCode() {
        return 200;
    }

    @Override
    public ResponseType getResponseStatus() {
        return ResponseType.SUCCESS;
    }

}
