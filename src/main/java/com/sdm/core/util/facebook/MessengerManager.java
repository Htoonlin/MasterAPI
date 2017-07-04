/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util.facebook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sdm.core.Globalizer;
import com.sdm.core.util.facebook.model.messenger.Message;
import com.sdm.core.util.facebook.model.PageResponse;
import com.sdm.core.util.facebook.model.messenger.Recipient;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.apache.log4j.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Htoonlin
 */
public class MessengerManager {
    private static final Logger LOG = Logger.getLogger(MessengerManager.class.getName());

    private final String FACEBOOK_API = "https://graph.facebook.com/v2.6/";
    private final String MESSAGE_API = FACEBOOK_API + "me/messages?access_token=";
    private final String PAGE_ACCESS_TOKEN;

    public MessengerManager(String token) {
        this.PAGE_ACCESS_TOKEN = token;
    }

    private Response getRequest(String url) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);
        return target.request().accept(MediaType.APPLICATION_JSON).get(Response.class);
    }

    private Response postRequest(String url, String data) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);
        return target.request().accept(MediaType.APPLICATION_JSON)
                .post(Entity.json(data), Response.class);
    }

    private void processResponse(Response response, IFacebookListener listener) {
        if (listener == null) {
            LOG.debug("Response Message : " + response.readEntity(String.class));
            return;
        }

        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            String successString = response.readEntity(String.class);
            LOG.info("Send Message : " + successString);
            listener.success(response.getStatus(), successString);
        } else {
            String errorString = response.readEntity(String.class);
            LOG.warn("Send Message : " + errorString);
            listener.failed(response.getStatus(), errorString);
        }
    }

    public PageResponse getPageInfo() {
        String url = FACEBOOK_API + "me?access_token=" + PAGE_ACCESS_TOKEN;
        Response response = this.getRequest(url);
        if (response.getStatus() == 200) {
            String responseString = response.readEntity(String.class);
            try {
                return Globalizer.jsonMapper().readValue(responseString, PageResponse.class);
            } catch (IOException exception) {
                LOG.error("Invalid Response : " + responseString);
            }
        }
        return null;
    }

    public void sendMessage(Recipient recipient, Message message, IFacebookListener listener) {
        try {
            Map<String, Object> sendMessage = new HashMap<>();
            sendMessage.put("recipient", recipient);
            sendMessage.put("message", message);
            String requestString = Globalizer.jsonMapper().writeValueAsString(sendMessage);
            Response response = postRequest(MESSAGE_API + PAGE_ACCESS_TOKEN, requestString);
            processResponse(response, listener);
        } catch (JsonProcessingException ex) {
            LOG.error(ex);
        }
    }

    public void sendJsonTemplate(String recipientId, String jsonFileURL, IFacebookListener listener) {
        try {
            JsonReader payload = Json.createReader(new FileReader(jsonFileURL));
            JsonObject data = Json.createObjectBuilder()
                    .add("recipient", Json.createObjectBuilder().add("id", recipientId))
                    .add("message", Json.createObjectBuilder().add("attachment",
                            Json.createObjectBuilder().add("type", "template")
                                    .add("payload", payload.readObject()))
                    ).build();
            Response response = this.postRequest(MESSAGE_API + PAGE_ACCESS_TOKEN, data.toString());
            processResponse(response, listener);
        } catch (FileNotFoundException ex) {
            this.sendMessage(new Recipient(recipientId),
                    new Message("Invalid Template File."), listener);
            LOG.error(ex);
        }
    }
}
