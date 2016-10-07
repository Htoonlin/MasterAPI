/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 *
 * @author Htoonlin
 */
public class TemplateManager {

    private static final Logger LOG = Logger.getLogger(TemplateManager.class.getName());
    private static TemplateManager instance;

    private VelocityEngine engine;

    public static String mergeTemplate(String fileName, Map<String, Object> data) throws Exception {
        VelocityContext context = new VelocityContext(data);
        return mergeTemplate(fileName, context);
    }

    public static String mergeTemplate(String fileName, VelocityContext context) throws Exception {
        if (instance == null) {
            instance = new TemplateManager();
        }
        instance.engine.init();
        Template template = instance.engine.getTemplate(fileName);
        try (StringWriter writer = new StringWriter()) {
            template.merge(context, writer);
            return writer.toString();
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }

    public TemplateManager() {
        try {
            Properties properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream("velocity.properties"));
            engine = new VelocityEngine(properties);
        } catch (Exception e) {
            LOG.error(e);
        }
    }

}
