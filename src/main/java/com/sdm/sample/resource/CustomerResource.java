/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.sample.resource;

import javax.annotation.PostConstruct;
import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.core.resource.RestResource;
import com.sdm.sample.dao.CustomerDAO;
import com.sdm.sample.entity.CustomerEntity;

/**
 *
 * @author Htoonlin
 */
@Path("sample/customer")
public class CustomerResource extends RestResource<CustomerEntity, Integer> {

    private static final Logger LOG = Logger.getLogger(CustomerResource.class.getName());
    private RestDAO mainDAO;

    @Override
    protected Logger getLogger() {
        return CustomerResource.LOG;
    }

    @PostConstruct
    private void init() {
        mainDAO = new CustomerDAO(getUserId());
    }

    @Override
    protected RestDAO getDAO() {
        return this.mainDAO;
    }

}
