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

import org.apache.log4j.Logger;

import com.sdm.core.Globalizer;
import com.sdm.core.Setting;
import com.sdm.core.di.IAccessManager;
import com.sdm.core.hibernate.audit.AuditStorage;
import com.sdm.core.response.MessageResponse;
import com.sdm.core.response.ResponseType;

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
	public final String FAILED_COUNT = "AUTHORIZATION_FAILED_COUNT";

	@Context
	private ResourceInfo resourceInfo;

	@Inject
	private HttpSession httpSession;

	@Inject
	private IAccessManager accessManager;

	private int getFailed() {
		try {
			return (int) httpSession.getAttribute(FAILED_COUNT);
		} catch (Exception e) {
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
			if (getFailed() >= Setting.getInstance().AUTH_FAILED_COUNT) {
				requestContext.abortWith(buildResponse(403,
						"Sorry! Server blocked your request. You need to wait " + blockTime() + " minutes."));
				return;
			}

			if (method.isAnnotationPresent(DenyAll.class)) {
				requestContext.abortWith(buildResponse(403));
				return;
			}

			final MultivaluedMap<String, String> headers = requestContext.getHeaders();
			final List<String> authorization = headers.get(HttpHeaders.AUTHORIZATION);
			final List<String> userAgents = headers.get(HttpHeaders.USER_AGENT);

			if (authorization == null || authorization.isEmpty() || userAgents == null || userAgents.isEmpty()) {
				requestContext.abortWith(buildResponse(401));
				return;
			}

			if (accessManager == null) {
				requestContext.abortWith(buildResponse(500, "AccessManager Interface is null or invalid."));
				return;
			}

			try {
				// Clean Token
				String tokenString = authorization.get(0).substring(Globalizer.AUTH_TYPE.length()).trim();
				byte[] jwtKey = Base64.getDecoder().decode(Setting.getInstance().JWT_KEY);

				try {
					Claims authorizeToken = Jwts.parser().setSigningKey(jwtKey).requireIssuer(userAgents.get(0))
							.parseClaimsJws(tokenString).getBody();

					if (!accessManager.validateToken(authorizeToken)) {
						requestContext.abortWith(buildResponse(403));
						return;
					}

					if (!accessManager.checkPermission(authorizeToken, method, requestContext.getMethod())) {
						requestContext.abortWith(buildResponse(403));
						return;
					}

					AuditStorage.getInstance().set(authorizeToken.getId());

					// Separate UserID and Save
					long userId = Long.parseLong(
							authorizeToken.getSubject().substring(Globalizer.AUTH_SUBJECT_PREFIX.length()).trim());
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

	private Response buildResponse(int code) {
		String description = "";
		httpSession.setAttribute(FAILED_COUNT, getFailed() + 1);
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
		MessageResponse message = new MessageResponse(code, ResponseType.ERROR, description);
		return Response.status(code).entity(message).build();
	}

	private void saveUserId(long userId) {
		this.httpSession.setAttribute(Globalizer.SESSION_USER_ID, userId);
	}
}
