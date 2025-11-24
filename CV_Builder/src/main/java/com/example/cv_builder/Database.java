package com.example.cv_builder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:sqlite:cvbuilder.db";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void initialize() {
        String sql = """
            CREATE TABLE IF NOT EXISTS personal_info (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                email TEXT,
                phone TEXT,
                address TEXT,
                skills TEXT,
                projects TEXT,
                experience TEXT
            );
        """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Database initialized!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
