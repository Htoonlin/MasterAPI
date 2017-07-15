/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.sdm.core.Constants;
import com.sdm.core.Globalizer;
import com.sdm.core.Setting;
import com.sdm.core.di.IMailManager;
import com.sdm.core.di.ITemplateManager;
import com.sdm.core.exception.InvalidRequestException;
import com.sdm.core.resource.DefaultResource;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.model.MessageModel;
import com.sdm.core.util.SecurityManager;
import com.sdm.master.dao.TokenDAO;
import com.sdm.master.dao.UserDAO;
import com.sdm.master.entity.TokenEntity;
import com.sdm.master.entity.UserEntity;
import com.sdm.master.request.ActivateRequest;
import com.sdm.master.request.AuthRequest;
import com.sdm.master.request.ChangePasswordRequest;
import com.sdm.master.request.RegistrationRequest;
import com.sdm.master.util.AuthMailSend;

import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * REST Web Service
 *
 * @author Htoonlin
 */
@Path("auth")
public class AuthResource extends DefaultResource {

	private static final Logger LOG = Logger.getLogger(AuthResource.class.getName());

	@Inject
	private ITemplateManager templateManager;

	@Inject
	private IMailManager mailManager;

	private UserDAO userDao;

	private int getFailed() {
		try {
			return (int) getHttpSession().getAttribute(Constants.SessionKey.FAILED_COUNT);
		} catch (Exception e) {
			return 0;
		}
	}

	private int blockTime() {
		int seconds = getHttpSession().getMaxInactiveInterval() * 2;
		return (seconds / 60);
	}

	@PostConstruct
	public void init() {
		userDao = new UserDAO(getUserId());
	}

	@HeaderParam("user-agent")
	private String userAgentString;

	private String getDeviceOS() {
		UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
		return userAgent.getOperatingSystem().getName();
	}

	private String generateJWT(TokenEntity currentToken) {
		String jwtKey = Setting.getInstance().get(Setting.JWT_KEY, SecurityManager.generateJWTKey());
		String compactJWT = Jwts.builder().setSubject(Constants.USER_PREFIX + currentToken.getUserId())
				.setIssuer(userAgentString).setIssuedAt(new Date()).setExpiration(currentToken.getTokenExpired())
				.setId(currentToken.getToken()).claim("device_id", currentToken.getDeviceId())
				.claim("device_os", currentToken.getDeviceOs()).compressWith(CompressionCodecs.DEFLATE)
				.signWith(SignatureAlgorithm.HS512, jwtKey).compact();

		getHttpSession().setAttribute(Constants.SessionKey.USER_TOKEN, compactJWT);
		return compactJWT;
	}

	private IBaseResponse authProcess(AuthRequest request, boolean cleanToken) throws Exception {
		try {
			MessageModel message = new MessageModel(401, "Invalid!",
					"Opp! Request email or password is something wrong");
			int limit = Setting.getInstance().getInt(Setting.AUTH_FAILED_COUNT, "3");
			if (getFailed() >= limit) {
				message = new MessageModel(401, "Blocked!",
						"Sorry! Server blocked you. You need to wait " + blockTime() + " minutes.");
			} else {
				UserEntity authUser = userDao.userAuth(request.getEmail(), request.getCryptPassword());
				if (authUser != null && authUser.getStatus() == UserEntity.ACTIVE && request.isAuth(authUser)) {
					userDao.beginTransaction();
					TokenDAO tokenDAO = new TokenDAO(userDao.getSession(), getUserId());
					if (cleanToken) {
						tokenDAO.cleanToken(authUser.getId());
					}
					TokenEntity authToken = tokenDAO.generateToken(authUser.getId(), request.getDeviceId(),
							this.getDeviceOS());
					String token = this.generateJWT(authToken);
					authUser.setCurrentToken(token);
					userDao.commitTransaction();

					// Reset failed count
					getHttpSession().setAttribute(Constants.SessionKey.FAILED_COUNT, 0);

					return new DefaultResponse<UserEntity>(authUser);
				}
			}
			// Increase failed count
			getHttpSession().setAttribute(Constants.SessionKey.FAILED_COUNT, getFailed() + 1);
			return new DefaultResponse<>(message);
		} catch (Exception e) {
			userDao.rollbackTransaction();
			LOG.error(e);
			throw e;
		}
	}

	@POST
	@Path("clean")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse cleanToken(AuthRequest request) throws Exception {
		return this.authProcess(request, true);
	}

	@PermitAll
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse authWithEmail(AuthRequest request, @HeaderParam("user-agent") String userAgent)
			throws Exception {
		return this.authProcess(request, false);
	}

	@PermitAll
	@POST
	@Path("register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse userRegistration(RegistrationRequest request) throws Exception {
		try {
			Map<String, String> errors = new HashMap<>();

			if (!mailManager.checkMail(request.getEmail())) {
				errors.put("email", "Requested email is not valid");
				throw new InvalidRequestException(errors);
			}

			UserEntity user = userDao.getUserByEmail(request.getEmail());
			if (user != null && user.getEmail().equalsIgnoreCase(request.getEmail())) {
				errors.put("email", "Sorry! someone already registered with this email");
				throw new InvalidRequestException(errors);
			}
			String password = SecurityManager.hashString(request.getEmail(), request.getPassword());
			user = new UserEntity(request.getEmail(), request.getDisplayName(), password, true, request.getCountry(),
					UserEntity.PENDING);
			AuthMailSend mailSend = new AuthMailSend(mailManager, templateManager);
			mailSend.activateLink(user, userAgentString);
			userDao.insert(user, true);
			MessageModel message = new MessageModel(200, "Registration Success",
					"Thank you for your registration. Pls check your mail for activation.");
			return new DefaultResponse<>(message);
		} catch (Exception e) {
			LOG.error(e);
			throw e;
		}
	}

	@PermitAll
	@GET
	@Path("activate")
	@Produces(MediaType.TEXT_HTML)
	public Response linkActivation(@DefaultValue("") @QueryParam("token") String request) throws Exception {

		Map<String, Object> data = new HashMap<>();
		data.put("current_year", Globalizer.getDateString("yyyy", new Date()));
		try {
			request = SecurityManager.base64Decode(request);
			ActivateRequest activateRequest = Globalizer.jsonMapper().readValue(request, ActivateRequest.class);
			activateRequest.setTimestamp((new Date()).getTime());
			IBaseResponse response = this.otpActivation(activateRequest);
			if (response instanceof MessageModel) {
				data.put("title", response.getStatus());
				data.put("message", "<p class=\"text-warning\">" + response.getContent() + "</p>");
			} else if (response.getContent() instanceof UserEntity) {
				data.put("title", "Activation Success");
				data.put("message", "<p>Your account is ready. Thank you for your registration.</p>");
			} else {
				data.put("title", "Activation Failed");
				data.put("message",
						"<p class=\"text-warning\">Sorry! Your activation token is <strong>Invalid.</strong></p>");
			}
		} catch (Exception e) {
			LOG.error(e);
			data.put("title", "SERVER ERROR");
			data.put("message", "<p class=\"text-danger\">" + e.getLocalizedMessage() + "</p>");
		}

		String output = templateManager.buildTemplate("auth-message.jsp", data);
		return Response.ok(output).build();
	}

	@PermitAll
	@Path("activate")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse otpActivation(ActivateRequest request) throws Exception {
		try {
			UserEntity user = userDao.checkToken(request.getEmail(), request.getToken());
			if (user == null) {
				MessageModel message = new MessageModel(204, "No Data", "There is no data for your request.");
				return new DefaultResponse<>(message);
			}

			if (user.getOtpExpired().before(new Date())) {
				AuthMailSend mailSend = new AuthMailSend(mailManager, templateManager);
				mailSend.activateLink(user, userAgentString);
				userDao.update(user, true);
				MessageModel message = new MessageModel(400, "Token Expired",
						"Sorry! Your token has expired. We send new token to your email.");
				return new DefaultResponse<>(message);
			}

			userDao.beginTransaction();
			user.setOtpToken(null);
			user.setOtpExpired(null);
			user.setStatus(UserEntity.ACTIVE);
			userDao.update(user, false);
			TokenDAO tokenDAO = new TokenDAO(userDao.getSession(), getUserId());
			TokenEntity authToken = tokenDAO.generateToken(user.getId(), request.getDeviceId(), this.getDeviceOS());
			String token = generateJWT(authToken);
			user.setCurrentToken(token);
			userDao.commitTransaction();
			return new DefaultResponse<UserEntity>(user);
		} catch (Exception e) {
			userDao.rollbackTransaction();
			LOG.error(e);
			throw e;
		}
	}

	@PermitAll
	@Path("resetPassword")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IBaseResponse resetPassword(ChangePasswordRequest request) throws Exception {
		try {
			MessageModel message = new MessageModel(400, "Invalid!", "Sorry! Requested token is invalid or expired.");
			String token = request.getToken();
			UserEntity user = userDao.userAuth(request.getEmail(), request.getOldPassword());

			if (user == null) {
				message = new MessageModel(204, "No Data", "There is no data for your request.");
				return new DefaultResponse<>(message);
			}

			if (!user.getOtpToken().equals(token) || user.getOtpExpired().before(new Date())) {
				AuthMailSend mailSend = new AuthMailSend(mailManager, templateManager);
				mailSend.forgetPasswordLink(user);
				userDao.update(user, true);
				message = new MessageModel(400, "Token Expired",
						"Sorry! Your token has expired. We send new link to your email.");
			} else if (user.getEmail().equalsIgnoreCase(request.getEmail())
					&& user.getPassword().equals(request.getOldPassword()) && user.getOtpToken().equals(token)) {
				String newPassword = SecurityManager.hashString(request.getEmail(), request.getNewPassword());
				user.setOtpExpired(null);
				user.setOtpToken(null);
				user.setPassword(newPassword);
				userDao.update(user, true);
				message = new MessageModel(202, "OK", "We updated the new password on your request successfully.");
			}

			return new DefaultResponse<>(message);
		} catch (Exception e) {
			LOG.error(e);
			throw e;
		}
	}

	@PermitAll
	@Path("forgetPassword/{email}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public IBaseResponse forgetPassword(@PathParam("email") String email) throws Exception {
		MessageModel message;
		try {
			if (!mailManager.checkMail(email)) {
				message = new MessageModel(400, "Invalid!", "Invalid email address.");
			} else {
				UserEntity user = userDao.getUserByEmail(email);
				if (user == null) {
					message = new MessageModel(400, "Invalid!", "Invalid email address");
				} else {

					AuthMailSend mailSend = new AuthMailSend(mailManager, templateManager);
					mailSend.forgetPasswordLink(user);
					userDao.update(user, true);
					message = new MessageModel(201, "Send Mail", "We send the reset password link to your e-mail.");
				}
			}
			return new DefaultResponse<>(message);
		} catch (Exception e) {
			LOG.error(e);
			throw e;
		}
	}
}
