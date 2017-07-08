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
public class HibernateConnector {

	private static final Logger LOG = Logger.getLogger(HibernateConnector.class.getName());
	private static HibernateConnector instance;
	private SessionFactory mainFactory;

	private HibernateConnector() {
		this.setup();
	}

	private void setup() {
		LOG.info("Creating new hibernate instance....");
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		try {
			mainFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			LOG.error(e);
			StandardServiceRegistryBuilder.destroy(registry);
			instance = null;
			throw e;
		}
	}

	public static synchronized SessionFactory getFactory() throws HibernateException {
		if (instance == null) {
			instance = new HibernateConnector();
		}
		return instance.mainFactory;
	}

	public static void shutdown() {		
		if (instance != null) {
			LOG.info("Shutting down hibernate session factory");
			instance.mainFactory.close();
		}
	}
}
