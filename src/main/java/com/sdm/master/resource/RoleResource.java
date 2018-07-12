/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import com.sdm.core.resource.RestResource;
import com.sdm.master.entity.RoleEntity;
import javax.ws.rs.Path;

/**
 *
 * @author Htoonlin
 */
@Path("roles")
public class RoleResource extends RestResource<RoleEntity, Integer> {

    public RoleResource() {
        super();
    }

}
