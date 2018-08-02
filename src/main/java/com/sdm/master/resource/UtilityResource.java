package com.sdm.master.resource;

import com.sdm.core.Globalizer;
import com.sdm.core.resource.DefaultResource;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.model.MessageModel;
import com.sdm.core.util.MyanmarFontManager;
import com.sdm.core.util.SecurityManager;
import java.util.HashMap;
import javax.annotation.security.PermitAll;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.commons.httpclient.HttpStatus;

@Path("util")
public class UtilityResource extends DefaultResource {

    @PermitAll
    @GET
    @Path("jwt")
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse generateJWTKey() {
        return new DefaultResponse<>(SecurityManager.generateJWTKey());
    }
    
    @PermitAll
    @GET
    @Path("strRandom/{len}")
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse generateRandomString(@DefaultValue("10") @PathParam("len") int length) {
        return new DefaultResponse<>(Globalizer.generateToken(length));
    }

    @PermitAll
    @GET
    @Path("lang")
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse langConverter(@QueryParam("input") String input) {
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
            MessageModel message = new MessageModel(HttpStatus.SC_BAD_REQUEST, "Invalid font!",
                    "No! It is not myanmar font.");
            return new DefaultResponse<>(message);
        }
    }

}
