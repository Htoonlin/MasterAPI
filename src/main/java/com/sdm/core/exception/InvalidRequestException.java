package com.sdm.core.exception;

import java.util.HashMap;

import javax.ws.rs.WebApplicationException;

import com.sdm.core.response.model.ErrorModel;

public class InvalidRequestException extends WebApplicationException {

    /**
     *
     */
    private static final long serialVersionUID = -1809767050392811636L;

    private HashMap<String, ErrorModel> errors;

    public InvalidRequestException() {
        this.errors = new HashMap<>();
    }

    public InvalidRequestException(HashMap<String, ErrorModel> errors) {
        this.errors = errors;
    }

    public HashMap<String, ErrorModel> getErrors() {
        return errors;
    }

    public void addError(String property, String message, Object value) {
        this.errors.put(property, new ErrorModel(message, value));
    }

    public void addError(String property, ErrorModel value) {
        if (this.errors == null) {
            this.errors = new HashMap<>();
        }
        this.errors.put(property, value);
    }

    public void setErrors(HashMap<String, ErrorModel> errors) {
        this.errors = errors;
    }
}
