/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.util;

import com.sdm.core.Setting;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.ws.rs.core.Context;

/**
 *
 * @author Htoonlin
 */
public class JSPTemplateManager implements ITemplateManager {

    private class TemplateWriter extends HttpServletResponseWrapper {

        private final CharArrayWriter charArray = new CharArrayWriter();

        public TemplateWriter(HttpServletResponse response) {
            super(response);
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return new PrintWriter(charArray);
        }

        public String getContent() {
            return charArray.toString();
        }
    }
    private static final Logger LOG = Logger.getLogger(JSPTemplateManager.class.getName());

    @Context
    private HttpServletResponse response;

    @Context
    private HttpServletRequest request;

    @Override
    public String buildTemplate(String template, Map<String, Object> data) {
        try {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                request.setAttribute(entry.getKey(), entry.getValue());
            }
            TemplateWriter writer = new TemplateWriter(response);
            template = Setting.getInstance().TEMPLATE_PATH + template;
            request.getRequestDispatcher(template).forward(request, writer);
            return writer.getContent();
        } catch (ServletException | IOException ex) {
            LOG.error(ex);
        }

        return "";
    }

}