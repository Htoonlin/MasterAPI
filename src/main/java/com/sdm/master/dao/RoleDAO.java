/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.dao;

import com.sdm.core.hibernate.dao.RestDAO;
import org.hibernate.Session;

/**
 *
 * @author htoonlin
 */
public class RoleDAO extends RestDAO {

    private static final String ENTITY = "RoleEntity";

    public RoleDAO(long userId) {
        super(ENTITY, userId);
    }

    public RoleDAO(Session session, long userId) {
        super(session, ENTITY, userId);
    }

}
