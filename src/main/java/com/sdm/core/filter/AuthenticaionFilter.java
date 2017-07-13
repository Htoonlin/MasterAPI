/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.filter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.List;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;

import com.sdm.core.Constants;
import com.sdm.core.Setting;
import com.sdm.core.di.IAccessManager;
import com.sdm.core.response.model.MessageModel;
import com.sdm.core.util.SecurityManager;

import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 *
 * @author Htoonlin
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticaionFilter implements ContainerRequestFilter {

	private static final Logger LOG = Logger.getLogger(AuthenticaionFilter.class);

	@Context
	private ResourceInfo resourceInfo;

	@Inject
	private HttpSession httpSession;

	@Inject
	private IAccessManager accessManager;

	private int getFailed() {
		try {
			return (int) httpSession.getAttribute(Constants.SessionKey.FAILED_COUNT);
		} catch (Exception e) {
			LOG.error(e);
			return 0;
		}
	}

	private int blockTime() {
		int seconds = httpSession.getMaxInactiveInterval() * 2;
		return (seconds / 60);
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		Method method = resourceInfo.getResourceMethod();
		if (!method.isAnnotationPresent(PermitAll.class)) {
			int limit = Setting.getInstance().getInt(Setting.AUTH_FAILED_COUNT, "3");
			if (this.getFailed() >= limit) {
				requestContext.abortWith(buildResponse(403,
						"Sorry! Server blocked your request. You need to wait " + blockTime() + " minutes."));
				return;
			}

			if (method.isAnnotationPresent(DenyAll.class)) {
				requestContext.abortWith(errorResponse(403));
				return;
			}

			final MultivaluedMap<String, String> headers = requestContext.getHeaders();
			final List<String> authorization = headers.get(HttpHeaders.AUTHORIZATION);
			final List<String> userAgents = headers.get(HttpHeaders.USER_AGENT);

			if (authorization == null || authorization.isEmpty() || userAgents == null || userAgents.isEmpty()) {
				requestContext.abortWith(errorResponse(401));
				return;
			}

			if (accessManager == null) {
				requestContext.abortWith(buildResponse(500, "AccessManager Interface is null or invalid."));
				return;
			}

			try {
				// Clean Token
				String settingKey = Setting.getInstance().get(Setting.JWT_KEY, SecurityManager.generateJWTKey());
				String tokenString = authorization.get(0).substring(Constants.AUTH_TYPE.length()).trim();
				byte[] jwtKey = Base64.getDecoder().decode(settingKey);

				try {
					Claims authorizeToken = Jwts.parser().setSigningKey(jwtKey).requireIssuer(userAgents.get(0))
							.parseClaimsJws(tokenString).getBody();

					if (!accessManager.validateToken(authorizeToken)) {
						requestContext.abortWith(errorResponse(403));
						return;
					}

					if (!accessManager.checkPermission(authorizeToken, method, requestContext.getMethod())) {
						requestContext.abortWith(errorResponse(403));
						return;
					}

					// Separate UserID and Save
					int userId = Integer
							.parseInt(authorizeToken.getSubject().substring(Constants.USER_PREFIX.length()).trim());
					this.saveUserId(userId);
				} catch (ClaimJwtException ex) {
					requestContext.abortWith(buildResponse(403, ex.getLocalizedMessage()));
				}

			} catch (UnsupportedJwtException | MalformedJwtException | SignatureException
					| IllegalArgumentException e) {
				LOG.error(e);
				requestContext.abortWith(buildResponse(500, e.getLocalizedMessage()));
			}
		}
	}

	private Response errorResponse(int code) {
		String description = "";
		this.httpSession.setAttribute(Constants.SessionKey.FAILED_COUNT, getFailed() + 1);
		switch (code) {
		case 401:
			description = "Hmmm! Your authorization is failed. If you are trying to hack server, don't do it again.";
			break;
		case 403:
			description = "Oppp! Something wrong or user doesn't have access to the resource.";
			break;
		}
		return buildResponse(code, description);
	}

	private Response buildResponse(int code, String description) {
		MessageModel message = new MessageModel(code, HttpStatus.getStatusText(code), description);
		return Response.status(code).entity(message).build();
	}

	private void saveUserId(int userId) {
		this.httpSession.setAttribute(Constants.SessionKey.FAILED_COUNT, 0);
		this.httpSession.setAttribute(Constants.SessionKey.USER_ID, userId);
	}
}
