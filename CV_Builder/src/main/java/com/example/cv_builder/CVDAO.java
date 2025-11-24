package com.example.cv_builder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
            throw new RuntimeException(e);
        }
    }

    public static ObservableList<Education> getAllEducations() {
        ObservableList<Education> list = FXCollections.observableArrayList();
        String sql = "SELECT exam, department, board, year, grade FROM education ORDER BY id";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String exam = rs.getString("exam");
                String dept = rs.getString("department");
                String board = rs.getString("board");
                String year = rs.getString("year");
                String grade = rs.getString("grade");

                list.add(new Education(
                        exam == null ? "" : exam,
                        dept == null ? "" : dept,
                        board == null ? "" : board,
                        year == null ? "" : year,
                        grade == null ? "" : grade
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return list;
    }

    public static void clearEducations() {
        String sql = "DELETE FROM education";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
            System.out.println("All education rows cleared.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void insertEducation(String exam, String department, String board, String year, String grade) {
        String sql = "INSERT INTO education(exam, department, board, year, grade) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, exam);
            ps.setString(2, department);
            ps.setString(3, board);
            ps.setString(4, year);
            ps.setString(5, grade);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void saveEducations(List<Education> educations) {
        clearEducations();
        for (Education edu : educations) {
            boolean empty = (isBlank(edu.getExam()) &&
                    isBlank(edu.getDepartment()) &&
                    isBlank(edu.getBoard()) &&
                    isBlank(edu.getYear()) &&
                    isBlank(edu.getGrade()));
            if (empty) continue;
            insertEducation(edu.getExam(), edu.getDepartment(), edu.getBoard(), edu.getYear(), edu.getGrade());
        }
        System.out.println("Education list saved to DB.");
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
