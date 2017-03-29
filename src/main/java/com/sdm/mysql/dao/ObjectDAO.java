/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.mysql.dao;

import com.sdm.mysql.request.object.CreateRequest;
import com.sdm.mysql.model.PropertyModel;
import com.sdm.mysql.util.MySQLManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 *
 * @author Htoonlin
 */
public class ObjectDAO extends MySQLDAO {

    private static final Logger LOG = Logger.getLogger(ObjectDAO.class.getName());

    private final String DEFAULT_ATTRS = "ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin";

    public ObjectDAO() throws SQLException, IOException, ClassNotFoundException {
        super();
    }

    public ObjectDAO(Connection connection) {
        super(connection);
    }

    public boolean create(CreateRequest object) throws SQLException {
        String sql = "CREATE" + (object.isTemporary() ? " TEMPORARY TABLE" : " TABLE");
        sql += " IF NOT EXISTS";

        String primary = "";        
        sql += " " + MySQLManager.quoteName(object.getName()) + " (";
        for (PropertyModel property : object.getProperties()) {
            sql += property.defaultSQL() + ",";
            if (property.isPrimary()) {
                primary += MySQLManager.quoteName(property.getName());
            }
        }
        if (primary.length() > 1) {
            primary = MySQLManager.cleanLastChar(primary, ",");
            sql += " PRIMARY KEY (" + primary + ")";
        }
        sql = MySQLManager.cleanLastChar(sql, ",");
        sql += ") " + this.DEFAULT_ATTRS;

        if (object.getDescription() != null && !object.getDescription().isEmpty()) {
            sql += " COMMENT '" + MySQLManager.escapeQuery(object.getDescription()) + "'";
        }

        if (object.getMaxRecords() > 0) {
            sql += " MAX_ROWS=" + object.getMaxRecords();
        }

        try {
            return execute(sql);
        } catch (SQLException ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    public boolean rename(String oldName, String newName) throws SQLException {
        String sql = "RENAME TABLE " + MySQLManager.quoteName(oldName) + " TO " + MySQLManager.quoteName(newName);
        try {
            return execute(sql);
        } catch (SQLException ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    public boolean clone(String source, String dest, boolean temporary, boolean dataCopy) throws SQLException {
        String sql = "CREATE" + (temporary ? " TEMPORARY TABLE" : " TABLE");
        sql += " IF NOT EXISTS";
        sql += " " + MySQLManager.quoteName(dest);
        if (dataCopy) {
            sql += " AS SELECT * FROM  " + MySQLManager.quoteName(source);
        } else {
            sql += " LIKE " + MySQLManager.quoteName(source);
        }
        try {
            return execute(sql);
        } catch (SQLException ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    public boolean remove(String objectName, boolean temp) throws SQLException {
        String sql = "DROP" + (temp ? " TEMPORARY TABLE" : " TABLE");
        sql += " IF EXISTS";
        sql += " " + MySQLManager.quoteName(objectName);
        try {
            return execute(sql);
        } catch (SQLException ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    public boolean addProperty(String objectName, PropertyModel property, String after, boolean isFirst) throws SQLException {
        String sql = "ALTER TABLE " + MySQLManager.quoteName(objectName);
        sql += " ADD COLUMN " + property.defaultSQL();
        if (isFirst) {
            sql += " FIRST";
        } else if (!after.isEmpty()) {
            sql += " AFTER " + MySQLManager.quoteName(after);
        }

        try {
            return execute(sql);
        } catch (SQLException ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    public boolean addProperty(String objectName, PropertyModel property) throws SQLException {
        return this.addProperty(objectName, property, "", false);
    }

    public boolean addPropertyAfter(String objectName, PropertyModel property, String after) throws SQLException {
        return this.addProperty(objectName, property, after, false);
    }

    public boolean addPropertyFirst(String objectName, PropertyModel property) throws SQLException {
        return this.addProperty(objectName, property, "", true);
    }

    public boolean editProperty(String objectName, String oldName, PropertyModel property, String after, boolean isFirst) throws SQLException {
        String sql = "ALTER TABLE " + MySQLManager.quoteName(objectName) + " CHANGE ";
        sql += MySQLManager.quoteName(oldName);
        if (!oldName.equalsIgnoreCase(property.getName())) {
            sql += " " + MySQLManager.quoteName(property.getName());
        } else {
            sql += " " + property.defaultSQL();
        }
        if (isFirst) {
            sql += " FIRST";
        } else if (after != null && !after.isEmpty()) {
            sql += " AFTER " + MySQLManager.quoteName(after);
        }
        try {
            return execute(sql);
        } catch (SQLException ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    public boolean editProperty(String objectName, PropertyModel property) throws SQLException {
        return this.editProperty(objectName, property.getName(), property, "", false);
    }

    public boolean editProperty(String objectName, String oldName, PropertyModel property) throws SQLException {
        return this.editProperty(objectName, oldName, property, "", false);
    }

    public boolean movePropertyToAfter(String objectName, PropertyModel property, String after) throws SQLException {
        return this.editProperty(objectName, property.getName(), property, after, false);
    }

    public boolean movePropertyToFirst(String objectName, PropertyModel property) throws SQLException {
        return this.editProperty(objectName, property.getName(), property, "", true);
    }

    public boolean dropProperty(String objectName, String propertyName) throws SQLException {
        String sql = "ALTER TABLE " + MySQLManager.quoteName(objectName);
        sql += " DROP " + MySQLManager.quoteName(propertyName);
        try {
            return execute(sql);
        } catch (SQLException ex) {
            LOG.error(ex);
            throw ex;
        }
    }
}
