/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.hibernate.dao;

import com.sdm.core.Globalizer;
import com.sdm.core.hibernate.HibernateConnector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;

/**
 *
 * @author htoonlin
 */
public class AuditDAO {

    private final AuditReader reader;

    public AuditDAO(AuditReader reader) {
        this.reader = reader;
    }

    public AuditDAO() {
        this(HibernateConnector.getFactory().openSession());
    }

    public AuditDAO(Session session) {
        this(AuditReaderFactory.get(session));
    }

    private HashMap<String, Object> convertMap(Object[] data) {
        HashMap<String, Object> response = new HashMap<>();
        for (Object value : data) {
            String key = Globalizer.camelToLowerUnderScore(value.getClass().getSimpleName());
            response.put(key.replaceAll("_entity.*", ""), value);
        }
        return response;
    }

    private AuditQuery getQueryById(Class entityClass, Object id) {
        return reader.createQuery()
                .forRevisionsOfEntity(entityClass, false, true)
                .addOrder(AuditEntity.revisionNumber().desc())
                .add(AuditEntity.id().eq(id));
    }

    public HashMap<String, Object> getDataByVersion(Class entityClass, Object id, long version) {
        Object data = this.getQueryById(entityClass, id)
                .add(AuditEntity.revisionNumber().eq(version))
                .getSingleResult();

        return this.convertMap(((Object[]) data));
    }

    public long getTotal(Class entityClass, Object id) {
        Object data = this.getQueryById(entityClass, id)
                .addProjection(AuditEntity.revisionNumber().count())
                .getSingleResult();
        return (long) data;
    }

    public List<?> getVersions(Class entityClass, Object id, int pageId, int pageSize) {
        // Calculate Start Index
        if (pageId <= 0) {
            pageId = 1;
        }
        int start = pageSize * (pageId - 1);

        List data = this.getQueryById(entityClass, id)
                .setFirstResult(start)
                .setMaxResults(pageSize)
                .getResultList();

        List<Map<String, Object>> responseList = new ArrayList<>();
        for (Object result : data) {
            responseList.add(this.convertMap((Object[]) result));
        }

        return responseList;
    }
}
