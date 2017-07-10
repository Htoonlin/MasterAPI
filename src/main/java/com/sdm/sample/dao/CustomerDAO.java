/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.sample.dao;

import org.hibernate.Session;

import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.sample.entity.CustomerEntity;

/**
 *
 * @author htoonlin
 */
public class CustomerDAO extends RestDAO {

    public CustomerDAO(long userId) {
        super(CustomerEntity.class.getName(), userId);
    }

    public CustomerDAO(Session session, long userId) {
        super(session, CustomerEntity.class.getName(), userId);
    }
}
