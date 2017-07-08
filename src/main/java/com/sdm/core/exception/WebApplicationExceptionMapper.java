/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.exception;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.sdm.core.Setting;
import com.sdm.core.response.MessageResponse;
import com.sdm.core.response.ResponseType;

/**
 *
 * @author Htoonlin
 */
@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException exception) {
        Response exResponse = exception.getResponse();
        MessageResponse message = new MessageResponse(exResponse.getStatus(), ResponseType.ERROR,
                exception.getLocalizedMessage());
        if (Setting.getInstance().ENVIRONMENT.equalsIgnoreCase("dev")) {
            Map<String, Object> debug = new HashMap<>();
            debug.put("StackTrace", exception.getStackTrace());
            debug.put("Suppressed", exception.getSuppressed());
            message.setDebug(debug);
        }

        return Response.status(exResponse.getStatus()).entity(message).type(MediaType.APPLICATION_JSON).build();
    }

}
