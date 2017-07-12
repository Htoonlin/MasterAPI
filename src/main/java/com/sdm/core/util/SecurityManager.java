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
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.log4j.Logger;

import com.sdm.core.Setting;

import io.jsonwebtoken.impl.crypto.MacProvider;

/**
 *
 * @author Htoonlin
 */
public class SecurityManager {

	private static final Logger LOG = Logger.getLogger(SecurityManager.class);

	public static String generateSalt() {
		SecureRandom random;
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException ex) {
			LOG.error(ex);
			random = new SecureRandom();
		}
		byte salt[] = new byte[64];
		random.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}

	public static String hashString(String salt, String input) {
		String systemSalt = Setting.getInstance().get(Setting.ENCRYPT_SALT, generateSalt());
		LOG.info("Preparing to encrypt data....");
		final int iterations = 1000;
		final int keyLength = 512;
		char[] password = input.toCharArray();
		byte[] staticSalt = Base64.getDecoder().decode(systemSalt);
		try {
			PBEKeySpec spec = new PBEKeySpec(password, staticSalt, iterations, keyLength);
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			String inputHex = new BigInteger(skf.generateSecret(spec).getEncoded()).toString(16);

			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(salt.getBytes());
			String saltHex = new BigInteger(1, digest.digest()).toString(16);
			String encryptedString = saltHex + inputHex;
			LOG.info("Successfully encrypted data.");
			return encryptedString;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			LOG.error(e);
		}

		return base64Encode(salt + input + systemSalt);
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
