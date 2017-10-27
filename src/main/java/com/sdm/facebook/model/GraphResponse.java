package com.sdm.facebook.model;

import java.io.Serializable;
import javax.ws.rs.core.MultivaluedMap;

public class GraphResponse implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5164534900242503689L;
    private int status;
    private MultivaluedMap<String, Object> headers;
    private String body;

    public GraphResponse() {

    }

    public GraphResponse(int status, MultivaluedMap<String, Object> headers, String body) {
        super();
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MultivaluedMap<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(MultivaluedMap<String, Object> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLog() {
        return "[" + this.status + "] => " + this.body;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((body == null) ? 0 : body.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        GraphResponse other = (GraphResponse) obj;
        if (body == null) {
            if (other.body != null) {
                return false;
            }
        } else if (!body.equals(other.body)) {
            return false;
        }
        return true;
    }
}
