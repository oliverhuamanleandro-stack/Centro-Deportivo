package com.centrodeportivo;

import java.sql.*;

public class ConnectionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/centro_deportivo?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // tu usuario MySQL
    private static final String PASS = "";     // tu contraseña MySQL

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}