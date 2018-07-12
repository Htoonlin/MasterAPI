/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 *
 * @author Htoonlin
 */
public final class HibernateConnector {

    private static final Logger LOG = Logger.getLogger(HibernateConnector.class.getName());
    private static int instance_count = 0;
    private static SessionFactory mainFactory;

    public static synchronized void init() {
        LOG.info("Creating new hibernate instance....");
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            mainFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            instance_count++;
            LOG.info("Current Hibernate Instance Count : " + instance_count);
        } catch (Exception e) {
            LOG.error(e);
            StandardServiceRegistryBuilder.destroy(registry);
            throw e;
        }
    }

    public static synchronized SessionFactory getFactory() throws HibernateException {
        if (mainFactory.isClosed()) {
            init();
        }
        return mainFactory;
    }

    public static synchronized void shutdown() {
        if (mainFactory != null && mainFactory.isOpen()) {
            LOG.info("Shutting down hibernate session factory");
            instance_count--;
            mainFactory.close();
            LOG.info("Current Hibernate Instance Count : " + instance_count);
        }
    }
}
