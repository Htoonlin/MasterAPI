/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util.mail;

import com.sdm.core.Globalizer;
import com.sdm.core.Setting;
import com.sdm.core.util.mail.response.ValidateResponse;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
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
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

/**
 *
 * @author Htoonlin
 */
public class MailgunService implements IBaseMailService {

    private static final Logger LOG = Logger.getLogger(MailgunService.class.getName());

    private final String MAILGUN_URL = "https://api.mailgun.net/v3/";
    //private final String SEND_PATH = Setting.getInstance().MAILGUN_DOMAIN + "/messages";
    private final String VALIDATE_PATH = "address/validate";

    public final String EMAIL_PATTERN = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$";

    private final String MAILGUN_PRI_API_KEY;
    private final String MAILGUN_PUB_API_KEY;
    private final String MAILGUN_DOMAIN;
    private final String MAILGUN_DEF_MAIL_SENDER;

    private static MailgunService instance;

    public MailgunService() throws IOException {
        Properties settingProps = new Properties();
        settingProps.load(getClass().getClassLoader().getResourceAsStream("setting.properties"));
        MAILGUN_PRI_API_KEY = settingProps.getProperty("MAILGUN_PRI_API_KEY", "");
        MAILGUN_PUB_API_KEY = settingProps.getProperty("MAILGUN_PUB_API_KEY", "");
        MAILGUN_DOMAIN = settingProps.getProperty("MAILGUN_DOMAIN", "");
        MAILGUN_DEF_MAIL_SENDER = settingProps.getProperty("MAILGUN_DEF_MAIL_SENDER", "");
    }

    public static synchronized MailgunService getInstance() {
        if (instance == null) {
            try {
                instance = new MailgunService();
            } catch (IOException e) {
                LOG.error(e);
            }
        }
        return instance;
    }

    public boolean checkMail(String email) throws IOException {
        if (Setting.getInstance().MAIL_SERVICE.equalsIgnoreCase("mailgun")) {
            ValidateResponse response = validateMail(email);
            return response.isValid();
        } else {
            return email.matches(EMAIL_PATTERN);
        }
    }

    private MultivaluedMap createFormData(MailInfo mailInfo) throws Exception {
        try {
            MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
            if (mailInfo.getFrom() == null || mailInfo.getFrom().isEmpty()) {
                mailInfo.setFrom(MAILGUN_DEF_MAIL_SENDER);
            }
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
            target.register(HttpAuthenticationFeature.basic("api", MAILGUN_PUB_API_KEY));
            String result = target.queryParam("address", email).request().get(String.class);
            return Globalizer.jsonMapper().readValue(result, ValidateResponse.class);
        } catch (IOException e) {
            LOG.error(e);
            throw e;
        }
    }

    @Override
    public Response sendAttachment(MailInfo mailInfo, File attachment, MediaType attachmentType) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(MAILGUN_URL).path(MAILGUN_DOMAIN + "/messages");
        target.register(MultiPartFeature.class);
        target.register(HttpAuthenticationFeature.basic("api", MAILGUN_PRI_API_KEY));

        FormDataMultiPart formData = new FormDataMultiPart();
        if (mailInfo.getFrom() == null || mailInfo.getFrom().isEmpty()) {
            mailInfo.setFrom(MAILGUN_DEF_MAIL_SENDER);
        }
        formData.field("from", mailInfo.getFrom());

        formData.field("to", mailInfo.getTo());

        if (mailInfo.getCc() != null) {
            formData.field("cc", mailInfo.getCc());
        }

        if (mailInfo.getBcc() != null) {
            formData.field("bcc", mailInfo.getBcc());
        }

        formData.field("subject", mailInfo.getSubject());
        formData.field("html", mailInfo.getBody());

        formData.bodyPart(new FileDataBodyPart("attachment", attachment, attachmentType));
        Response response = target.request(MediaType.MULTIPART_FORM_DATA_TYPE)
                .post(Entity.entity(formData, MediaType.MULTIPART_FORM_DATA));
        if (response.getStatus() == 200) {
            return response;
        }

        return null;
    }

    @Override
    public Response sendHTML(MailInfo mailInfo) throws Exception {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(MAILGUN_URL).path(MAILGUN_DOMAIN + "/messages");
            target.register(HttpAuthenticationFeature.basic("api", MAILGUN_PRI_API_KEY));

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

    @Override
    public Response sendRaw(MailInfo mailInfo) throws Exception {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(MAILGUN_URL).path(MAILGUN_DOMAIN + "/messages");
            target.register(HttpAuthenticationFeature.basic("api", MAILGUN_PRI_API_KEY));

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
