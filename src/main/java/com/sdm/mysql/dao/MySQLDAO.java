/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.mysql.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author Htoonlin
 */
public class MySQLDAO {

    private final String DRIVER = "com.mysql.jdbc.Driver";
    private final String PROP_HOST = "MYSQL_DB_SERVER";
    private final String PROP_DB = "MYSQL_DB_NAME";
    private final String PROP_USER = "MYSQL_DB_USER";
    private final String PROP_PWD = "MYSQL_DB_PWD";
    private static final Logger LOG = Logger.getLogger(MySQLDAO.class.getName());

    private Connection connection;
    private Object lastInsertedId;

    public Connection getConnection() {
        return connection;
    }

    public Object getLastInsertedId() {
        return lastInsertedId;
    }

    public void setLastInsertedId(double lastInsertedId) {
        this.lastInsertedId = lastInsertedId;
    }

    public void closeConnection() throws SQLException {
        if (!this.connection.isClosed()) {
            this.connection.close();
        }
    }

    public MySQLDAO() throws SQLException, IOException, ClassNotFoundException {
        try {
            Class.forName(DRIVER);
            Properties props = new Properties();
            props.load(getClass().getClassLoader().getResourceAsStream("setting.properties"));

            String url = "jdbc:mysql://" + props.getProperty(PROP_HOST, "localhost:3306") + "/";
            url += props.getProperty(PROP_DB, "test");
            //Reference Properties => https://dev.mysql.com/doc/connector-j/6.0/en/connector-j-reference-configuration-properties.html
            url += "?useSSL=false&zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=UTF-8";

            String user = props.getProperty(PROP_USER, "root");

            String pwd = props.getProperty(PROP_PWD, "");

            this.connection = DriverManager.getConnection(url, user, pwd);
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    public MySQLDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean execute(String sql) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            return statement.execute(sql);
        } catch (SQLException ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    public boolean execute(String sql, List<Object> values) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                statement.setObject(i + 1, values.get(i));
            }
            return statement.execute();
        } catch (SQLException ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    public int executeUpdate(String sql) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(sql);
        } catch (SQLException ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    public int executeUpdate(String sql, List<Object> values) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                statement.setObject(i + 1, values.get(i));
            }
            return statement.executeUpdate();
        } catch (SQLException ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    public int executeUpdateWithAI(String sql, List<Object> values) throws SQLException {
        int effected = 0;
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < values.size(); i++) {
                statement.setObject(i + 1, values.get(i));
            }
            effected = statement.executeUpdate();
            if (effected > 0) {
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.first()) {
                        lastInsertedId = resultSet.getObject(1);
                    }
                }
            }
        } catch (SQLException ex) {
            LOG.error(ex);
            throw ex;
        }

        return effected;
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    public ResultSet executeQuery(String sql, List<Object> values) throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 0; i < values.size(); i++) {
                statement.setObject(i + 1, values.get(i));
            }
            return statement.executeQuery();
        } catch (SQLException ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.closeConnection();
    }
}
