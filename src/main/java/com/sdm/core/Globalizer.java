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
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Htoonlin
 */
public class Globalizer {

    public static final String AUTH_SUBJECT_PREFIX = "USER-";
    public static final String AUTH_TYPE = "Bearer";
    public static final String SESSION_USER_ID = "CURRENT_AUTH_USER_ID";
    public static final String SESSION_USER_TOKEN = "CURRENT_AUTH_USER_TOKEN";

    public static ObjectMapper jsonMapper() {
        ObjectMapper mapper = new ObjectMapper();
        //mapper.setDateFormat(Setting.getInstance().DATE_TIME_FORMAT);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.enable(DeserializationFeature.WRAP_EXCEPTIONS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        return mapper;
    }

    public static String camelToLowerUnderScore(String input) {
        /*String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        return input.replaceAll(regex, replacement).toLowerCase();*/
        return (new PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy()).translate(input);
    }

    public static String camelToReadable(String input) {
        return input.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
    }

    public static boolean isHttpSuccess(String code) {
        if (code.matches("\\d{3}")) {
            int status = Integer.parseInt(code);
            return (status >= 100 && status <= 511);
        }
        return false;
    }

    public static String getBasePath(HttpServletRequest request) {
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
        cal.add(Calendar.DAY_OF_MONTH, Setting.getInstance().AUTH_TOKEN_LIFE);
        return cal.getTime();
    }

    public static boolean validTimeStamp(Date timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        cal.add(Calendar.MINUTE, Setting.getInstance().SECURITY_TIMESTAMP_LIFE);
        Date checkDate = cal.getTime();
        return checkDate.after(new Date());
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
}
