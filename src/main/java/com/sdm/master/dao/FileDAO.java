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
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.hibernate.Session;

import com.sdm.core.Setting;
import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.core.util.FileManager;
import com.sdm.master.entity.FileEntity;

/**
 *
 * @author Htoonlin
 */
public class FileDAO extends RestDAO {

	private static final Logger LOG = Logger.getLogger(FileDAO.class.getName());
	private final String GET_BY_TOKEN = "FROM FileEntity f WHERE f.publicToken = :token AND f.extension = :extension";

	public FileDAO(int userId) {
		super(FileEntity.class.getName(), userId);
	}

	public FileDAO(Session session, int userId) {
		super(session, FileEntity.class.getName(), userId);
	}

	public FileEntity fetchByToken(String token, String ext) throws Exception {
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("token", token);
			params.put("extension", ext);
			return fetchOne(GET_BY_TOKEN, params);
		} catch (Exception e) {
			throw e;
		}
	}

	public void delete(FileEntity entity, boolean commit) throws Exception {
		File savedFile = new File(Setting.getInstance().get(Setting.FILE_STORAGE_PATH) + entity.getStoragePath());
		if (savedFile.exists() && savedFile.delete()) {
			LOG.info("Deleted File by ID => " + entity.getId());
		}
		super.delete(entity, commit);
	}

	public FileEntity saveFile(InputStream fileStream, FormDataContentDisposition fileDetail)
			throws IOException, Exception {
		String[] fileInfo = FileManager.fileNameSplitter(fileDetail.getFileName());
		FileEntity entity = new FileEntity();
		entity.setName(fileInfo[0]);
		if (fileInfo.length == 2) {
			entity.setExtension(fileInfo[1]);
		}
		String token = FileManager.generateToken();
		File saveFile = FileManager.generateFile(USER_ID, token, entity.getExtension());

		try (OutputStream out = new FileOutputStream(saveFile)) {
			int read;
			byte[] bytes = new byte[1024];
			while ((read = fileStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
		} catch (IOException e) {
			throw e;
		}
		String type = Files.probeContentType(saveFile.toPath());
		if (type == null || type.length() <= 0) {
			type = "application/" + entity.getExtension();
		}

		entity.setType(type);
		entity.setPublicToken(token);
		entity.setFileSize(saveFile.length());
		entity.setStoragePath(
				saveFile.getPath().substring(Setting.getInstance().get(Setting.FILE_STORAGE_PATH).length()));
		return super.insert(entity, true);
	}
}
