package com.tungnn.tutor.java.core17.enthuware.test03;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;

public class Q07 {

    public static void main(String[] args) {
        String qr = """
                    insert into STOCK ( ID, TICKER, LASTPRICE, EXCHANGE ) 
                    values(?, ?, ?, ?)""";

        try (Connection c = null;
             PreparedStatement ps = c.prepareStatement(qr)
        ) {
            ps.setInt(1, 1);
            ps.setString(2, "APPLE");
            ps.setNull(3, Types.INTEGER);
            ps.setString(4, null);

            int i = ps.executeUpdate();
            System.out.println(i);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
