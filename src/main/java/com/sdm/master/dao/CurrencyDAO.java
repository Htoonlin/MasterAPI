/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.dao;

import com.sdm.core.database.dao.RestDAO;
import com.sdm.master.entity.CurrencyEntity;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;

/**
 *
 * @author Htoonlin
 */
public class CurrencyDAO extends RestDAO<CurrencyEntity> {

    public CurrencyDAO(HttpSession httpSession) {
        super(CurrencyEntity.class, httpSession);
    }

    public CurrencyDAO(Session session, HttpSession httpSession) {
        super(session, CurrencyEntity.class, httpSession);
    }

}
