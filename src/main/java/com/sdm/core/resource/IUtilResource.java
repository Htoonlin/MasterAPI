/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.resource;

import java.io.Serializable;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sdm.core.request.QueryRequest;
import com.sdm.core.request.SyncRequest;
import com.sdm.core.response.IBaseResponse;

/**
 *
 * @author Htoonlin
 */
public interface IUtilResource {

	@POST
	@Path("query")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse queryData(QueryRequest request) throws Exception;

	@POST
	@Path("sync")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public <T extends Serializable> IBaseResponse syncData(SyncRequest<T> request) throws Exception;

}
