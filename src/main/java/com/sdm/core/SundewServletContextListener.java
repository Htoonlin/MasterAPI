/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core;

import com.sdm.core.hibernate.HibernateConnector;
import org.apache.log4j.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author Htoonlin
 */
@WebListener
public class SundewServletContextListener implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(SundewServletContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOG.info("System is starting ....");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOG.info("System is stopping ....");
        //HibernateConnector.shutdown();
        LOG.info("Good bye!");
    }

}