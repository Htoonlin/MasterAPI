/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.sdm.core.Setting;
import com.sdm.core.di.IMailManager;

/**
 *
 * @author Htoonlin
 */
public class WebMailService implements IMailManager {

	private static final Logger LOG = Logger.getLogger(WebMailService.class.getName());

	private final String MAIL_SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private Session mailSession;

	public WebMailService() {
		Properties settingProps = new Properties();
		settingProps.put("mail.smtp.host", Setting.getInstance().get(Setting.MAIL_HOST, "smtp.gmail.com"));
		settingProps.put("mail.smtp.socketFactory.port", Setting.getInstance().get(Setting.MAIL_PORT, "465"));
		settingProps.put("mail.smtp.socketFactory.class", MAIL_SOCKET_FACTORY);
		settingProps.put("mail.smtp.auth", Setting.getInstance().get(Setting.MAIL_IS_AUTH, "true"));
		settingProps.put("mail.smtp.port", Setting.getInstance().get(Setting.MAIL_PORT, "465"));

		mailSession = Session.getDefaultInstance(settingProps, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Setting.getInstance().get(Setting.MAIL_USER, ""),
						Setting.getInstance().get(Setting.MAIL_PASSWORD, ""));
			}
		});
	}

	private Message buildMessage(MailInfo mailInfo) {
		try {
			Message message = new MimeMessage(mailSession);
			message.setHeader("X-Priority", "1");
			if (mailInfo.getFrom() == null || mailInfo.getFrom().isEmpty()) {
				mailInfo.setFrom(Setting.getInstance().get(Setting.MAIL_USER, ""));
			}
			message.setFrom(new InternetAddress(mailInfo.getFrom()));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailInfo.getTo()));

			if (mailInfo.getCc() != null) {
				message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(mailInfo.getCc()));
			}

			if (mailInfo.getBcc() != null) {
				message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(mailInfo.getBcc()));
			}

			message.setSubject(mailInfo.getSubject());

			return message;
		} catch (MessagingException ex) {
			LOG.error(ex);
		}
		return null;
	}

	@Override
	public Response sendAttachments(MailInfo mailInfo, List<File> attachments) {
		Response response = Response.ok().build();
		try {
			Message message = buildMessage(mailInfo);
			MimeBodyPart textBody = new MimeBodyPart();
			textBody.setContent(mailInfo.getBody(), "text/html; charset=utf-8");

			Multipart bodyPart = new MimeMultipart();
			bodyPart.addBodyPart(textBody);

			for (File attachment : attachments) {
				MimeBodyPart attachmentBody = new MimeBodyPart();
				DataSource dataSource = new FileDataSource(attachment);
				attachmentBody.setDataHandler(new DataHandler(dataSource));
				attachmentBody.setFileName(attachment.getName());
				bodyPart.addBodyPart(attachmentBody);
			}

			message.setContent(bodyPart);

			Transport.send(message);
		} catch (MessagingException ex) {
			response = Response.serverError().build();
			LOG.error(ex);
		}

		return response;
	}

	@Override
	public Response sendAttachment(MailInfo mailInfo, File attachment) {
		List<File> files = new ArrayList<>();
		files.add(attachment);
		return this.sendAttachments(mailInfo, files);
	}

	@Override
	public Response sendHTML(MailInfo mailInfo) {
		Response response = Response.ok().build();
		try {
			Message message = buildMessage(mailInfo);
			message.setContent(mailInfo.getBody(), "text/html; charset=utf-8");
			Transport.send(message);
		} catch (MessagingException ex) {
			LOG.error(ex);
			response = Response.serverError().build();
		}
		return response;
	}

	@Override
	public Response sendRaw(MailInfo mailInfo) {
		Response response = Response.ok().build();
		try {
			Message message = buildMessage(mailInfo);
			message.setText(mailInfo.getBody());
			Transport.send(message);
		} catch (MessagingException ex) {
			LOG.error(ex);
			response = Response.serverError().build();
		}
		return response;
	}

	@Override
	public boolean checkMail(String email) {
		return email.matches(EMAIL_PATTERN);
	}

}
