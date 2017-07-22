/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.sdm.core.Setting;
import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.core.resource.RestResource;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.model.MessageModel;
import com.sdm.core.util.FileManager;
import com.sdm.master.dao.FileDAO;
import com.sdm.master.dao.UserDAO;
import com.sdm.master.entity.FileEntity;
import com.sdm.master.entity.UserEntity;

import net.coobird.thumbnailator.Thumbnails;

/**
 *
 * @author Htoonlin
 */
@Path("file")
public class FileResource extends RestResource<FileEntity, BigInteger> {

	private static final Logger LOG = Logger.getLogger(FileResource.class.getName());
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

	private Response downloadFile(final FileEntity entity, final Dimension dimension) {
		DefaultResponse cacheResponse = this.validateCache();
		if (cacheResponse != null) {
			return Response.ok(cacheResponse, MediaType.APPLICATION_JSON).build();
		}

		StreamingOutput fileStream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				try {
					String filePath = Setting.getInstance().get(Setting.UPLOAD_DIRECTORY) + entity.getStoragePath();
					File savedFile = new File(filePath);
					if (savedFile.exists()) {
						// If it is image and include dimension, it will process image on dimension
						if (entity.getType().contains("image") && dimension != null) {
							Thumbnails.of(savedFile).size(dimension.width, dimension.height).keepAspectRatio(true)
									.useOriginalFormat().toOutputStream(output);
						} else {
							byte[] data = Files.readAllBytes(savedFile.toPath());
							output.write(data);
							output.flush();
						}
					}
				} catch (Exception e) {
					throw new WebApplicationException("File not found!");
				}
			}
		};

		//Create cache with 1 week
		Map<String, Object> cacheHeaders = this.buildCache(3600 * 24 * 7);
		
		String fileName = entity.getName() + "." + entity.getExtension();

		ResponseBuilder builder = Response.ok(fileStream, entity.getType())
				.header("content-disposition", "attachment; filename=\"" + fileName + "\"");
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
			@FormDataParam("uploadedFile") FormDataContentDisposition fileDetail) throws Exception {
		try {
			UserDAO userDAO = new UserDAO(mainDAO.getSession(), getUserId());
			UserEntity currentUser = userDAO.fetchById(getUserId());
			if (currentUser == null) {
				MessageModel message = new MessageModel(401, "Invalid User!",
						"You neeed to register new account to upload file.");
				return new DefaultResponse<>(message);
			}

			String[] fileInfo = FileManager.fileNameSplitter(fileDetail.getFileName());
			FileEntity rawEntity = new FileEntity();
			rawEntity.setName(fileInfo[0]);
			if (fileInfo.length == 2) {
				rawEntity.setExtension(fileInfo[1]);
			}
			rawEntity.setPublicAccess(false);
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
	public Response publicDownload(@PathParam("id") BigInteger id, @DefaultValue("0") @QueryParam("width") int width,
			@DefaultValue("0") @QueryParam("height") int height) throws Exception {
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

		return this.downloadFile(entity, dimension);
	}

	@GET
	@Path("{id:\\d+}/download")
	public Response privateDownload(@PathParam("id") BigInteger id, @DefaultValue("0") @QueryParam("width") int width,
			@DefaultValue("0") @QueryParam("height") int height) throws Exception {
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

		return this.downloadFile(entity, dimension);
	}

	@Override
	protected Logger getLogger() {
		return FileResource.LOG;
	}
}
