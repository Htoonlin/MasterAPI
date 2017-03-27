/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.mysql.resource;

import com.sdm.core.resource.DefaultResource;
import com.sdm.core.response.IBaseResponse;
import com.sdm.mysql.request.ObjectRequest;
import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author Htoonlin
 */
@Path("mysql/object")
public class ObjectResource extends DefaultResource{
    private static final Logger LOG = Logger.getLogger(ObjectResource.class.getName());
    
    @PostConstruct
    public void onLoad(){
        LOG.info("Starting Object Manager");
    }
    
    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse create(ObjectRequest model){        
        return null;
    }
    
    @POST
    @Path("rename")
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse rename(){
        return null;
    }
}
