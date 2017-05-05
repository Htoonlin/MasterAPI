/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.dao;

import com.sdm.core.hibernate.dao.DefaultDAO;
import com.sdm.master.entity.NotifyEntity;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *
 * @author Htoonlin
 */
public class NotificationDAO extends DefaultDAO<NotifyEntity>{

    private static final Logger LOG = Logger.getLogger(NotificationDAO.class.getName());
    
    private final String SELECT_BY_USER = "from NotifyEntity n WHERE n.receiverId = :userId";
    
    public NotificationDAO() {
        super(NotifyEntity.class);
    }

    public NotificationDAO(Session session) {
        super(session, NotifyEntity.class);
    }

    public List<NotifyEntity> getNotificationsByUserId(long userId) throws Exception{
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        return super.fetch(SELECT_BY_USER, params);
    }    
    
    public NotifyEntity addNotification(NotifyEntity notify){
        notify.setCreatedAt(new Date());        
        mainSession.save(notify);
        return notify;
    }
}
