package com.sdm.master.dao;

import com.sdm.core.hibernate.dao.RestDAO;
import com.sdm.master.entity.MenuEntity;
import org.hibernate.Session;

public class MenuDAO extends RestDAO {

    public MenuDAO(int userId) {
        super(MenuEntity.class.getName(), userId);
    }

    public MenuDAO(Session session, int userId) {
        super(session, MenuEntity.class.getName(), userId);
    }

}
