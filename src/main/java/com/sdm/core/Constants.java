package com.sdm.core;

public interface Constants {
	/**
	 * It will use to create user upload folder and JWT Key subject.
	 */
	String USER_PREFIX = "user-";

	/**
	 * Prefix of Authorization Token String
	 */
	String AUTH_TYPE = "Bearer";

	/**
	 * HttpSession key for auth failed count.
	 */
	String SESSION_FAILED_COUNT = "com.sdm.session.auth_failed_count";

	/**
	 * HttpSession key for current user.
	 */
	String SESSION_USER_ID = "com.sdm.session.auth_user_id";

	/**
	 * HttpSession key for current token.
	 */
	String SESSION_USER_TOKEN = "com.sdm.session.user_token";

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
	 * Expired [Minute] time to request api server. Don't need on SYSTEM_ENV => DEV.
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
}
