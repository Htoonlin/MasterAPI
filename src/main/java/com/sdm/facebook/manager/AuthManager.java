package com.sdm.facebook.manager;

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

public class AuthManager extends GraphManager {

	private final String REQUEST_FIELDS = "id, name, email, locale, picture.width(512).height(512)";
	private final UserDAO userDao;

	private class FacebookUser {
		public String id;
		public String name;
		public String email;
		public String locale;
		public String picture;
	}

	public AuthManager(String accessToken, UserDAO dao) {
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
				authUser = this.facebookMigrate(facebookUser);
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

	private UserEntity facebookMigrate(FacebookUser facebookUser) {
		UserEntity userEntity = userDao.getUserByEmail(facebookUser.email);
		if (userEntity == null) {
			String randomPassword = SecurityManager.randomPassword(32);
			// New user registration with random password
			userEntity = new UserEntity();
			userEntity.setFacebookId(facebookUser.id);
			userEntity.setDisplayName(facebookUser.name);
			userEntity.setEmail(facebookUser.email);
			userEntity.setPassword(SecurityManager.hashString(facebookUser.email, randomPassword));
			userEntity.addExtra("locale", facebookUser.locale);
			userEntity.setStatus(UserEntity.ACTIVE);
			userEntity = userDao.insert(userEntity, false);

			// Create User Profile Picture by Facebook
			FileEntity userPicture = new FileEntity();
			userPicture.setExternalURL(facebookUser.picture);
			userPicture.setOwnerId(userEntity.getId());
			userPicture.setName(facebookUser.name);
			userPicture.setExtension("jpg");
			userPicture.setPublicAccess(true);
			FileDAO fileDao = new FileDAO(userDao.getSession(), userDao.getUserId());
			userEntity.setProfileImage(fileDao.insert(userPicture, false));

			return userEntity;
		} else {
			userEntity.setFacebookId(facebookUser.id);
			userEntity.addExtra("locale", facebookUser.locale);
			return userDao.update(userEntity, false);
		}
	}
}
