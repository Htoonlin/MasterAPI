/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.resource;

import com.sdm.core.request.SyncRequest;
import com.sdm.core.response.IBaseResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Htoonlin
 */
public interface IUtilResource {

    /*
    @POST
    @Path("query")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse queryData(QueryRequest request) throws Exception; */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("struct")
    public IBaseResponse getStructure() throws Exception;

    @POST
    @Path("sync")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse syncData(SyncRequest request) throws Exception;

}
