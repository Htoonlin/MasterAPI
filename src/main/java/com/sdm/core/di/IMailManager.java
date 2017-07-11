/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.di;

import java.io.File;
import java.util.List;

import javax.ws.rs.core.Response;

import com.sdm.core.util.mail.MailInfo;

/**
 *
 * @author Htoonlin
 */
public interface IMailManager {

	public final String EMAIL_PATTERN = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$";

	public boolean checkMail(String email);

	Response sendAttachment(MailInfo mailInfo, File attachment);

	Response sendAttachments(MailInfo mailInfo, List<File> attachments);

	Response sendHTML(MailInfo mailInfo);

	Response sendRaw(MailInfo mailInfo);

}
