package com.tungnn.tutor.java.core17.enthuware.test01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Q30 {

    public static void main(String[] args) throws SQLException {
        Connection con = DriverManager.getConnection("dbURL");
        con.setAutoCommit(false);

        Statement stmt = con.createStatement();

        String updateString =
            "UPDATE sales SET t_amount = 100 WHERE t_name = 'BOB'";
        stmt.executeUpdate(updateString);

        con.commit();
    }
}