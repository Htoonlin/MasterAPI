/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util.mail;

import java.io.File;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Htoonlin
 */
public interface IBaseMailService {

    Response sendAttachment(MailInfo mailInfo, File attachment, MediaType attachmentType) throws Exception;

    Response sendHTML(MailInfo mailInfo) throws Exception;

    Response sendRaw(MailInfo mailInfo) throws Exception;

}
