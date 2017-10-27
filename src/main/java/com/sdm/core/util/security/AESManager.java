/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util.security;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.log4j.Logger;

/**
 *
 * @author Htoonlin
 */
public class AESManager {

    private static final Logger LOG = Logger.getLogger(AESManager.class.getName());

    private final String CRYPTO_METHOD = "AES";
    private final String CRYPTO_PAIR = "AES/CBC/PKCS5Padding";
    private final int KEY_SIZE = 256;

    Cipher AESCipher;

    public AESManager() {
    }

    private void initCipher(int mode, String key) {
        try {
            AESCipher = Cipher.getInstance(CRYPTO_PAIR);

            byte[] keyBytes = new byte[16];
            byte[] b = key.getBytes("UTF-8");
            int len = b.length;
            if (len > keyBytes.length) {
                len = keyBytes.length;
            }
            System.arraycopy(b, 0, keyBytes, 0, len);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, CRYPTO_METHOD);
            IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
            AESCipher.init(mode, keySpec, ivSpec);
        } catch (Exception ex) {
            LOG.error(ex);
        }
    }

    public String generateKey() throws NoSuchAlgorithmException {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(CRYPTO_METHOD);
            keyGenerator.init(KEY_SIZE);
            SecretKey secKey = keyGenerator.generateKey();
            return Base64.getEncoder().encodeToString(secKey.getEncoded());
        } catch (NoSuchAlgorithmException ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    public String encrypt(String plainText, String key) throws IllegalBlockSizeException, BadPaddingException {
        try {
            byte[] encryptBytes = plainText.getBytes();
            this.initCipher(Cipher.ENCRYPT_MODE, key);
            byte[] byteCipher = AESCipher.doFinal(encryptBytes);
            return Base64.getEncoder().encodeToString(byteCipher);
        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    public String decrypt(String cipherText, String key) throws GeneralSecurityException {
        try {
            byte[] decryptBytes = Base64.getDecoder().decode(cipherText);
            this.initCipher(Cipher.DECRYPT_MODE, key);
            byte[] bytePlain = AESCipher.doFinal(decryptBytes);
            return new String(bytePlain);
        } catch (GeneralSecurityException ex) {
            LOG.error(ex);
            throw ex;
        }
    }
}
