package com.bank.repository;

import java.sql.Connection;
import java.sql.SQLException;

public class AppRepository {
    private final Connection connection;

    public AppRepository(Connection connection) {
        this.connection = connection;
    }

    public void test() throws SQLException {
        final var stm = connection.createStatement();
        final var rs = stm.executeQuery("SELECT 1+1");
        if (rs.next()) {
            System.out.println(rs.getInt(1));
        }
        rs.close();
        stm.close();
    }
}
