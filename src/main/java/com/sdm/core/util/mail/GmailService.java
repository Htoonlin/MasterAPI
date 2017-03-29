/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util.mail;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;

/**
 *
 * @author Htoonlin
 */
public class GmailService implements IBaseMailService {

    private static final Logger LOG = Logger.getLogger(GmailService.class.getName());

    private static GmailService instance;

    private final String GMAIL_HOST = "smtp.gmail.com";
    private final String GMAIL_PORT = "465";
    private final String GMAIL_SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";
    private final String GMAIL_NEED_AUTH = "true";
    private final String GMAIL_USER;

    private Session mailSession;

    public GmailService() throws IOException {
        Properties settingProps = new Properties();
        settingProps.load(getClass().getClassLoader().getResourceAsStream("setting.properties"));
        GMAIL_USER = settingProps.getProperty("GMAIL_USER", "");
        final String password = settingProps.getProperty("GMAIL_PASSWORD", "");
        settingProps.put("mail.smtp.host", GMAIL_HOST);
        settingProps.put("mail.smtp.socketFactory.port", GMAIL_PORT);
        settingProps.put("mail.smtp.socketFactory.class", GMAIL_SOCKET_FACTORY);
        settingProps.put("mail.smtp.auth", GMAIL_NEED_AUTH);
        settingProps.put("mail.smtp.port", GMAIL_PORT);

        mailSession = Session.getDefaultInstance(settingProps,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(GMAIL_USER, password);
            }
        });
    }

    public static synchronized GmailService getInstance() {
        if (instance == null) {
            try {
                instance = new GmailService();
            } catch (IOException e) {
                LOG.error(e);
            }
        }
        return instance;
    }

    private Message buildMessage(MailInfo mailInfo) throws MessagingException {
        try {
            Message message = new MimeMessage(mailSession);
            message.setHeader("X-Priority", "1");
            if (mailInfo.getFrom() == null || mailInfo.getFrom().isEmpty()) {
                mailInfo.setFrom(GMAIL_USER);
            }
            message.setFrom(new InternetAddress(mailInfo.getFrom()));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mailInfo.getTo()));

            if (mailInfo.getCc() != null) {
                message.addRecipients(Message.RecipientType.CC,
                        InternetAddress.parse(mailInfo.getCc()));
            }

            if (mailInfo.getBcc() != null) {
                message.addRecipients(Message.RecipientType.BCC,
                        InternetAddress.parse(mailInfo.getBcc()));
            }

            message.setSubject(mailInfo.getSubject());

            return message;
        } catch (MessagingException ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    @Override
    public Response sendAttachment(MailInfo mailInfo, File attachment, MediaType attachmentType) throws Exception {
        Response response = Response.ok().build();
        try {
            Message message = buildMessage(mailInfo);
            MimeBodyPart textBody = new MimeBodyPart();
            textBody.setContent(mailInfo.getBody(), "text/html; charset=utf-8");

            MimeBodyPart attachmentBody = new MimeBodyPart();
            DataSource dataSource = new FileDataSource(attachment);
            attachmentBody.setDataHandler(new DataHandler(dataSource));
            attachmentBody.setFileName(attachment.getName());

            Multipart bodyPart = new MimeMultipart();
            bodyPart.addBodyPart(textBody);
            bodyPart.addBodyPart(attachmentBody);

            message.setContent(bodyPart);

            Transport.send(message);
        } catch (MessagingException ex) {
            response = Response.serverError().build();
            throw ex;
        }

        return response;
    }

    @Override
    public Response sendHTML(MailInfo mailInfo) throws Exception {
        Response response = Response.ok().build();
        try {
            Message message = buildMessage(mailInfo);
            message.setContent(mailInfo.getBody(), "text/html; charset=utf-8");
            Transport.send(message);
        } catch (MessagingException ex) {
            response = Response.serverError().build();
            throw ex;
        }
        return response;
    }

    @Override
    public Response sendRaw(MailInfo mailInfo) throws Exception {
        Response response = Response.ok().build();
        try {
            Message message = buildMessage(mailInfo);
            message.setText(mailInfo.getBody());
            Transport.send(message);
        } catch (MessagingException ex) {
            response = Response.serverError().build();
            throw ex;
        }
        return response;
    }

}
