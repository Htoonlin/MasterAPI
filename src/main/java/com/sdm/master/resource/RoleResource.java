/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import com.sdm.core.resource.RestResource;
import com.sdm.master.entity.RoleEntity;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;

/**
 *
 * @author Htoonlin
 */
@Path("role")
public class RoleResource extends RestResource<RoleEntity, Integer> {
    private static final Logger LOG = Logger.getLogger(RoleResource.class.getName());
}
