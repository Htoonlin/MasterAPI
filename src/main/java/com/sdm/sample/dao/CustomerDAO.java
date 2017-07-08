/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.sample.dao;

import org.hibernate.Session;

import com.sdm.core.hibernate.dao.RestDAO;

/**
 *
 * @author htoonlin
 */
public class CustomerDAO extends RestDAO {

    private static final String ENTITY = "Sample_CustomerEntity";

    public CustomerDAO(long userId) {
        super(ENTITY, userId);
    }

    public CustomerDAO(Session session, long userId) {
        super(session, ENTITY, userId);
    }
}
