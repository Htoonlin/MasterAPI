/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sdm.core.util.security.AccessType;
import com.sdm.core.util.security.AccessorType;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Htoonlin
 */
public class Globalizer {

    public static ObjectMapper jsonMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.enable(DeserializationFeature.WRAP_EXCEPTIONS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        return mapper;
    }

    public static String camelToLowerUnderScore(String input) {
        return (new PropertyNamingStrategy.SnakeCaseStrategy()).translate(input);
    }

    public static String camelToReadable(String input) {
        return input.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
                "(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
    }

    public static String capitalize(String word) {
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }

    public static boolean isHttpSuccess(String code) {
        if (code.matches("\\d{3}")) {
            int status = Integer.parseInt(code);
            return (status >= 100 && status <= 511);
        }
        return false;
    }

    public static String getSystemURL(HttpServletRequest request) {
        String url = "";
        String schema = request.getScheme();
        String server = request.getServerName();
        String contextPath = request.getContextPath();
        int port = request.getServerPort();
        if (port == 80 || port == 443) {
            url = String.format("%s://%s", schema, server);
        } else {
            url = String.format("%s://%s:%s", schema, server, port);
        }

        return url + contextPath;
    }

    public static String getDateString(String format, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static Date getTokenExpired() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int day = Setting.getInstance().getInt(Setting.AUTH_TOKEN_LIFE, "30");
        cal.add(Calendar.DAY_OF_YEAR, day);
        return cal.getTime();
    }

    public static boolean validTimeStamp(Date timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        int minute = Setting.getInstance().getInt(Setting.SECURITY_TIMESTAMP_LIFE, "5");
        cal.add(Calendar.MINUTE, minute);
        Date checkDate = cal.getTime();
        return checkDate.after(new Date());
    }

    public static String generateToken(int length) {
        String chars = Setting.getInstance().get(Setting.TOKEN_CHARS, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        return generateToken(chars, length);
    }

    public static String generateToken(String chars, int length) {
        SecureRandom rnd = new SecureRandom();
        StringBuilder pass = new StringBuilder();
        for (int i = 0; i < length; i++) {
            pass.append(chars.charAt(rnd.nextInt(chars.length())));
        }

        return pass.toString();
    }

    public static boolean hacAccess(String permission, AccessorType accessor, AccessType type) {
        byte allow = (byte) Character.digit(permission.charAt(accessor.ordinal()), 16);
        byte access = (byte) type.getValue();
        return ((allow & access) == access);
    }
    
    public static boolean isEmail(String email){
        Pattern pattern=Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        return pattern.matcher(email).matches();
    }
}
