/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.resource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.model.Invocable;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.ResourceMethod;

import com.sdm.Constants;
import com.sdm.core.Setting;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.core.response.model.ListModel;
import com.sdm.core.response.model.MessageModel;
import com.sdm.core.response.model.RouteInfo;

/**
 *
 * @author Htoonlin
 */
public class DefaultResource implements IBaseResource {

	private static final Logger LOG = Logger.getLogger(DefaultResource.class.getName());

	@Context
	private UriInfo uriInfo;

	@Inject
	private HttpSession httpSession;

	@Context
	private Request request;

	private static String eTag = UUID.randomUUID().toString();
	private static Date lastModified = new Date();

	protected void modifiedResource() {
		eTag = UUID.randomUUID().toString();
		lastModified = new Date();
	}

	protected DefaultResponse<MessageModel> validateCache(){
		return this.validateCache(Setting.getInstance().getInt(Setting.CC_MAX_AGE));
	}
	
	protected DefaultResponse<MessageModel> validateCache(int cacheAge) {
		ResponseBuilder builder = request.evaluatePreconditions(lastModified);
		if (builder != null) {
			MessageModel message = new MessageModel(HttpStatus.SC_NOT_MODIFIED, "NO_CHANGE",
					"There is no any changes for your request.");
			DefaultResponse<MessageModel> response = new DefaultResponse<>(HttpStatus.SC_NOT_MODIFIED,
					ResponseType.INFO, message);
			response.setHeaders(this.buildCache(cacheAge));
			return response;
		}
		return null;
	}

	protected Map<String, Object> buildCache() {
		return this.buildCache(Setting.getInstance().getInt(Setting.CC_MAX_AGE));
	}

	protected Map<String, Object> buildCache(int cacheAge) {
		// Build CacheControl
		CacheControl cc = new CacheControl();
		cc.setMaxAge(cacheAge);
		cc.setNoStore(false);
		cc.setPrivate(true);
		cc.setNoTransform(false);

		// Build Expire
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.SECOND, cacheAge);

		Map<String, Object> cacheHeader = new HashMap<>();
		cacheHeader.put(HttpHeaders.CACHE_CONTROL, cc);
		cacheHeader.put(HttpHeaders.EXPIRES, cal.getTime());
		cacheHeader.put(HttpHeaders.ETAG, new EntityTag(eTag));
		cacheHeader.put(HttpHeaders.LAST_MODIFIED, lastModified);
		return cacheHeader;
	}

	@Override
	public HttpSession getHttpSession() {
		return this.httpSession;
	}

	@Override
	public int getUserId() {
		try {
			return (int) this.getHttpSession().getAttribute(Constants.SessionKey.USER_ID);
		} catch (Exception e) {
			LOG.error("There is no session. <" + e.getLocalizedMessage() + ">");
			return 0;
		}
	}

	@Override
	public UriInfo getUriInfo() {
		return uriInfo;
	}

	@Override
	public void setUriInfo(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}

	public String getBaseURI() {
		String[] baseURI = getUriInfo().getAbsolutePath().toString().split("/api/", 2);
		if (baseURI.length > 1) {
			return baseURI[0] + "/";
		}
		return getUriInfo().getAbsolutePath().toString();
	}

	protected List<RouteInfo> collectRoute(Resource resource, String basePath) {
		List<RouteInfo> routeList = new ArrayList<>();
		String parentPath = "";
		for (ResourceMethod method : resource.getResourceMethods()) {
			Invocable invocable = method.getInvocable();

			// Skip Routes
			if (invocable.getHandlingMethod().getName().equalsIgnoreCase("getRoutes")) {
				continue;
			}

			RouteInfo route = new RouteInfo();
			// Set Resource Class
			route.setResourceClass(this.getClass().getName());

			// Set Resource Method
			route.setResourceMethod(invocable.getHandlingMethod().getName());

			// Set Path
			if (!basePath.endsWith("/")) {
				basePath += "/";
			}
			parentPath = (basePath + resource.getPath()).replaceAll("//", "/");
			route.setPath(parentPath);

			// Set HTTP Method
			route.setMethod(method.getHttpMethod());

			routeList.add(route);
		}

		for (Resource childResource : resource.getChildResources()) {
			routeList.addAll(collectRoute(childResource, parentPath));
		}

		return routeList;
	}

	@Override
	public IBaseResponse getRoutes() {
		try {
			Resource resource = Resource.from(this.getClass());
			if (resource == null) {
				MessageModel message = new MessageModel(204, "No Data", "There is no data for your request.");
				return new DefaultResponse<>(message);
			}
			List<RouteInfo> routeList = collectRoute(resource, "/");
			ListModel<RouteInfo> content = new ListModel<>(routeList);
			return new DefaultResponse<>(content);
		} catch (Exception e) {
			LOG.error(e);
			throw e;
		}
	}
}
