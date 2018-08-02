/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util;

import com.sdm.core.Setting;
import com.sdm.core.di.ITemplateManager;
import java.io.StringWriter;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

/**
 *
 * @author htoonlin
 */
public class VelocityTemplateService implements ITemplateManager {

    private static final Logger LOG = Logger.getLogger(VelocityTemplateService.class.getName());

    private final VelocityEngine engine;
    
    public VelocityTemplateService() {
        this.engine = new VelocityEngine();
        String templatePath = Setting.getInstance().get(Setting.TEMPLATE_DIRECTORY, "./template/");
        this.engine.addProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, templatePath);
        this.engine.init();
    }
    
    
    @Override
    public String buildTemplate(String template, Map<String, Object> data) {
        VelocityContext context = new VelocityContext(data);
        Template vm = engine.getTemplate(template);
        StringWriter writer = new StringWriter();
        vm.merge(context, writer);
        
        return writer.toString();
    }
    
}
