package com.yegor.jdbcConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by YegorKost on 25.02.2017.
 */
public class PostgreSqlConnection {
    private static Connection connection;

    public static void setConnection(String url, String user, String password) {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return connection == null
                ? connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/javaHW5", "postgres", "1234")
                : connection;
    }
}
