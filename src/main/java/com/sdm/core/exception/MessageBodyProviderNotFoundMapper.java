package com.sdm.core.exception;

import javax.ws.rs.core.Response;
import org.glassfish.jersey.message.internal.MessageBodyProviderNotFoundException;

public class MessageBodyProviderNotFoundMapper extends DefaultExceptionMapper<MessageBodyProviderNotFoundException> {

    @Override
    public Response toResponse(MessageBodyProviderNotFoundException exception) {
        return buildResponse(422, exception);
    }

}
