/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.di;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.glassfish.hk2.api.Factory;

/**
 *
 * @author Htoonlin
 */
public class HttpSessionFactory implements Factory<HttpSession> {

	private final HttpServletRequest request;

	@Inject
	public HttpSessionFactory(Provider<HttpServletRequest> requestProvider) {
		this.request = requestProvider.get();
	}

	@Override
	public HttpSession provide() {
		return request.getSession();
	}

	@Override
	public void dispose(HttpSession instance) {

	}

}
