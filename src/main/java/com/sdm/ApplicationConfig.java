/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Application;

import org.apache.log4j.Logger;
import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.internal.inject.Injections;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ServerProperties;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.sdm.core.Setting;
import com.sdm.core.di.HttpSessionFactory;
import com.sdm.core.di.IAccessManager;
import com.sdm.core.di.IMailManager;
import com.sdm.core.di.ITemplateManager;
import com.sdm.core.filter.JacksonObjectMapper;
import com.sdm.core.util.JSPTemplateManager;
import com.sdm.core.util.mail.MailgunService;
import com.sdm.core.util.mail.WebMailService;
import com.sdm.master.util.AccessManager;

/**
 *
 * @author Htoonlin
 */
@javax.ws.rs.ApplicationPath("/api")
public class ApplicationConfig extends Application {

    private static final Logger LOG = Logger.getLogger(ApplicationConfig.class.getName());

    @Inject
    public ApplicationConfig(ServiceLocator serviceLocator) {
    		LOG.info("Loading Dependency Injections....");
        DynamicConfiguration dc = Injections.getConfiguration(serviceLocator);
        //Inject HttpSession
        Injections.addBinding(Injections.newFactoryBinder(HttpSessionFactory.class)
                .to(HttpSession.class).proxy(true).proxyForSameScope(false)
                .in(RequestScoped.class), dc);

        //Inject AccessManager
        Injections.addBinding(Injections.newBinder(AccessManager.class).to(IAccessManager.class), dc);

        //Inject TemplateManager
        Injections.addBinding(Injections.newBinder(JSPTemplateManager.class).to(ITemplateManager.class), dc);
        
        //Inject MailManager
        if(Setting.getInstance().MAIL_SERVICE.equalsIgnoreCase("mailgun")){
            Injections.addBinding(Injections.newBinder(MailgunService.class).to(IMailManager.class), dc);    
        }else{
            Injections.addBinding(Injections.newBinder(WebMailService.class).to(IMailManager.class), dc);    
        }

        dc.commit();
        LOG.info("Successfully loaded Dependency Injections....");
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        resources.add(MultiPartFeature.class);
        resources.add(JacksonJaxbJsonProvider.class);
        resources.add(JacksonObjectMapper.class);
        resources.add(LoggingFeature.class);
        addRestResourceClasses(resources);
        return resources;
    }

    @Override
    public Set<Object> getSingletons() {
        return Collections.EMPTY_SET;
    }

    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, false);
        properties.put(ServerProperties.BV_FEATURE_DISABLE, true);
        properties.put(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        properties.put(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);
        properties.put(ServerProperties.PROCESSING_RESPONSE_ERRORS_ENABLED, true);
        return properties;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.sdm.core.exception.ClassNotFoundExceptionMapper.class);
        resources.add(com.sdm.core.exception.GenericExceptionMapper.class);
        resources.add(com.sdm.core.exception.IllegalStateExceptionMapper.class);
        resources.add(com.sdm.core.exception.SQLExceptionMapper.class);
        resources.add(com.sdm.core.exception.UnrecognizedPropertyExceptionMapper.class);
        resources.add(com.sdm.core.exception.WebApplicationExceptionMapper.class);
        resources.add(com.sdm.core.filter.AuthenticaionFilter.class);
        resources.add(com.sdm.core.filter.CORSFilter.class);
        resources.add(com.sdm.core.filter.ResponseFilter.class);
        resources.add(com.sdm.master.resource.AuthResource.class);
        resources.add(com.sdm.master.resource.FileResource.class);
        resources.add(com.sdm.master.resource.GeneralResource.class);
        resources.add(com.sdm.master.resource.PermissionResource.class);
        resources.add(com.sdm.master.resource.ProfileResource.class);
        resources.add(com.sdm.master.resource.RoleResource.class);
        resources.add(com.sdm.master.resource.UserResource.class);
        resources.add(com.sdm.sample.resource.CustomerResource.class);
    }
}
