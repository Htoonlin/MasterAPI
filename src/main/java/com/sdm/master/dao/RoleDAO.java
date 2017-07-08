/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.dao;

import org.hibernate.Session;

import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.master.entity.RoleEntity;

/**
 *
 * @author htoonlin
 */
public class RoleDAO extends RestDAO {

	public RoleDAO(long userId) {
		super(RoleEntity.class.getName(), userId);
	}

	public RoleDAO(Session session, long userId) {
		super(session, RoleEntity.class.getName(), userId);
	}

}
