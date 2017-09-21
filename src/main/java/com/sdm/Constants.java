package com.sdm;

import java.io.File;

/**
 * Global constants data.
 *
 * @author Htoonlin
 *
 */
public interface Constants {

    /**
     * IDE constants
     */
    public interface IDE {

        String JAVA_DIR = ("/src/main/java/com/sdm/").replace('/', File.separatorChar);
        String RESOURCE_DIR = ("/src/main/resources/").replace('/', File.separatorChar);
        String WEB_DIR = ("/src/main/webapp/").replace('/', File.separatorChar);
        String[] MODULE_DIRS = {"dao", "entity", "resource"};

        String PREV_PROJECT_DIR = "com.sdm.ide.PREV_DIR";
    }

    /**
     * Facebook Constants
     */
    public interface Facebook {

        String API_VERSION = "v2.10";
        String GRAPH_API = "https://graph.facebook.com/";
        String AUTH_SCOPE = "email,public_profile";
    }

    /**
     * Regex Patterns
     *
     * @author Htoonlin
     *
     */
    public interface Pattern {

        /**
         * Support Alpha numeric only
         */
        String ALPHA_NUMERIC = "^[a-zA-Z0-9]*$";
        /**
         * Support Character only without number
         */
        String CHARACTER_ONLY = "^[a-zA-Z]*$";

        /**
         * Must be integer
         */
        String INTEGER = "^-?\\d+$";

        /**
         * Must be positive integer
         */
        String POSITIVE_INTEGER = "^\\d+$";

        /**
         * Must be negative integer
         */
        String NEGATIVE_INTEGER = "^-\\d+$";

        /**
         * Must be number
         */
        String NUMBER = "^-?\\d*\\.?\\d+$";

        /**
         * Must be positive number
         */
        String POSITIVE_NUMBER = "^\\d*\\.?\\d+$";

        /**
         * Must be negative number
         */
        String NEGATIVE_NUMBER = "^-\\d*\\.?\\d+$";

        /**
         * Password validation
         */
        String STRONG_PASSWORD = "^(?=^.{6,}$)((?=.*[A-Za-z0-9])(?=.*[A-Z])(?=.*[a-z]))^.*$";
        /**
         * Color hexa code. example => #FF0000, #ff0000, #fff, #FFF
         */
        String COLOR_HEX = "^#?([a-f0-9]{8}|[a-f0-9]{6}|[a-f0-9]{4}|[a-f0-9]{3})$";
        /**
         * Email format
         */
        String EMAIL = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$";
        /**
         * URL format
         */
        String URL = "^(((http|https|ftp):\\/\\/)?([[a-zA-Z0-9]\\-\\.])+(\\.)([[a-zA-Z0-9]]){2,4}([[a-zA-Z0-9]\\/+=%&_\\.~?\\-]*))*$";
        /**
         * IP Format
         */
        String IP = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        /**
         * All credit card (Master, Visa, American Express)
         */
        String CREDIT_CARD = "^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|6011[0-9]{12}|622((12[6-9]|1[3-9][0-9])|([2-8][0-9][0-9])|(9(([0-1][0-9])|(2[0-5]))))[0-9]{10}|64[4-9][0-9]{13}|65[0-9]{14}|3(?:0[0-5]|[68][0-9])[0-9]{11}|3[47][0-9]{13})*$";
        /**
         * Master card
         */
        String MASTER_CARD = "^(5[1-5][0-9]{14})*$";
        /**
         * Visa card
         */
        String VISA_CARD = "^(4[0-9]{12}(?:[0-9]{3})?)*$";
        /**
         * American express card
         */
        String AMERICAN_EXPRESS_CARD = "^(3[47][0-9]{13})*$";
        /**
         * Other credit/debit cards
         */
        String UNKNOWN_CARD = "^(3(?:0[0-5]|[68][0-9])[0-9]{11})*$";
    }

    /**
     * System properties key for System
     *
     * @author Htoonlin
     *
     */
    public interface Setting {

        /**
         * SYSTEM_ENV: Supported variables => DEV, BETA, LIVE
         */
        String SYSTEM_ENV = "com.sdm.system.env";

        /**
         * Default writeable file path for system.
         */
        String ROOT_DIRECTORY = "com.sdm.path";

        /**
         * File upload path for system.
         */
        String UPLOAD_DIRECTORY = "com.sdm.path.storage";

        /**
         * JSP Template file path to use ITemplate Engine.
         */
        String TEMPLATE_DIRECTORY = "com.sdm.path.template";

        /**
         * Cache-Control max age [Second]
         */
        String CC_MAX_AGE = "com.sdm.cc.max_age";

        /**
         * Root User account ID. It will be access full permission of API.
         */
        String ROOT_ID = "com.sdm.security.root_id";

        /**
         * Expired [Day] time of generated token.
         */
        String AUTH_TOKEN_LIFE = "com.sdm.security.auth_token_life";

        /**
         * Cryptography KEY for JWT. It will generate from SecurityManager.
         */
        String JWT_KEY = "com.sdm.security.jwt_key";

        /**
         * Expired [Minute] time for new user registration.
         */
        String OTP_LIFE = "com.sdm.security.otp_life";

        /**
         * Expired [Minute] time to request api server. Don't need on SYSTEM_ENV
         * => DEV.
         */
        String SECURITY_TIMESTAMP_LIFE = "com.sdm.security.timestamp_life";

        /**
         * User password encryption salt key.
         */
        String ENCRYPT_SALT = "com.sdm.security.encrypt_salt";

        /**
         * These chars will include in Randomize token such as OTP.
         */
        String TOKEN_CHARS = "com.sdm.security.token_chars";

        /**
         * How many time user can try auth by token.
         */
        String AUTH_FAILED_COUNT = "com.sdm.security.auth_failed_count";

        /**
         * Display date time format.
         */
        String DATE_TIME_FORMAT = "com.sdm.format.date_time";

        /**
         * Display date format.
         */
        String DATE_FORMAT = "com.sdm.format.date";

        /**
         * Display time format.
         */
        String TIME_FORMAT = "com.sdm.format.time";

        /**
         * CORS allow origin value.
         */
        String CORS_ORIGIN = "com.sdm.cors.origin";

        /**
         * CORS allow methods such as (GET, POST, PUT, DELETE).
         */
        String CORS_METHODS = "com.sdm.cors.methods";

        /**
         * CORS max age.
         */
        String CORS_MAX_AGE = "com.sdm.cors.age";

        /**
         * Supported mail type => webmail, mailgun
         */
        String MAIL_TYPE = "com.sdm.mail";

        /**
         * Web mail host name. example => smtp.gmail.com
         */
        String MAIL_HOST = "com.sdm.mail.host";
        /**
         * Web mail port number. example => 465
         */
        String MAIL_PORT = "com.sdm.mail.port";

        /**
         * Web mail server need auth?
         */
        String MAIL_IS_AUTH = "com.sdm.mail.is_auth";

        /**
         * If webmail server need auth, enter user name.
         */
        String MAIL_USER = "com.sdm.mail.user";

        /**
         * If webmail server need auth, enter password.
         */
        String MAIL_PASSWORD = "com.sdm.mail.password";

        /**
         * Mailgun private key to access mailgun server.
         */
        String MAILGUN_PRI_KEY = "com.sdm.mailgun.private_key";

        /**
         * Mailgun public key to access mailgun server.
         */
        String MAILGUN_PUB_KEY = "com.sdm.mailgun.public_key";

        /**
         * Mailgun domain name for mail.
         */
        String MAILGUN_DOMAIN = "com.sdm.mailgun.domain";

        /**
         * Mailgun default mail sender.
         */
        String MAILGUN_DEFAULT_MAIL = "com.sdm.mailgun.default_mail";

        /**
         * Facebook api_id for OAuth
         */
        String FB_API_ID = "com.sdm.facebook.app_id";

        /**
         * Facebook verify token for messenger platform
         */
        String FB_MESSENGER_TOKEN = "com.sdm.facebook.messenger.token";
    }

    /**
     * Http Sesssion storage keys
     *
     * @author Htoonlin
     *
     */
    public interface SessionKey {

        /**
         * HttpSession key for auth failed count.
         */
        String FAILED_COUNT = "com.sdm.session.auth_failed_count";

        /**
         * HttpSession key for current user.
         */
        String USER_ID = "com.sdm.session.auth_user_id";

        /**
         * HttpSession key for current token.
         */
        String USER_TOKEN = "com.sdm.session.user_token";
    }

    /**
     * It will use to create user upload folder and JWT Key subject.
     */
    String USER_PREFIX = "user-";

    /**
     * Prefix of Authorization Token String
     */
    String AUTH_TYPE = "Bearer";

}
