/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.mysql.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Htoonlin
 */
public class ObjectModel implements Serializable {

    private String name;
    private String engine;
    private int version;
    private long dataCount;
    private long currentSerial;
    private Date createdDate;

    public ObjectModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getDataCount() {
        return dataCount;
    }

    public void setDataCount(long dataCount) {
        this.dataCount = dataCount;
    }

    public long getCurrentSerial() {
        return currentSerial;
    }

    public void setCurrentSerial(long currentSerial) {
        this.currentSerial = currentSerial;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

}
