package com.mycompany.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Admin {
    public static boolean CheckEmpty() throws SQLException {
        String Query = "SELECT COUNT(*) AS num FROM admin";
        Connection conct = MySQLConnector.getConnection();
        PreparedStatement stmt = conct.prepareStatement(Query);
        ResultSet resultSet = stmt.executeQuery();
        int rowCount = 0;
        if (resultSet.next()) {
            rowCount = resultSet.getInt("num");
        }
        conct.close();
        return rowCount == 0;

    }


    public static void addAdmin(String firstName, String lastName, String email, String password) throws SQLException {
        String Query = "INSERT INTO admin (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
        Connection conct = MySQLConnector.getConnection();
        conct.setAutoCommit(false);
        PreparedStatement stmt = conct.prepareStatement(Query);
        stmt.setString(1, firstName);
        stmt.setString(2, lastName);
        stmt.setString(3, email);
        stmt.setString(4, password);
        stmt.executeUpdate();
        conct.commit();
        conct.close();
    }

    public static boolean checkLogin(String email, String password) throws SQLException {
        String Query = "SELECT * FROM admin WHERE email = ? AND password = ?";
        Connection conct = MySQLConnector.getConnection();
        PreparedStatement stmt = conct.prepareStatement(Query);
        stmt.setString(1, email);
        stmt.setString(2, password);
        ResultSet resultSet = stmt.executeQuery();
        boolean result = resultSet.next();
        conct.close();
        return result;
    }

}

