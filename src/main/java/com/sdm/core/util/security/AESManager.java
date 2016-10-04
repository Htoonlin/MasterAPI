/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util.security;

import com.sdm.core.Setting;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Htoonlin
 */
public class AESManager {

    private final String CRYPTO_METHOD = "AES";
    private final String CRYPTO_PAIR = "AES/CBC/PKCS5Padding";
    private final int KEY_SIZE = 256;    

    Cipher AESCipher;
    
    public static AESManager getInstance() {
        return new AESManager();
    }

    private void initCipher(int mode) throws Exception {
        AESCipher = Cipher.getInstance(CRYPTO_PAIR);
        byte[] keyBytes = new byte[16];
        byte[] b = Setting.getInstance().AES_KEY.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, CRYPTO_METHOD);
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        AESCipher.init(mode, keySpec, ivSpec);
    }

    public String generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(CRYPTO_METHOD);
        keyGenerator.init(KEY_SIZE);
        SecretKey secKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secKey.getEncoded());
    }

    public String encrypt(String plainText) throws Exception {
        byte[] encryptBytes = plainText.getBytes();
        this.initCipher(Cipher.ENCRYPT_MODE);
        byte[] byteCipher = AESCipher.doFinal(encryptBytes);
        return Base64.getEncoder().encodeToString(byteCipher);
    }

    public String decrypt(String cipherText) throws Exception {
        byte[] decryptBytes = Base64.getDecoder().decode(cipherText);
        this.initCipher(Cipher.DECRYPT_MODE);
        byte[] bytePlain = AESCipher.doFinal(decryptBytes);
        return new String(bytePlain);
    }
}
