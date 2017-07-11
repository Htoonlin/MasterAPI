/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util;

import java.io.File;
import java.util.Date;

import com.sdm.core.Globalizer;
import com.sdm.core.Setting;

/**
 *
 * @author Htoonlin
 */
public class FileManager {

	public static final String[] SIZE_CODES = new String[] { "", "K", "M", "G", "T", "P", "E", "Z", "Y" };

	public static String byteSize(long size) {
		if (size <= 1024) {
			return "1 KB";
		}
		float resultSize = size;
		String result = resultSize + " T";
		for (String code : SIZE_CODES) {
			if (resultSize < 1024) {
				result = (Math.round(resultSize * 100.0) / 100.0) + " " + code;
				break;
			}
			resultSize /= 1024;
		}
		return result + "B";
	}

	public static String[] fileNameSplitter(String fileName) {
		String[] fileInfo = fileName.split("\\.(?=[^\\.]+$)");
		if (fileInfo.length < 2) {
			return new String[] { fileName };
		}
		return fileInfo;
	}

	/*
	 * public static String publicFileURL(String token, String ext) { String uri =
	 * Setting.getInstance().BASE_PATH + "/file/public/{token}.{ext}"; return
	 * UriBuilder.fromUri(uri) .resolveTemplate("token", token)
	 * .resolveTemplate("ext", ext).build().toString(); }
	 */

	public static String generateToken() {
		return Globalizer.generateToken(
				Setting.getInstance().get(Setting.TOKEN_CHARS, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"), 10) + "-"
				+ Globalizer.getDateString("yyyyMMddHHmmss", new Date());
	}

	public static File generateFile(int userId, String token, String ext) {
		String uploadPath = "/User-" + userId + Globalizer.getDateString("/yyyy/MMMM/", new Date());
		String fileName = token + "." + ext;

		File baseDir = new File(
				Setting.getInstance().get(Setting.FILE_STORAGE_PATH, "/var/www/master-api/upload/") + uploadPath);
		if (!baseDir.exists()) {
			baseDir.mkdirs();
		}
		return new File(baseDir, fileName);
	}
}
