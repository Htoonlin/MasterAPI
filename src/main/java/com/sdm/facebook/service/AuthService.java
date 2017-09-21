package com.sdm.facebook.service;

import org.apache.commons.httpclient.HttpStatus;
import org.json.JSONObject;

import com.sdm.Constants;
import com.sdm.core.util.SecurityManager;
import com.sdm.facebook.model.GraphResponse;
import com.sdm.facebook.util.GraphManager;
import com.sdm.master.dao.FileDAO;
import com.sdm.master.dao.UserDAO;
import com.sdm.master.entity.FileEntity;
import com.sdm.master.entity.UserEntity;

public class AuthService extends GraphManager {

    private final String REQUEST_FIELDS = "id, name, email, locale, picture.width(512).height(512), gender, age_range";
    private final UserDAO userDao;

    private class FacebookUser {

        public String id;
        public String name;
        public String email;
        public String locale;
        public String picture;
        public String gender;
        public String ageRange;
    }

    public AuthService(String accessToken, UserDAO dao) {
        super(accessToken);
        this.userDao = dao;
    }

    public UserEntity authByFacebook() {
        GraphResponse response = this.setPath("me").addQuery("fields", this.REQUEST_FIELDS).getRequest();
        if (response.getStatus() == HttpStatus.SC_OK) {
            JSONObject jsonBody = new JSONObject(response.getBody());
            FacebookUser facebookUser = this.convertToUser(jsonBody);
            UserEntity authUser = userDao.userAuthByFacebook(facebookUser.id);
            // If new user, migrate or create user
            if (authUser == null) {
                authUser = userDao.getUserByEmail(facebookUser.email);
                if (authUser == null) {
                    // New user registration with random password
                    String randomPassword = SecurityManager.randomPassword(32);
                    authUser = new UserEntity();

                    authUser.setEmail(facebookUser.email);
                    authUser.setPassword(SecurityManager.hashString(facebookUser.email, randomPassword));
                    authUser.setStatus(UserEntity.ACTIVE);
                    this.mergeFacebookUser(authUser, facebookUser);
                    authUser = userDao.insert(authUser, false);
                } else {
                    this.mergeFacebookUser(authUser, facebookUser);
                    return userDao.update(authUser, false);
                }
            } else if (!authUser.getFacebookId().equals(facebookUser.id)) {
                authUser = null;
            }
            return authUser;
        }
        return null;

    }

    private FacebookUser convertToUser(JSONObject json) {
        FacebookUser user = new FacebookUser();

        if (json.has("id")) {
            user.id = json.getString("id");
        }

        if (json.has("name")) {
            user.name = json.getString("name");
        }

        if (json.has("email")) {
            user.email = json.getString("email");
        }

        if (json.has("locale")) {
            user.locale = json.getString("locale");
        }

        if (json.has("gender")) {
            user.gender = json.getString("gender");
        }

        if (json.has("age_range")) {
            user.ageRange = json.getJSONObject("age_range").toString();
        }

        user.picture = Constants.Facebook.GRAPH_API + Constants.Facebook.API_VERSION
                + "/me/picture?width=512&height=512";

        if (json.has("picture")) {
            if (json.getJSONObject("picture").has("data")) {
                if (json.getJSONObject("picture").getJSONObject("data").has("url")) {
                    user.picture = json.getJSONObject("picture").getJSONObject("data").getString("url");
                }
            }
        }

        return user;
    }

    private void mergeFacebookUser(UserEntity userEntity, FacebookUser facebookUser) {
        userEntity.setFacebookId(facebookUser.id);
        userEntity.setDisplayName(facebookUser.name);
        userEntity.setOtpExpired(null);
        userEntity.setOtpToken(null);
        userEntity.setStatus(UserEntity.ACTIVE);

        userEntity.addExtra("locale", facebookUser.locale);
        userEntity.addExtra("gender", facebookUser.gender);
        userEntity.addExtra("age_range", facebookUser.ageRange);

        // Create User Profile Picture by Facebook
        FileEntity userPicture = new FileEntity();
        userPicture.setExternalURL(facebookUser.picture);
        userPicture.setOwnerId(userEntity.getId());
        userPicture.setName(facebookUser.name);
        userPicture.setExtension("jpg");
        userPicture.setPublicAccess(true);
        FileDAO fileDao = new FileDAO(userDao.getSession(), userDao.getUserId());
        userEntity.setProfileImage(fileDao.insert(userPicture, false));
    }
}
