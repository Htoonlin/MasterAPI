/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.mysql.dao;

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
}
