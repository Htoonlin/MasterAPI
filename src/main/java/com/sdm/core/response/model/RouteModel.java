/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.response.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Htoonlin
 */
@JsonPropertyOrder(value = {"path", "method", "response_type", "query_params,", "entity_params", "form_params", "resource_class", "resource_method"})
public class RouteModel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6757987371372221883L;
    private String resourceClass;
    private String resourceMethod;
    private String path;
    private String method;
    private String responseType;
    private Map<String, RouteParamModel> queryParams;
    private Map<String, RouteParamModel> otherParams;

    public String getResourceClass() {
        return resourceClass;
    }

    public void setResourceClass(String resourceClass) {
        this.resourceClass = resourceClass;
    }

    public String getResourceMethod() {
        return resourceMethod;
    }

    public void setResourceMethod(String resourceMethod) {
        this.resourceMethod = resourceMethod;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public Map<String, RouteParamModel> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, RouteParamModel> queryParams) {
        this.queryParams = queryParams;
    }

    public void addQueryParam(String name, RouteParamModel body) {
        if (this.queryParams == null) {
            this.queryParams = new HashMap<>();
        }
        this.queryParams.put(name, body);
    }

    public Map<String, RouteParamModel> getOtherParams() {
        return otherParams;
    }

    public void setOtherParams(Map<String, RouteParamModel> otherParams) {
        this.otherParams = otherParams;
    }

    public void addOtherParam(String name, RouteParamModel body) {
        if (this.otherParams == null) {
            this.otherParams = new HashMap();
        }
        this.otherParams.put(name, body);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((method == null) ? 0 : method.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        result = prime * result + ((resourceClass == null) ? 0 : resourceClass.hashCode());
        result = prime * result + ((resourceMethod == null) ? 0 : resourceMethod.hashCode());
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
        RouteModel other = (RouteModel) obj;
        if (method == null) {
            if (other.method != null) {
                return false;
            }
        } else if (!method.equals(other.method)) {
            return false;
        }
        if (path == null) {
            if (other.path != null) {
                return false;
            }
        } else if (!path.equals(other.path)) {
            return false;
        }
        if (resourceClass == null) {
            if (other.resourceClass != null) {
                return false;
            }
        } else if (!resourceClass.equals(other.resourceClass)) {
            return false;
        }
        if (resourceMethod == null) {
            if (other.resourceMethod != null) {
                return false;
            }
        } else if (!resourceMethod.equals(other.resourceMethod)) {
            return false;
        }
        return true;
    }

}
