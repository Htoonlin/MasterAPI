/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.sample.dao;

import com.sdm.core.hibernate.dao.RestDAO;
import org.hibernate.Session;

/**
 *
 * @author htoonlin
 */
public class CustomerDAO extends RestDAO {

    public CustomerDAO(long userId) {
        super(userId);
    }

    public CustomerDAO(Session session, long userId) {
        super(session, userId);
    }

    @Override
    protected boolean useVersion() {
        return true;
    }

    @Override
    protected boolean useLog() {
        return true;
    }

    @Override
    protected boolean useTimeStamp() {
        return true;
    }

    @Override
    protected boolean useSoftDelete() {
        return true;
    }

    @Override
    protected String getEntityName() {
        return "Sample_Customer";
    }
}
