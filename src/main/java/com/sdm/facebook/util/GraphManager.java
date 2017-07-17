package com.sdm.facebook.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sdm.core.Constants;
import com.sdm.core.filter.JsonProvider;

public class GraphManager {
	private UriBuilder graphURL;
	private final String accessToken;
	private List<String> fields;
	
	public GraphManager(String accessToken, String node) {
		super();
		this.accessToken = accessToken;
		this.fields = new ArrayList<>();
		graphURL = UriBuilder.fromUri(Constants.Facebook.GRAPH_API)
				.path(Constants.Facebook.API_VERSION).path(node);
	}
	
	public GraphManager setPath(String path) {
		this.graphURL.path(path);
		return this;
	}
	
	public GraphManager setFields(List<String> fields) {
		this.fields = fields;
		return this;
	}
	
	public GraphManager addField(String fieldName) {
		this.fields.add(fieldName);
		return this;
	}
	
	private WebTarget buildRequest() {
		Client client = ClientBuilder.newClient().register(JsonProvider.class);
		WebTarget target = client.target(this.graphURL)
				.queryParam("access_token", this.accessToken)
				.queryParam("fields", String.join(",", this.fields));
		return target;
	}

	public <T extends Serializable> T getRequest(Class<T> resClass) {
		WebTarget target = this.buildRequest();
		return target.request(MediaType.APPLICATION_JSON).get(resClass);
	}

	public <T extends Serializable> T postRequest(String url, Entity data, Class<T> resClass) {
		WebTarget target = this.buildRequest();
		return target.request(MediaType.APPLICATION_JSON).post(data, resClass);
	}
}
