/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util.security;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

/**
 *
 * @author Htoonlin
 */
public class RSAManager {

    private final String CRYPTO_METHOD = "RSA";
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public RSAManager(int size) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(CRYPTO_METHOD);
        keyPairGenerator.initialize(size);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    public String getPublicKey() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public void setPublicKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(CRYPTO_METHOD);
        this.publicKey = keyFactory.generatePublic(keySpec);
    }

    public String getPrivateKey() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public void setPrivateKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privateBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(CRYPTO_METHOD);
        this.privateKey = keyFactory.generatePrivate(keySpec);
    }

    public String encrypt(String data) throws Exception {
        byte[] dataToEncrypt = data.getBytes();
        byte[] encryptedData = null;
        if (publicKey == null) {
            throw new Exception("There is no key to encrypt.");
        }
        try {
            Cipher cipher = Cipher.getInstance(CRYPTO_METHOD);
            cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
            encryptedData = cipher.doFinal(dataToEncrypt);
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            throw e;
        }
    }

    public String decrypt(String data) throws Exception {
        byte[] dataToDecrypt = Base64.getDecoder().decode(data.replaceAll(" ", ""));
        byte[] decryptedData = null;
        if (privateKey == null) {
            throw new Exception("There is not key to decrypt.");
        }

        try {
            Cipher cipher = Cipher.getInstance(CRYPTO_METHOD);
            cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
            decryptedData = cipher.doFinal(dataToDecrypt);
            return new String(decryptedData);
        } catch (Exception e) {
            throw e;
        }
    }
}
