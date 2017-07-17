package com.sdm.facebook.resource;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sdm.core.resource.DefaultResource;
import com.sdm.core.response.IBaseResponse;

@Path("facebook/auth")
public class AuthResoure extends DefaultResource {
	
	@PermitAll
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse authProcess(@QueryParam("token") String token) {
		return null;
	}
}
