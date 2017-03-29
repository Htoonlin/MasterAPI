/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import com.sdm.core.Setting;
import com.sdm.core.resource.RestResource;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.MessageResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.master.dao.FileDAO;
import com.sdm.master.dao.UserDAO;
import com.sdm.master.entity.FileEntity;
import com.sdm.master.entity.UserEntity;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import org.apache.log4j.Logger;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author Htoonlin
 */
@Path("file")
public class FileResource extends RestResource<FileEntity, Double> {

    private static final Logger logger = Logger.getLogger(FileResource.class.getName());
    private FileDAO mainDAO;
    
    @Override
    protected void onLoad() {        
        mainDAO = new FileDAO(getHttpSession());
        super.mainDAO = mainDAO;
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
            UserDAO userDAO = new UserDAO(mainDAO.getSession(), getHttpSession());
            UserEntity currentUser = userDAO.fetchById(getUserId());
            if (currentUser == null) {
                return new DefaultResponse(new MessageResponse(401, ResponseType.WARNING, "NO_USER", 
                        "Invalid user. You neeed to register new account to upload file."));
            }
            FileEntity entity = mainDAO.saveFile(inputFile, fileDetail);
            return new DefaultResponse(entity);
        } catch (Exception e) {
            logger.error(e);
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
        return Response.ok(new DefaultResponse(
                new MessageResponse(204, ResponseType.WARNING, 
                        "NO_FILE", "There is no file for your requested token.")),
                MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("{id:\\d+}/download")
    public Response privateDownload(@PathParam("id") double id) throws Exception {
        FileEntity entity = mainDAO.fetchById(id);
        if (entity == null) {
            return Response.ok(new DefaultResponse(
                    new MessageResponse(204, ResponseType.WARNING,
                            "NO_FILE", "There is no file for your request.")),
                    MediaType.APPLICATION_JSON).build();
        }

        return this.downloadFile(entity);
    }
}
