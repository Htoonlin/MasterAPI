package com.sdm.facebook.util;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;

import com.sdm.Constants;
import com.sdm.facebook.model.GraphResponse;

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

    public GraphResponse getRequest() {
        WebTarget target = this.buildRequest();
        LOG.info("GET Request => " + target.getUri().toString());
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        GraphResponse graph = new GraphResponse(response.getStatus(), response.getHeaders(),
                response.readEntity(String.class));
        LOG.info("Response " + graph.getLog());
        return graph;
    }

    public GraphResponse postRequest(Entity data) {
        WebTarget target = this.buildRequest();
        LOG.info("POST Request => " + target.getUri().toString());
        Response response = target.request(MediaType.APPLICATION_JSON).post(data);
        GraphResponse graph = new GraphResponse(response.getStatus(), response.getHeaders(),
                response.readEntity(String.class));
        LOG.info("Response " + graph.getLog());
        return graph;
    }
}
