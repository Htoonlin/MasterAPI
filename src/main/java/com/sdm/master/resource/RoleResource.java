/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.core.resource.RestResource;
import com.sdm.master.dao.RoleDAO;
import com.sdm.master.entity.RoleEntity;
import javax.annotation.PostConstruct;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;

/**
 *
 * @author Htoonlin
 */
@Path("roles")
public class RoleResource extends RestResource<RoleEntity, Integer> {

    private static final Logger LOG = Logger.getLogger(RoleResource.class.getName());

    private RoleDAO mainDAO;

    @PostConstruct
    private void init() {
        if (this.mainDAO == null) {
            this.mainDAO = new RoleDAO(getUserId());
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
