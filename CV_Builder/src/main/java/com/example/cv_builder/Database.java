package com.example.cv_builder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
            System.out.println("personal_info table ensured.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void initializeDatabase() {
        String sql = """
            CREATE TABLE IF NOT EXISTS education (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                exam TEXT,
                department TEXT,
                board TEXT,
                year TEXT,
                grade TEXT
            );
        """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("education table ensured.");

            try (ResultSet rs = stmt.executeQuery("SELECT sql FROM sqlite_master WHERE type='table' AND name='education'")) {
                if (rs.next()) {
                    String createSQL = rs.getString("sql");
                    if (createSQL != null && !createSQL.contains("cv_id")) {
                        try {
                            stmt.execute("ALTER TABLE education ADD COLUMN cv_id INTEGER");
                            System.out.println("Added cv_id column to education table.");
                        } catch (SQLException ex) {
                            System.out.println("Could not add cv_id column (maybe already exists): " + ex.getMessage());
                        }
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Error checking education table SQL: " + ex.getMessage());
            }

            try {
                stmt.execute("PRAGMA foreign_keys = ON");
            } catch (SQLException ex) {
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
