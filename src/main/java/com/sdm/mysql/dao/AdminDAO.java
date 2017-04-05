/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.mysql.dao;

import com.sdm.mysql.model.ObjectModel;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
        List<ObjectModel> dataList = new ArrayList<>();

        try (ResultSet resultSet = executeQuery("SHOW TABLE STATUS")) {            
            while (resultSet.next()) {
                ObjectModel model = new ObjectModel();
                model.setName(resultSet.getString("Name"));
                model.setEngine(resultSet.getString("Engine"));
                model.setVersion(resultSet.getInt("Version"));
                model.setDataCount(resultSet.getLong("Rows"));                
                model.setCurrentSerial(resultSet.getLong("Auto_increment"));
                model.setCreatedDate(resultSet.getDate("Create_Time"));    
                dataList.add(model);
            }
        }

        return dataList;
    }
}
