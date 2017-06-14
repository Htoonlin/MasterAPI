/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util;

import com.sdm.core.Globalizer;
import com.sdm.core.Setting;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodec;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

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

    public static void main(String[] args) {
        System.out.println((new Date()).getTime());
        /*byte[] key = MacProvider.generateKey().getEncoded();
        String keyString = Base64.getEncoder().encodeToString(key);
        System.out.println(keyString);
        String compactJWS = Jwts.builder()
                .setSubject("User-1")
                .setIssuer("test-device-id")                
                .setIssuedAt(new Date())
                .setExpiration(Globalizer.getTokenExpired())
                .setId("cd07922c-7d85-4b8b-b150-f275e3e2fcd3")                                
                .compressWith(CompressionCodecs.DEFLATE)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        System.out.println(compactJWS);*/

        String key = "8syWRCkI+Ea/4gUQ2A+z0u5pb9bEd6Umve6a0Gyg3VL0FNxzbZOk6D5UM1FrAy2wEYacEoQBzz8xkd/a63rt/w==";
        String token = "eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNoky0EKhDAMQNG7ZG3ApqlR7zEHsE2EupJJR4Rh7j6Ky__gf8E_GWZ4ub0xQAfV_cpm3lDtqMWw6s1LgznwJDwwx9CBnfsDQnG4YWv1Gov2MhEVFB0Tch4z5pB6XEmSRaO1aITfHwAA__8.tWRNJckwIvXh1BKzY2iPZlOZYZ9kUYnGflPR3uIjZFrq3WvWA_eP8E0vWezek8aMa7CIFTIgX7RXSBJdpeWC3Q";
        Claims tokenInfo = Jwts.parser().setSigningKey(Base64.getDecoder().decode(key)).parseClaimsJws(token).getBody();
        System.out.println(tokenInfo);
        System.out.println((new Date()).getTime());
    }
}
