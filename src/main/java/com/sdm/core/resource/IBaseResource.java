/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.sdm.core.response.IBaseResponse;
/**
 *
 * @author Htoonlin
 */
public interface IBaseResource {
    public Object getHttpSession();
    
    public long getUserId();

    public UriInfo getUriInfo();

    public void setUriInfo(UriInfo uriInfo);

    @GET
    @Path("route")
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse getRoutes();
}
