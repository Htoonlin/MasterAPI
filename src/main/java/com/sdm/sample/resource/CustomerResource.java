/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.sample.resource;

import com.sdm.core.resource.RestResource;
import com.sdm.sample.entity.CustomerEntity;
import org.apache.log4j.Logger;
import javax.ws.rs.Path;

/**
 *
 * @author Htoonlin
 */
@Path("sample/customer")
public class CustomerResource extends RestResource<CustomerEntity, Long> {

    private static final Logger LOG = Logger.getLogger(CustomerResource.class.getName());

}