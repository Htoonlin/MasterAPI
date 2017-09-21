/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util.mail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.jvnet.hk2.annotations.Service;

import com.sdm.core.Globalizer;
import com.sdm.core.Setting;
import com.sdm.core.di.IMailManager;
import com.sdm.core.util.mail.response.ValidateResponse;

/**
 *
 * @author Htoonlin
 */
@Service
public class MailgunService implements IMailManager {

    private static final Logger LOG = Logger.getLogger(MailgunService.class.getName());

    private final String MAILGUN_URL = "https://api.mailgun.net/v3/";
    // private final String SEND_PATH = Setting.getInstance().MAILGUN_DOMAIN +
    // "/messages";
    private final String VALIDATE_PATH = "address/validate";

    private final String DEFAULT_SENDER;
    private final String DOMAIN;
    private final String PRIVATE_KEY;
    private final String PUBLIC_KEY;

    public MailgunService() {
        this.DEFAULT_SENDER = Setting.getInstance().get(Setting.MAILGUN_DEFAULT_MAIL, "");
        this.DOMAIN = Setting.getInstance().get(Setting.MAILGUN_DOMAIN, "");
        this.PUBLIC_KEY = Setting.getInstance().get(Setting.MAILGUN_PUB_KEY, "");
        this.PRIVATE_KEY = Setting.getInstance().get(Setting.MAILGUN_PRI_KEY, "");
    }

    @Override
    public boolean checkMail(String email) {
        ValidateResponse response = validateMail(email);
        return response.isValid();
    }

    private MultivaluedMap<String, String> createFormData(MailInfo mailInfo) {
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
        try {
            if (mailInfo.getFrom() == null || mailInfo.getFrom().isEmpty()) {
                mailInfo.setFrom(this.DEFAULT_SENDER);
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
        } catch (Exception e) {
            LOG.error(e);
        }
        return formData;
    }

    public ValidateResponse validateMail(String email) {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(MAILGUN_URL).path(VALIDATE_PATH);
            target.register(HttpAuthenticationFeature.basic("api", PUBLIC_KEY));
            String result = target.queryParam("address", email).request().get(String.class);
            return Globalizer.jsonMapper().readValue(result, ValidateResponse.class);
        } catch (IOException e) {
            LOG.error(e);
        }
        return null;
    }

    @Override
    public Response sendAttachments(MailInfo mailInfo, List<File> attachments) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(MAILGUN_URL).path(DOMAIN + "/messages");
        target.register(MultiPartFeature.class);
        target.register(HttpAuthenticationFeature.basic("api", PRIVATE_KEY));

        FormDataMultiPart formData = new FormDataMultiPart();
        if (mailInfo.getFrom() == null || mailInfo.getFrom().isEmpty()) {
            mailInfo.setFrom(DEFAULT_SENDER);
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

        for (File attachment : attachments) {
            formData.bodyPart(new FileDataBodyPart("attachment", attachment));
        }
        Response response = target.request(MediaType.MULTIPART_FORM_DATA_TYPE)
                .post(Entity.entity(formData, MediaType.MULTIPART_FORM_DATA));
        if (response.getStatus() == 200) {
            return response;
        }

        return null;
    }

    @Override
    public Response sendAttachment(MailInfo mailInfo, File attachment) {
        List<File> files = new ArrayList<>();
        files.add(attachment);
        return this.sendAttachments(mailInfo, files);
    }

    @Override
    public Response sendHTML(MailInfo mailInfo) {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(MAILGUN_URL).path(DOMAIN + "/messages");
            target.register(HttpAuthenticationFeature.basic("api", PRIVATE_KEY));

            MultivaluedMap<String, String> formData = createFormData(mailInfo);
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
    public Response sendRaw(MailInfo mailInfo) {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(MAILGUN_URL).path(DOMAIN + "/messages");
            target.register(HttpAuthenticationFeature.basic("api", PRIVATE_KEY));

            MultivaluedMap<String, String> formData = createFormData(mailInfo);
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
