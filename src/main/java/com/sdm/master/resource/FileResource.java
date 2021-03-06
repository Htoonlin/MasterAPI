/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import com.sdm.core.Setting;
import com.sdm.core.resource.RestResource;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.core.response.model.MessageModel;
import com.sdm.core.util.FileManager;
import com.sdm.master.dao.FileDAO;
import com.sdm.master.entity.FileEntity;
import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Map;
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
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author Htoonlin
 */
@Path("files")
public class FileResource extends RestResource<FileEntity, Long> {

    private final int CACHE_AGE = 3600 * 24 * 7;

    private Response buildWithCache(ResponseBuilder builder) {
        // Create cache with 1 week
        Map<String, Object> cacheHeaders = this.buildCache(CACHE_AGE);
        //Set Cache Headers
        for (String key : cacheHeaders.keySet()) {
            builder.header(key, cacheHeaders.get(key));
        }
        return builder.build();
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

        byte[] data;

        try {
            // If it is image and include dimension, it will process image on dimension
            if (entity.getType().contains("image") && dimension != null) {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                Thumbnails.of(savedFile).size(dimension.width, dimension.height).keepAspectRatio(true)
                        .useOriginalFormat().toOutputStream(output);
                data = output.toByteArray();
            } else {
                data = Files.readAllBytes(savedFile.toPath());
            }
        } catch (IOException ex) {
            getLogger().error(ex.getMessage(), ex);
            return Response.noContent().build();
        }

        if (is64) {
            String base64String = Base64.getMimeEncoder().encodeToString(data);
            ResponseBuilder builder = Response.ok(base64String, MediaType.TEXT_PLAIN);
            return buildWithCache(builder);
        }

        StreamingOutput fileStream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    output.write(data);
                    output.flush();
                } catch (IOException ex) {
                    getLogger().error(ex.getMessage(), ex);
                    throw new WebApplicationException("File not found!");
                } finally {
                    output.close();
                }
            }
        };

        ResponseBuilder builder = Response.ok(fileStream, entity.getType());

        //if download directly
        if (isDownload) {
            String fileName = entity.getName() + "." + entity.getExtension();
            builder.header("content-disposition", "attachment; filename=\"" + fileName + "\"");
        }

        return buildWithCache(builder);
    }

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse uploadFile(@FormDataParam("uploadedFile") InputStream inputFile,
            @FormDataParam("uploadedFile") FormDataBodyPart fileBody,
            @DefaultValue("false") @FormDataParam("isPublic") boolean isPublic) {
        try {
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
            rawEntity.setOwnerId(getCurrentUserId());
            rawEntity.setStatus(FileEntity.STORAGE);
            FileDAO fileDAO = new FileDAO(getDAO().getSession(), this);

            FileEntity entity = fileDAO.createFile(inputFile, rawEntity, true);

            this.modifiedResource();
            return new DefaultResponse<FileEntity>(entity);
        } catch (Exception e) {
            getLogger().error(e);
            throw e;
        }
    }

    @PermitAll
    @GET
    @Path("{id:\\d+}/public")
    public Response publicDownload(@PathParam("id") long id,
            @DefaultValue("0") @QueryParam("width") int width,
            @DefaultValue("0") @QueryParam("height") int height,
            @DefaultValue("false") @QueryParam("is64") boolean is64,
            @DefaultValue("false") @QueryParam("isDownload") boolean isDownload) {
        FileEntity entity = this.checkData(id);

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
    public Response privateDownload(@PathParam("id") long id,
            @DefaultValue("0") @QueryParam("width") int width,
            @DefaultValue("0") @QueryParam("height") int height,
            @DefaultValue("false") @QueryParam("is64") boolean is64,
            @DefaultValue("false") @QueryParam("isDownload") boolean isDownload) {

        FileEntity entity = this.checkData(id);

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

    @PUT
    @Path("{id:\\d+}/rename")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse rename(FileEntity file, @PathParam("id") long id) {
        try {
            FileEntity entity = this.checkData(id);

            FileDAO fileDAO = new FileDAO(getDAO().getSession(), this);

            entity.setName(file.getName());
            entity = fileDAO.update(entity, true);
            this.modifiedResource();
            MessageModel message = new MessageModel(202, "SUCCESS", "Update Success.");
            return new DefaultResponse<>(202, ResponseType.SUCCESS, message);
        } catch (Exception e) {
            getLogger().error(e);
            throw e;
        }
    }
}
