package com.sdm.generate.resource;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sdm.core.di.ITemplateManager;
import com.sdm.core.resource.DefaultResource;
import com.sdm.generate.model.ObjectModel;

@Path("generate")
public class GenerateResource extends DefaultResource{	
	@Inject 
	private ITemplateManager templateManager;
	
	@Path("entity")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String generateEntity(@Valid ObjectModel request) {
		String javaSource = templateManager.buildTemplate("generate/entity.jsp", request.getTemplateData());
		return javaSource;
	}
}
