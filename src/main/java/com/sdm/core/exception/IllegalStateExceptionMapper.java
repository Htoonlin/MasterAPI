/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.exception;

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
public class IllegalStateExceptionMapper implements ExceptionMapper<IllegalStateException> {

    @Override
    public Response toResponse(IllegalStateException exception) {
        MessageResponse message =  new MessageResponse(500, ResponseType.ERROR, "ILLEGAL_STATE_ERROR", exception.getMessage());
        if (Setting.getInstance().ENVIRONMENT.equalsIgnoreCase("dev")) {
            Map<String, Object> debug = new HashMap<>();
            debug.put("StackTrace", exception.getStackTrace());
            debug.put("Suppressed", exception.getSuppressed());
            message.setDebug(debug);
        } 
        
        return Response.serverError().entity(new DefaultResponse(message)).type(MediaType.APPLICATION_JSON).build();
    }

}
