/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.sample.dao;

import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.sample.entity.CustomerEntity;
import org.hibernate.Session;

/**
 *
 * @author htoonlin
 */
public class CustomerDAO extends RestDAO {

    public CustomerDAO(int userId) {
        super(CustomerEntity.class.getName(), userId);
    }

    public CustomerDAO(Session session, int userId) {
        super(session, CustomerEntity.class.getName(), userId);
    }
}
