package com.tungnn.tutor.java.sql.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Demo {

  public static void main(String[] args) {
    String url = "jdbc:mysql://localhost:3306/mydb";
    String username = "user";
    String password = "password";

    // Try to establish the connection
    try (Connection connection = DriverManager.getConnection(url, username, password)) {
      System.out.println("Connection established successfully!");
      connection.createStatement().execute("SELECT 1");
    } catch (SQLException e) {
      System.err.println("Failed to establish connection to the database.");
      e.printStackTrace();
    }
  }
}
