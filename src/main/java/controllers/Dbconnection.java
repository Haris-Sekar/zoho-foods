package controllers;

import java.sql.*;

public class Dbconnection {
    public Connection initializeDatabase()
            throws SQLException, ClassNotFoundException {
        String dbDriver = "com.mysql.jdbc.Driver";
        String dbURL = "jdbc:mysql://localhost:3306/";
        String dbName = "zoho_food";
        String dbUsername = "root";
        String dbPassword = "haris@1400";
        Class.forName(dbDriver);
        return DriverManager.getConnection(dbURL + dbName, dbUsername, dbPassword);
    }
}
