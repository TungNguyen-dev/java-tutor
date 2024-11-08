package com.tungnn.tutor.java.core17.base.library.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SimpleQuery {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/tutor";
        String username = "root";
        String password = "root";

        try (Connection con =
                 DriverManager.getConnection(url, username, password);
             Statement stmt = con.createStatement()) {

            String sql = "SELECT * FROM users";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println("User ID: " + rs.getInt(1));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
