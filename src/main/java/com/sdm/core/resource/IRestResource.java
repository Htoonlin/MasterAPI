/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.resource;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sdm.core.request.IBaseRequest;
import com.sdm.core.response.IBaseResponse;

/**
 *
 * @author Htoonlin
 * @param <T>
 * @param <PK>
 */
public interface IRestResource<T extends IBaseRequest, PK extends Serializable> {

	@GET
	@Path("q")
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse getNamedQueries();

	@POST
	@Path("q/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse postQuery(@PathParam("name") String queryName, Map<String, Object> params);

	@GET
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse getAll() throws Exception;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse getPaging(@DefaultValue("") @QueryParam("filter") String filter,
			@DefaultValue("1") @QueryParam("page") int pageId, @DefaultValue("10") @QueryParam("size") int pageSize,
			@DefaultValue("id:ASC") @QueryParam("sort") String sort);

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse getById(@PathParam("id") PK id);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse create(@Valid T request);
	
	@POST
	@Path("multi")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse createByList(@Valid List<T> request);

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse update(@Valid T request, @PathParam("id") PK id);

	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse remove(@PathParam("id") PK id);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("struct")
	public IBaseResponse getStructure();
}
