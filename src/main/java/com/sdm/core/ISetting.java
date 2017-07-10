package com.sdm.core;

interface ISetting {
	/* System Setting */
    String PROP_ENV = "com.sdm.system.env";

    /* Storage Settings */
    String PROP_STORAGE_PATH = "com.sdm.path.storage";
    String PROP_TEMPLATE_PATH = "com.sdm.path.template";

    /* Security Setting */
    String PROP_ROOT_ID = "com.sdm.security.root_id";
    String PROP_AUTH_TOKEN_LIFE = "com.sdm.security.auth_token_life";
    String PROP_JWT_KEY = "com.sdm.security.jwt_key";
    String PROP_OTP_LIFE = "com.sdm.security.otp_life";
    String PROP_SECURITY_TIMESTAMP_LIFE = "com.sdm.security.timestamp_life";
    String PROP_ENCRYPT_SALT = "com.sdm.security.encrypt_salt";
    String PROP_TOKEN_CHARS = "com.sdm.security.token_chars";
    String PROP_AUTH_FAILED_COUNT = "com.sdm.security.auth_failed_count";

    /* Date Time Setting */
    String PROP_DATE_TIME_FORMAT = "com.sdm.format.date_time";
    String PROP_DATE_FORMAT = "com.sdm.format.date";
    String PROP_TIME_FORMAT = "com.sdm.format.time";

    /* CORS Setting */
    String PROP_CORS_ORIGIN = "com.sdm.cors.origin";
    String PROP_CORS_METHODS = "com.sdm.cors.methods";
    String PROP_CORS_HEADERS = "com.sdm.cors.headers";
    String PROP_CORS_MAX_AGE = "com.sdm.cors.age";

    /* Mail Setting */
    String PROP_MAIL_TYPE = "com.sdm.mail";
    
    String PROP_MAIL_HOST = "com.sdm.mail.host";
    String PROP_MAIL_PORT = "com.sdm.mail.port";
    String PROP_MAIL_IS_AUTH = "com.sdm.mail.is_auth";
    String PROP_MAIL_USER = "com.sdm.mail.user";
    String PROP_MAIL_PASSWORD = "com.sdm.mail.password";
    
    String PROP_MAILGUN_PRI_KEY= "com.sdm.mailgun.private_key";
    	String PROP_MAILGUN_PUB_KEY= "com.sdm.mailgun.public_key";
    	String PROP_MAILGUN_DOMAIN="com.sdm.mailgun.domain";
    	String PROP_MAILGUN_DEFAULT_MAIL= "com.sdm.mailgun.default_mail";
}
