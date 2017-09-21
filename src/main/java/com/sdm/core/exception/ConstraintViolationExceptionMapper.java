package com.sdm.core.exception;

import java.util.HashMap;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.commons.httpclient.HttpStatus;
import org.hibernate.validator.internal.engine.path.PathImpl;

import com.sdm.core.Globalizer;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.core.response.model.ErrorModel;

@Provider
public class ConstraintViolationExceptionMapper extends DefaultExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        HashMap<String, ErrorModel> content = new HashMap<>();
        for (ConstraintViolation<?> error : exception.getConstraintViolations()) {
            PathImpl property = (PathImpl) error.getPropertyPath();
            String propertyName = Globalizer.camelToLowerUnderScore(property.getLeafNode().getName());
            content.put(propertyName, new ErrorModel(error));
        }

        DefaultResponse response = new DefaultResponse<>(HttpStatus.SC_BAD_REQUEST, ResponseType.CLIENT_ERROR, content);
        return Response.status(HttpStatus.SC_BAD_REQUEST).entity(response).type(MediaType.APPLICATION_JSON).build();
    }

}
