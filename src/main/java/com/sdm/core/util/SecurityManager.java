/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import com.sdm.core.Setting;

import io.jsonwebtoken.impl.crypto.MacProvider;

/**
 *
 * @author Htoonlin
 */
public class SecurityManager {

	public static String generateSalt(int length) {
		final SecureRandom random = new SecureRandom();
		byte salt[] = new byte[length];
		random.nextBytes(salt);
		return new String(salt);
	}

	public static String md5String(String salt, String input) {
		try {
			String staticSalt = Setting.getInstance().get(Setting.ENCRYPT_SALT, SecurityManager.generateSalt(16));
			String saltedString = (salt + input + staticSalt);
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(saltedString.getBytes(), 0, saltedString.length());
			return new BigInteger(1, digest.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return (salt + input);
	}

	public static String base64Encode(String normal) {
		byte[] data;
		try {
			data = normal.getBytes("UTF-8");
		} catch (UnsupportedEncodingException ex) {
			data = normal.getBytes(Charset.defaultCharset());
		}
		return Base64.getEncoder().encodeToString(data);
	}

	public static String base64Decode(String base64) {
		byte[] data = Base64.getDecoder().decode(base64);
		return new String(data);
	}

	public static String generateJWTKey() {
		byte[] key = MacProvider.generateKey().getEncoded();
		return Base64.getEncoder().encodeToString(key);
	}
}
