package com.sdm.master.resource;

import com.sdm.core.Globalizer;
import com.sdm.core.exception.InvalidRequestException;
import com.sdm.core.resource.DefaultResource;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.util.MyanmarFontManager;
import com.sdm.core.util.SecurityManager;
import com.sdm.master.util.GeoIPManager;
import java.util.HashMap;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("util")
public class UtilityResource extends DefaultResource {

    @PermitAll
    @GET
    @Path("ip/{address}")
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse checkIP(@Context HttpServletRequest request,
            @DefaultValue("") @PathParam("address") String address) throws Exception {
        GeoIPManager ipManager = new GeoIPManager();
        if (address.isEmpty()) {
            address = request.getRemoteAddr();
        }
        return new DefaultResponse<HashMap<String, Object>>(ipManager.lookupInfo(address));
    }

    @PermitAll
    @GET
    @Path("jwt")
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse generateJWTKey() throws Exception {
        return new DefaultResponse<String>(SecurityManager.generateJWTKey());
    }

    @PermitAll
    @GET
    @Path("strRandom/{len}")
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse generateRandomString(@DefaultValue("10") @PathParam("len") int length) throws Exception {
        return new DefaultResponse<String>(Globalizer.generateToken(length));
    }

    @PermitAll
    @GET
    @Path("lang")
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse langConverter(@QueryParam("input") String input) throws Exception {
        HashMap<String, String> content = new HashMap<>();
        if (MyanmarFontManager.isMyanmar(input)) {
            String msgString = "Yes! It is myanmar";
            if (MyanmarFontManager.isUnicode(input)) {
                msgString += " unicode font.";
                content.put("unicode", input);
                content.put("zawgyi", MyanmarFontManager.toZawgyi(input));
            } else if (MyanmarFontManager.isZawgyi(input)) {
                msgString += " zawgyi font.";
                content.put("zawgyi", input);
                content.put("unicode", MyanmarFontManager.toUnicode(input));
            }
            content.put("message", msgString);
            return new DefaultResponse(content);
        } else {
            InvalidRequestException exception = new InvalidRequestException();
            exception.addError("input", "It is not myanmar font.", input);
            throw exception;
        }
    }

}
