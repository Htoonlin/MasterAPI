package com.sdm.master.resource;

import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.core.resource.RestResource;
import com.sdm.master.dao.MenuDAO;
import com.sdm.master.entity.MenuEntity;
import javax.annotation.PostConstruct;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;

@Path("menus")
public class MenuResource extends RestResource<MenuEntity, Long> {

    private MenuDAO mainDAO;

    @PostConstruct
    private void init() {
        mainDAO = new MenuDAO(getUserId());
    }

    @Override
    protected Logger getLogger() {
        return Logger.getLogger(MenuResource.class);
    }

    @Override
    protected RestDAO getDAO() {
        return this.mainDAO;
    }

}
