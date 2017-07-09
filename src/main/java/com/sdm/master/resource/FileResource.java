/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.sdm.core.Setting;
import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.core.resource.RestResource;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.MessageResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.master.dao.FileDAO;
import com.sdm.master.dao.UserDAO;
import com.sdm.master.entity.FileEntity;
import com.sdm.master.entity.UserEntity;

/**
 *
 * @author Htoonlin
 */
@Path("file")
public class FileResource extends RestResource<FileEntity, Long> {

    private static final Logger LOG = Logger.getLogger(FileResource.class.getName());
    private FileDAO mainDAO;

    @Override
    protected RestDAO getDAO() {
        return this.mainDAO;
    }

    @PostConstruct
    protected void init() {
        if (this.mainDAO == null) {
            mainDAO = new FileDAO();
        }
    }

    private Response downloadFile(final FileEntity entity) {
        StreamingOutput fileStream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    String filePath = Setting.getInstance().STORAGE_PATH + entity.getStoragePath();
                    File savedFile = new File(filePath);
                    if (savedFile.exists()) {
                        byte[] data = Files.readAllBytes(savedFile.toPath());
                        output.write(data);
                        output.flush();
                    }
                } catch (Exception e) {
                    throw new WebApplicationException("File not found!");
                }
            }
        };

        String fileName = entity.getName() + "." + entity.getExtension();
        return Response.ok(fileStream, entity.getType())
                .header("content-disposition", "attachment; filename=\"" + fileName + "\"")
                .build();
    }

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse uploadFile(
            @FormDataParam("uploadedFile") InputStream inputFile,
            @FormDataParam("uploadedFile") FormDataContentDisposition fileDetail) throws Exception {
        try {
            UserDAO userDAO = new UserDAO(mainDAO.getSession());
            UserEntity currentUser = userDAO.fetchById(getUserId());
            if (currentUser == null) {
                return new MessageResponse(401, ResponseType.WARNING,
                        "Invalid user. You neeed to register new account to upload file.");
            }
            FileEntity entity = mainDAO.saveFile(getUserId(), inputFile, fileDetail);
            return new DefaultResponse<FileEntity>(entity);
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }

    @PermitAll
    @GET
    @Path("public/{token}.{ext}")
    public Response publicDownload(@PathParam("token") String token, @PathParam("ext") String ext) throws Exception {
        FileEntity entity = mainDAO.fetchByToken(token, ext);
        if (entity != null) {
            return this.downloadFile(entity);
        }
        MessageResponse message = new MessageResponse(204, ResponseType.WARNING, "There is no file for your requested token.");
        return Response.ok(message, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("{id:\\d+}/download")
    public Response privateDownload(@PathParam("id") double id) throws Exception {
        FileEntity entity = mainDAO.fetchById(id);
        if (entity == null) {
            MessageResponse message = new MessageResponse(204, ResponseType.WARNING, "There is no file for your request.");
            return Response.ok(message, MediaType.APPLICATION_JSON).build();
        }

        return this.downloadFile(entity);
    }

    @Override
    protected Logger getLogger() {
        return FileResource.LOG;
    }
}
