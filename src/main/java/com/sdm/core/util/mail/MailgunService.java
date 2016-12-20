/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util.mail;

import com.sdm.core.Globalizer;
import com.sdm.core.Setting;
import com.sdm.core.util.mail.response.ValidateResponse;
import java.io.IOException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.apache.log4j.Logger;

/**
 *
 * @author Htoonlin
 */
public class MailgunService {

    private static final Logger LOG = Logger.getLogger(MailgunService.class.getName());

    private final String MAILGUN_URL = "https://api.mailgun.net/v3/"; 

    private final String SEND_PATH = Setting.getInstance().MAILGUN_DOMAIN + "/messages";
    private final String VALIDATE_PATH = "address/validate";

    private static MailgunService instance;

    public MailgunService() {
        super();
    }

    public static synchronized MailgunService getInstance() {
        if (instance == null) {
            instance = new MailgunService();
        }
        return instance;
    }

    public boolean checkMail(String email) throws IOException {
        ValidateResponse response = validateMail(email);
        return response.isValid();
    }

    private MultivaluedMap createFormData(MailInfo mailInfo) throws Exception {
        try {
            MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
            formData.add("from", mailInfo.getFrom());

            formData.add("to", mailInfo.getTo());

            if (mailInfo.getCc() != null) {
                formData.add("cc", mailInfo.getCc());
            }

            if (mailInfo.getBcc() != null) {
                formData.add("bcc", mailInfo.getBcc());
            }

            formData.add("subject", mailInfo.getSubject());

            return formData;
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }

    public ValidateResponse validateMail(String email) throws IOException {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(MAILGUN_URL).path(VALIDATE_PATH);
            target.register(HttpAuthenticationFeature.basic("api", Setting.getInstance().MAILGUN_PUB_API_KEY));
            String result = target.queryParam("address", email).request().get(String.class);
            return Globalizer.jsonMapper().readValue(result, ValidateResponse.class);
        } catch (IOException e) {
            LOG.error(e);
            throw e;
        }
    }

    public Response sendHTML(MailInfo mailInfo) throws Exception {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(MAILGUN_URL).path(SEND_PATH);
            target.register(HttpAuthenticationFeature.basic("api", Setting.getInstance().MAILGUN_PRI_API_KEY));

            MultivaluedMap formData = createFormData(mailInfo);
            formData.add("html", mailInfo.getBody());

            Response response = target.request(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(Entity.form(formData));
            if (response.getStatus() == 200) {
                return response;
            }
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
        return null;
    }

    public Response sendRaw(MailInfo mailInfo) throws Exception {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(MAILGUN_URL).path(SEND_PATH);
            target.register(HttpAuthenticationFeature.basic("api", Setting.getInstance().MAILGUN_PRI_API_KEY));

            MultivaluedMap formData = createFormData(mailInfo);
            formData.add("text", mailInfo.getBody());

            Response response = target.request(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(Entity.form(formData));
            if (response.getStatus() == 200) {
                return response;
            }
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
        return null;
    }
}
