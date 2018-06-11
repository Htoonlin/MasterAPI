/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import com.sdm.core.Setting;
import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.core.resource.RestResource;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.core.response.model.MessageModel;
import com.sdm.core.util.FileManager;
import com.sdm.master.dao.FileDAO;
import com.sdm.master.dao.UserDAO;
import com.sdm.master.entity.FileEntity;
import com.sdm.master.entity.UserEntity;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author Htoonlin
 */
@Path("files")
public class FileResource extends RestResource<FileEntity, BigInteger> {

    private static final Logger LOG = Logger.getLogger(FileResource.class.getName());
    private final int CACHE_AGE = 3600 * 24 * 7;
    private FileDAO mainDAO;

    @Override
    protected RestDAO getDAO() {
        return this.mainDAO;
    }

    @PostConstruct
    protected void init() {
        if (this.mainDAO == null) {
            mainDAO = new FileDAO(getUserId());
        }
    }

    private Response downloadFile(final FileEntity entity, final Dimension dimension, boolean is64, boolean isDownload) {

        DefaultResponse cacheResponse = this.validateCache(CACHE_AGE);
        if (cacheResponse != null) {
            return Response.ok(cacheResponse, MediaType.APPLICATION_JSON).build();
        }

        String filePath = Setting.getInstance().get(Setting.UPLOAD_DIRECTORY) + entity.getStoragePath();
        File savedFile = new File(filePath);

        if (!savedFile.exists()) {
            return Response.noContent().build();
        }

        if (is64) {
            try {
                byte[] data = Files.readAllBytes(savedFile.toPath());
                String base64String = Base64.getMimeEncoder().encodeToString(data);
                return Response.ok(base64String, MediaType.TEXT_PLAIN).build();
            } catch (IOException e) {
                return Response.noContent().build();
            }
        }

        StreamingOutput fileStream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    // If it is image and include dimension, it will process image on dimension
                    if (entity.getType().contains("image") && dimension != null) {
                        Thumbnails.of(savedFile).size(dimension.width, dimension.height).keepAspectRatio(true)
                                .useOriginalFormat().toOutputStream(output);
                    } else {
                        byte[] data = Files.readAllBytes(savedFile.toPath());
                        output.write(data);
                        output.flush();
                    }
                } catch (Exception e) {
                    throw new WebApplicationException("File not found!");
                }
            }
        };

        // Create cache with 1 week
        Map<String, Object> cacheHeaders = this.buildCache(CACHE_AGE);
        ResponseBuilder builder = Response.ok(fileStream, entity.getType());
        
        //if download directly
        if(isDownload){    
            String fileName = entity.getName() + "." + entity.getExtension();
            builder.header("content-disposition", "attachment; filename=\"" + fileName + "\"");
        }
        
        //Set Cache Headers
        for (String key : cacheHeaders.keySet()) {
            builder.header(key, cacheHeaders.get(key));
        }
        return builder.build();
    }

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse uploadFile(@FormDataParam("uploadedFile") InputStream inputFile,
            @FormDataParam("uploadedFile") FormDataBodyPart fileBody,
            @DefaultValue("false" ) @FormDataParam("isPublic") boolean isPublic) throws Exception {
        try {
            UserDAO userDAO = new UserDAO(mainDAO.getSession(), getUserId());
            UserEntity currentUser = userDAO.fetchById(getUserId());
            if (currentUser == null) {
                MessageModel message = new MessageModel(401, "Invalid User!",
                        "You neeed to register new account to upload file.");
                return new DefaultResponse<>(message);
            }

            String[] fileInfo = FileManager.fileNameSplitter(fileBody.getContentDisposition().getFileName());
            FileEntity rawEntity = new FileEntity();
            rawEntity.setName(fileInfo[0]);
            if (fileInfo.length == 2) {
                rawEntity.setExtension(fileInfo[1]);
            }
            //TODO: To Change Back Public Access To `False` when File Permission Finish
            //rawEntity.setPublicAccess(false);
            rawEntity.setType(fileBody.getMediaType().toString());
            rawEntity.setPublicAccess(isPublic);
            rawEntity.setOwnerId(getUserId());
            rawEntity.setStatus(FileEntity.STORAGE);

            FileEntity entity = mainDAO.createFile(inputFile, rawEntity, true);

            this.modifiedResource();
            return new DefaultResponse<FileEntity>(entity);
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }

    @PermitAll
    @GET
    @Path("{id:\\d+}/public")
    public Response publicDownload(@PathParam("id") BigInteger id, 
            @DefaultValue("0") @QueryParam("width") int width,
            @DefaultValue("0") @QueryParam("height") int height, 
            @DefaultValue("false") @QueryParam("is64") boolean is64,
            @DefaultValue("false") @QueryParam("isDownload") boolean isDownload) throws Exception {
        FileEntity entity = mainDAO.fetchById(id);
        if (entity == null || !entity.isPublicAccess()) {
            MessageModel message = new MessageModel(204, "No File!", "There is no file for your request.");
            return Response.ok(new DefaultResponse<>(message), MediaType.APPLICATION_JSON).build();
        }

        Dimension dimension = null;
        if (width > 0 && height <= 0) {
            dimension = new Dimension(width, width);
        } else if (height > 0 && width <= 0) {
            dimension = new Dimension(height, height);
        } else if (width > 0 && height > 0) {
            dimension = new Dimension(width, height);
        }

        return this.downloadFile(entity, dimension, is64, isDownload);
    }

    @GET
    @Path("{id:\\d+}/download")
    public Response privateDownload(@PathParam("id") BigInteger id, 
            @DefaultValue("0") @QueryParam("width") int width,
            @DefaultValue("0") @QueryParam("height") int height,
            @DefaultValue("false") @QueryParam("is64") boolean is64,
            @DefaultValue("false") @QueryParam("isDownload") boolean isDownload) throws Exception {
        FileEntity entity = mainDAO.fetchById(id);
        if (entity == null) {
            MessageModel message = new MessageModel(204, "No File!", "There is no file for your request.");
            return Response.ok(new DefaultResponse<>(message), MediaType.APPLICATION_JSON).build();
        }

        Dimension dimension = null;
        if (width > 0 && height <= 0) {
            dimension = new Dimension(width, width);
        } else if (height > 0 && width <= 0) {
            dimension = new Dimension(height, height);
        } else if (width > 0 && height > 0) {
            dimension = new Dimension(width, height);
        }

        return this.downloadFile(entity, dimension, is64, isDownload);
    }

    @Override
    protected Logger getLogger() {
        return FileResource.LOG;
    }

    @PUT
    @Path("{id:\\d+}/rename")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse rename(FileEntity file, @PathParam("id") BigInteger id) {
        try {
            FileEntity f = this.mainDAO.fetchById(id);
            if (f == null) {
                MessageModel message = new MessageModel(204, "No Data", "There is no data for your request.");
                return new DefaultResponse<>(message);
            }

            f.setName(file.getName());
            f = this.mainDAO.update(f, true);
            this.modifiedResource();
            MessageModel message = new MessageModel(202, "SUCCESS", "Update Success.");
            return new DefaultResponse<>(202, ResponseType.SUCCESS, message);
        } catch (Exception e) {
            getLogger().error(e);
            throw e;
        }
    }
}
