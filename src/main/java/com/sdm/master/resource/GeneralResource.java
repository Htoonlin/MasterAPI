/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import com.sdm.core.resource.DefaultResource;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.MessageResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.core.util.GeoIPManager;
import com.sdm.core.util.MyanmarFontManager;
import com.sdm.master.dao.GeoIPCacheDAO;
import com.sdm.master.dao.UserDAO;
import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author Htoonlin
 */
@Path("/")
public class GeneralResource extends DefaultResource {

    private static final Logger LOG = Logger.getLogger(ProfileResource.class.getName());
    
     @PostConstruct
    public void onLoad() {
        LOG.info("Welcome...");
    }

    @PermitAll
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse welcome() throws Exception {
        MessageResponse response = new MessageResponse(200, ResponseType.SUCCESS,
                "WELCOME", "Welcome from sundew API. Never give up to be a warrior!");
        return new DefaultResponse(response);
    }

    @PermitAll
    @GET
    @Path("ip")
    @Produces(MediaType.APPLICATION_JSON)
    public DefaultResponse getIpResponse(@Context HttpServletRequest request) throws Exception {   
        GeoIPManager ipManager = new GeoIPManager(new GeoIPCacheDAO());
        return new DefaultResponse(ipManager.lookupInfo(request.getRemoteAddr()));
    }

    @PermitAll
    @GET
    @Path("lang")
    @Produces(MediaType.APPLICATION_JSON)
    public DefaultResponse getIpResponse(@QueryParam("input") String input) throws Exception {
        MessageResponse message = new MessageResponse(200, ResponseType.INFO, "IS_MYANMAR", "No! It is not myanmar font.");
        if (MyanmarFontManager.isMyanmar(input)) {
            String msgString = "Yes! It is myanmar";
            if(MyanmarFontManager.isUnicode(input)){
                msgString += " unicode font.";
            }else if(MyanmarFontManager.isZawgyi(input)){
                msgString += " zawgyi font.";
            }
            message = new MessageResponse(200, ResponseType.INFO, "IS_MYANMAR", msgString);
        }
        return new DefaultResponse(message);
    }
}
