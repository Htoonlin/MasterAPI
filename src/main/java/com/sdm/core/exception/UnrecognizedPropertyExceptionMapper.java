/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.exception;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.sdm.core.Setting;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.core.response.MessageResponse;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Htoonlin
 */
@Provider
public class UnrecognizedPropertyExceptionMapper implements ExceptionMapper<UnrecognizedPropertyException> {

    @Override
    public Response toResponse(UnrecognizedPropertyException exception) {
        MessageResponse message =  new MessageResponse(400, ResponseType.ERROR, "UNRECOGNIZED_PROPERTY", exception.getLocalizedMessage());
        if (Setting.getInstance().ENVIRONMENT.equalsIgnoreCase("dev")) {
            Map<String, Object> debug = new HashMap<>();
            debug.put("StackTrace", exception.getStackTrace());
            debug.put("Suppressed", exception.getSuppressed());
            message.setDebug(debug);
        } 
        
        return Response.status(400).entity(new DefaultResponse(message)).type(MediaType.APPLICATION_JSON).build();
    }

}
