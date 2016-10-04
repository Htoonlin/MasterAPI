/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.resource;

import com.sdm.core.database.entity.RestEntity;
import com.sdm.core.request.QueryRequest;
import com.sdm.core.request.SyncRequest;
import com.sdm.core.response.DefaultResponse;
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
 * @param <T>
 */
public interface IUtilResource<T extends RestEntity> {

    @POST
    @Path("query")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse queryData(QueryRequest request) throws Exception;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("struct")
    public DefaultResponse getStructure();

    @POST
    @Path("sync")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse syncData(SyncRequest<T> request) throws Exception;

}
