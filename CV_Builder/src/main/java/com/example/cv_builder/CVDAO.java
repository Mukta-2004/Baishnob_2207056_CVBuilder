package com.example.cv_builder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CVDAO {

    public static void saveCV(String name, String email, String phone, String address,
                              String skills, String projects, String experience) {

        String sql = "INSERT INTO personal_info(name, email, phone, address, skills, projects, experience) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.setString(4, address);
            pstmt.setString(5, skills);
            pstmt.setString(6, projects);
            pstmt.setString(7, experience);

            pstmt.executeUpdate();
            System.out.println("CV saved to database!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
