package com.sdm.sample.resource;

import javax.ws.rs.Path;
import com.sdm.core.resource.RestResource;
import com.sdm.sample.entity.CustomerEntity;

@Path("sample/customers")
public class CustomerResource extends RestResource<CustomerEntity, Integer> {

    public CustomerResource() {
        super();
    }
}
