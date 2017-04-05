/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.mysql.dao;

import com.sdm.mysql.model.PropertyModel;
import com.sdm.mysql.model.type.PropertyType;
import com.sdm.mysql.model.type.Text;
import com.sdm.mysql.util.MySQLManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Htoonlin
 */
public class AdminDAO extends MySQLDAO {

    private final String OBJECT_LIST = "SHOW TABLE STATUS";
    private final String OBJECT_DESC = "SHOW FULL COLUMNS FROM ";

    public AdminDAO() throws SQLException, IOException, ClassNotFoundException {
    }

    public AdminDAO(Connection connection) {
        super(connection);
    }

    public List fetchObjects() throws SQLException {
        List<Map<String, Object>> dataList = new ArrayList<>();

        try (ResultSet resultSet = executeQuery("SHOW TABLE STATUS")) {
            while (resultSet.next()) {
                Map<String, Object> data = new HashMap<>();
                data.put("name", resultSet.getString("Name"));
                data.put("data_count", resultSet.getLong("Rows"));
                data.put("created_at", resultSet.getDate("Create_Time"));
                dataList.add(data);
            }
        }

        return dataList;
    }

    private PropertyType typeConverter(String sql) {
        sql = sql.toUpperCase();
        if (sql.startsWith("CHAR") || sql.startsWith("VARCHAR")) {
            return new Text(sql);
        }

        if (sql.contains("INT") || sql.contains("INTEGER")) {
            return new com.sdm.mysql.model.type.Integer(sql);
        }

        if (sql.startsWith("FLOAT") || sql.startsWith("DOUBLE") || sql.startsWith("DECIMAL")) {
            return new com.sdm.mysql.model.type.Float(sql);
        }

        if (sql.contains("DATE") || sql.contains("TIME") || sql.startsWith("YEAR")) {
            return new com.sdm.mysql.model.type.DateTime(sql);
        }

        if (sql.startsWith("ENUM")) {
            return new com.sdm.mysql.model.type.List(sql);
        }

        if (sql.equalsIgnoreCase("BIT(1)")) {
            return new com.sdm.mysql.model.type.Boolean();
        }

        return null;
    }

    public List<PropertyModel> descObject(String objectName) throws SQLException {
        List<PropertyModel> properties = new ArrayList<>();
        String query = OBJECT_DESC + MySQLManager.quoteName(objectName);
        try (ResultSet resultSet = executeQuery(query)) {
            int i = 0;
            while (resultSet.next()) {
                PropertyModel model = new PropertyModel();
                model.setName(resultSet.getString("Field"));
                model.setType(this.typeConverter(resultSet.getString("Type")));
                model.setRequire(!resultSet.getBoolean("Null"));
                String key = resultSet.getString("Key");
                model.setPrimary(key != null && key.equalsIgnoreCase("PRI"));
                String extra = resultSet.getString("Extra");
                if (extra != null && model.getType() instanceof com.sdm.mysql.model.type.Integer) {
                    com.sdm.mysql.model.type.Integer currentType = (com.sdm.mysql.model.type.Integer) model.getType();
                    currentType.setSerial(extra.equalsIgnoreCase("AUTO_INCREMENT"));
                }
                String comment = resultSet.getString("Comment");
                if (comment != null && comment.length() > 0) {
                    model.setDescription(comment);
                }
                model.setIndex(i++);                
                properties.add(model);
            }
        }
        return properties;
    }
}
