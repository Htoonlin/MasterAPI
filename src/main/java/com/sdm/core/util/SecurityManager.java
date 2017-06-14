/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util;

import com.sdm.core.Setting;
import com.sdm.core.util.security.RSAManager;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Htoonlin
 */
public class SecurityManager {
    public static String md5String(String salt, String input) {
        try {
            String saltString = (salt + input + Setting.getInstance().ENCRYPT_SALT);
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(saltString.getBytes(), 0, saltString.length());
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
    
    public static void main(String[] args){
        try {
            RSAManager manager = new RSAManager(128);
            String publicKey = manager.getPublicKey();
            String privateKey = manager.getPrivateKey();
            String secret = "19324901419:cd07922c-7d85-4b8b-b150-f275e3e2fcd3";
            String signature = manager.encrypt(secret);            
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
