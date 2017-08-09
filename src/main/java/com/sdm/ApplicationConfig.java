/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import com.sdm.core.Setting;
import com.sdm.core.di.HttpSessionFactory;
import com.sdm.core.di.IAccessManager;
import com.sdm.core.di.IMailManager;
import com.sdm.core.di.ITemplateManager;
import com.sdm.core.hibernate.entity.DefaultEntity;
import com.sdm.core.resource.SystemResource;
import com.sdm.core.util.JSPTemplateService;
import com.sdm.core.util.mail.MailgunService;
import com.sdm.core.util.mail.WebMailService;
import com.sdm.generate.resource.GenerateResource;
import com.sdm.master.util.AccessService;

/**
 *
 * @author Htoonlin
 */
@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends ResourceConfig {

	private static final Logger LOG = Logger.getLogger(ApplicationConfig.class.getName());

	public ApplicationConfig() {
		LOG.info("Loading packages");
		packages("com.sdm.core.exception", "com.sdm.core.filter", "com.sdm.master.resource",
				"com.sdm.facebook.resource", "com.sdm.sample.resource");
		LOG.info("Successfully loaded packages.");

		LOG.info("Loading jersey properties");
		property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
		property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);
		LOG.info("Successfully loaded jersey properties");

		LOG.info("Loading jersey features and providers");
		register(SystemResource.class);
		register(GenerateResource.class);
		register(MultiPartFeature.class);
		register(LoggingFeature.class);
		register(JacksonFeature.class);
		LOG.info("Successfully loaded jersey features and providers");

		register(new AbstractBinder() {

			@Override
			protected void configure() {
				LOG.info("Loading Dependency Injections....");
				// Inject HttpSession
				bindFactory(HttpSessionFactory.class).to(HttpSession.class).proxy(true).proxyForSameScope(false)
						.in(RequestScoped.class);
				
				//Inject DefaultEntity
				bind(DefaultEntity.class).to(DefaultEntity.class);

				// Inject AccessManager
				bindAsContract(AccessService.class).to(IAccessManager.class);

				// Inject TemplateManager
				bindAsContract(JSPTemplateService.class).to(ITemplateManager.class);

				// Inject MailManager
				String mailType = Setting.getInstance().get(Setting.MAIL_TYPE, "webmail");
				if (mailType.equalsIgnoreCase("mailgun")) {
					bindAsContract(MailgunService.class).to(IMailManager.class);
				} else {
					bindAsContract(WebMailService.class).to(IMailManager.class);
				}

				LOG.info("Successfully loaded Dependency Injections....");
			}
		});
	}
}
