/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.sdm.Constants;
import com.sdm.core.Globalizer;
import com.sdm.core.Setting;
import com.sdm.core.exception.InvalidRequestException;
import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.core.response.model.ErrorModel;
import com.sdm.master.entity.FileEntity;

/**
 *
 * @author Htoonlin
 */
public class FileDAO extends RestDAO {

    private static final Logger LOG = Logger.getLogger(FileDAO.class.getName());

    public FileDAO(int userId) {
        super(FileEntity.class.getName(), userId);
    }

    public FileDAO(Session session, int userId) {
        super(session, FileEntity.class.getName(), userId);
    }

    @Override
    public <T extends Serializable> void delete(T entity, boolean autoCommit) {
        FileEntity fileEntity = (FileEntity) entity;
        File savedFile = new File(Setting.getInstance().get(Setting.UPLOAD_DIRECTORY) + fileEntity.getStoragePath());
        if (savedFile.exists() && savedFile.delete()) {
            LOG.info("Deleted File by ID => " + fileEntity.getId());
        }
        super.delete(fileEntity, autoCommit);
    }

    private File generateFile(int userId, String ext) {
        String uploadPath = "/" + Constants.USER_PREFIX + userId + Globalizer.getDateString("/yyyy/MMMM/", new Date());
        String fileName = Globalizer.generateToken(8) + "." + ext;

        File baseDir = new File(Setting.getInstance().get(Setting.UPLOAD_DIRECTORY) + uploadPath);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        return new File(baseDir, fileName);
    }

    public FileEntity createFile(InputStream fileStream, FileEntity entity, boolean autoCommit) {
        File saveFile = this.generateFile(entity.getOwnerId(), entity.getExtension());

        // Get File Type
        final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        String type = fileTypeMap.getContentType(saveFile);
        if (type == null || type.length() <= 0) {
            type = "application/" + entity.getExtension();
        }

        try (OutputStream out = new FileOutputStream(saveFile)) {
            int read;
            byte[] bytes = new byte[1024];
            while ((read = fileStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            fileStream.close();
        } catch (IOException e) {
            LOG.error(e);
        }

        // Set File Info
        entity.setType(type);
        entity.setFileSize(saveFile.length());
        entity.setStoragePath(
                saveFile.getPath().substring(Setting.getInstance().get(Setting.UPLOAD_DIRECTORY).length()));
        return super.insert(entity, autoCommit);
    }

    @Override
    public <T extends Serializable> T insert(T requestEntity, boolean autoCommit) {
        FileEntity entity = (FileEntity) requestEntity;
        InvalidRequestException errorResponse = new InvalidRequestException();
        if (entity.getExternalURL() == null || entity.getExternalURL().isEmpty()) {
            errorResponse.addError("external_url",
                    new ErrorModel("External URL can't be blank. Otherwise, try to use file upload path.", ""));
            throw errorResponse;
        }

        entity.setStatus(FileEntity.EXTERNAL);
        Client client = ClientBuilder.newClient();
        Response response = client.target(entity.getExternalURL()).request().get();
        return (T) this.createFile(response.readEntity(InputStream.class), entity, autoCommit);
    }
}
