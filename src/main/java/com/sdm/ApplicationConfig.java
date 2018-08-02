/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm;

import com.sdm.core.di.IAccessManager;
import com.sdm.core.di.IMailManager;
import com.sdm.core.di.ITemplateManager;
import com.sdm.core.hibernate.entity.DefaultEntity;
import com.sdm.core.resource.SystemResource;
import com.sdm.core.util.VelocityTemplateService;
import com.sdm.core.util.mail.WebMailService;
import com.sdm.master.util.AccessService;
import org.apache.log4j.Logger;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

/**
 *
 * @author Htoonlin
 */
@javax.ws.rs.ApplicationPath("/")
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
        property(ServerProperties.OUTBOUND_CONTENT_LENGTH_BUFFER, 0);
        LOG.info("Successfully loaded jersey properties");

        LOG.info("Loading jersey features and providers");
        register(SystemResource.class);
        register(MultiPartFeature.class);
        register(LoggingFeature.class);
        register(JacksonFeature.class);
        LOG.info("Successfully loaded jersey features and providers");

        register(new AbstractBinder() {

            @Override
            protected void configure() {
                LOG.info("Loading Dependency Injections....");

                // Inject DefaultEntity
                bind(DefaultEntity.class).to(DefaultEntity.class);

                // Inject AccessManager
                bindAsContract(AccessService.class).to(IAccessManager.class);

                // Inject TemplateManager
                bindAsContract(VelocityTemplateService.class).to(ITemplateManager.class);

                // Inject MailManager
                bindAsContract(WebMailService.class).to(IMailManager.class);

                LOG.info("Successfully loaded Dependency Injections....");
            }
        });
    }
}
