package com.sdm.facebook.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;

import com.sdm.Constants;
import com.sdm.core.Globalizer;

public class GraphManager {
	private static final Logger LOG = Logger.getLogger(GraphManager.class);

	private UriBuilder graphURL;
	private Map<String, String> queryParams;

	public GraphManager(String accessToken) {
		super();
		graphURL = UriBuilder.fromUri(Constants.Facebook.GRAPH_API).path(Constants.Facebook.API_VERSION);
		this.queryParams = new HashMap<>();
		this.queryParams.put("access_token", accessToken);
	}

	public GraphManager setPath(String path) {
		this.graphURL.path(path);
		return this;
	}

	public GraphManager addQuery(String name, String value) {
		this.queryParams.put(name, value);
		return this;
	}

	private WebTarget buildRequest() {
		Client client = ClientBuilder.newClient();
		for (String name : this.queryParams.keySet()) {
			String value = this.queryParams.get(name);
			if (value != null && value.length() > 0) {
				this.graphURL.queryParam(name, value);
			}
		}
		WebTarget target = client.target(this.graphURL);
		return target;
	}

	public <T extends Serializable> T getRequest(Class<T> resClass) {
		try {
			WebTarget target = this.buildRequest();
			LOG.info("GET Request => " + target.getUri().toString());
			String responseString = target.request(MediaType.APPLICATION_JSON).get(String.class);
			LOG.info("Facebook Response => " + responseString);
			return Globalizer.jsonMapper().readValue(responseString, resClass);
		} catch (IOException e) {
			LOG.error(e);
			return null;
		}
	}

	public <T extends Serializable> T postRequest(Entity data, Class<T> resClass) {
		try {
			WebTarget target = this.buildRequest();
			LOG.info("POST Request => " + target.getUri().toString());
			String responseString = target.request(MediaType.APPLICATION_JSON).post(data, String.class);
			return Globalizer.jsonMapper().readValue(responseString, resClass);
		} catch (IOException e) {
			LOG.error(e);
			return null;
		}
	}
}
