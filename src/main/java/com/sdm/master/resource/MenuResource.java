package com.sdm.master.resource;

import com.sdm.core.resource.RestResource;
import com.sdm.master.entity.MenuEntity;
import javax.ws.rs.Path;

@Path("menus")
public class MenuResource extends RestResource<MenuEntity, Long> {

    public MenuResource() {
        super();
    }
    
}
