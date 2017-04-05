/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.mysql.dao;

import com.sdm.core.mysql.model.query.Condition;
import com.sdm.master.request.object.QueryRequest;
import com.sdm.core.mysql.util.MySQLManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Htoonlin
 */
public class RestDAO extends MySQLDAO {

    public RestDAO() throws SQLException, IOException, ClassNotFoundException {
        super();
    }

    public RestDAO(Connection connection) {
        super(connection);
    }

    public long fetchTotal(QueryRequest query) throws SQLException {
        long totalRows = 0;
        try (ResultSet resultSet = executeQuery(query.rowCountSQL(), query.getParamValues())) {
            if (resultSet.first()) {
                totalRows = resultSet.getLong(query.ROW_COUNTS_COL);
            }
        }
        return totalRows;
    }

    public List fetch(QueryRequest query) throws SQLException {
        List<Map<String, Object>> dataList = new ArrayList<>();

        try (ResultSet resultSet = executeQuery(query.defaultSQL(), query.getParamValues())) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            while (resultSet.next()) {
                Map<String, Object> record = new HashMap<>();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    record.put(metaData.getColumnName(i), resultSet.getObject(i));
                }
                dataList.add(record);
            }
        }

        return dataList;
    }

    public int insert(String objectName, Map<String, Object> entity, boolean isAutoGenerateID) throws SQLException {
        String query = "INSERT INTO " + MySQLManager.quoteName(objectName);
        query += " (`" + String.join("`,`", entity.keySet()) + "`) VALUES(";
        List<Object> values = new ArrayList<>(entity.values());
        for (Object value : values) {
            query += "?,";
        }
        query = MySQLManager.cleanLastChar(query, ",") + ")";
        if (isAutoGenerateID) {
            int effected = executeUpdateWithAI(query, values);
            entity.put("GeneratedID", getLastInsertedId());
            return effected;
        } else {
            return executeUpdate(query, values);
        }

    }

    public int update(String objectName, Map<String, Object> entity, List<Condition> conditions) throws SQLException {
        String query = "UPDATE " + MySQLManager.quoteName(objectName) + " SET ";
        query += "`" + String.join("` = ?,`", entity.keySet()) + "` = ?";
        List<Object> values = new ArrayList<>(entity.values());
        query += " WHERE 1 = 1";

        //Build WHERE Conditions
        if (conditions != null && conditions.size() > 0) {
            for (Condition condition : conditions) {
                query += " " + condition.defaultSQL();
                values.add(condition.getValue());
            }
        }
        return executeUpdate(query, values);
    }

    public int remove(String objectName, List<Condition> conditions) throws SQLException {
        if (conditions != null && conditions.size() > 0) {
            String query = "DELETE FROM " + MySQLManager.quoteName(objectName);
            query += " WHERE 1 = 1";
            List<Object> values = new ArrayList<>();
            //Build WHERE Conditions
            for (Condition condition : conditions) {
                query += " " + condition.defaultSQL();
                values.add(condition.getValue());
            }
            return executeUpdate(query, values);
        }
        return -1;
    }

    public void truncate(String objectName) throws SQLException {
        String query = "TURNCATE TABLE " + MySQLManager.quoteName(objectName);
        execute(query);
    }
}
