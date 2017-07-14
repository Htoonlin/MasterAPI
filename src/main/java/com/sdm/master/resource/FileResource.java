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
import javax.ws.rs.core.CacheControl;
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
import com.sdm.core.response.model.MessageModel;
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
			mainDAO = new FileDAO(getUserId());
		}
	}

	private Response downloadFile(final FileEntity entity) {
		StreamingOutput fileStream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				try {
					String filePath = Setting.getInstance().get(Setting.UPLOAD_DIRECTORY) + entity.getStoragePath();
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
		CacheControl cc = new CacheControl();

		// Cache will hold 30 days to file
		cc.setMaxAge((3600 * 24 * 30));
		cc.setNoStore(false);
		cc.setNoTransform(true);

		String fileName = entity.getName() + "." + entity.getExtension();

		return Response.ok(fileStream, entity.getType()).cacheControl(cc)
				.header("content-disposition", "attachment; filename=\"" + fileName + "\"").build();
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
			FileEntity entity = mainDAO.saveFile(inputFile, fileDetail);
			this.modifiedResource();
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
		MessageModel message = new MessageModel(204, "No File!", "There is no file for your requested token.");
		return Response.ok(new DefaultResponse<>(message), MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("{id:\\d+}/download")
	public Response privateDownload(@PathParam("id") double id) throws Exception {
		FileEntity entity = mainDAO.fetchById(id);
		if (entity == null) {
			MessageModel message = new MessageModel(204, "No File!", "There is no file for your request.");
			return Response.ok(new DefaultResponse<>(message), MediaType.APPLICATION_JSON).build();
		}

		return this.downloadFile(entity);
	}

	@Override
	protected Logger getLogger() {
		return FileResource.LOG;
	}
}
