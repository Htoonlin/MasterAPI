/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import javax.annotation.PostConstruct;
import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.core.resource.RestResource;
import com.sdm.master.dao.RoleDAO;
import com.sdm.master.entity.RoleEntity;

/**
 *
 * @author Htoonlin
 */
@Path("role")
public class RoleResource extends RestResource<RoleEntity, Long> {

    private static final Logger LOG = Logger.getLogger(RoleResource.class.getName());

    private RoleDAO mainDAO;

    @PostConstruct
    private void init() {
        if (this.mainDAO == null) {
            this.mainDAO = new RoleDAO();
        }
    }

    @Override
    protected RestDAO getDAO() {
        return this.mainDAO;
    }

    @Override
    protected Logger getLogger() {
        return RoleResource.LOG;
    }
}
