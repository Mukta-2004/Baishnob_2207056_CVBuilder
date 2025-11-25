package com.example.cv_builder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.List;

public class CVDAO {

    public static int createCV(String name, String email, String phone, String address,
                               String skills, String projects, String experience) {
        String sql = "INSERT INTO personal_info(name, email, phone, address, skills, projects, experience) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setString(5, skills);
            ps.setString(6, projects);
            ps.setString(7, experience);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void updateCV(int id, String name, String email, String phone, String address,
                                String skills, String projects, String experience) {
        String sql = "UPDATE personal_info SET name=?, email=?, phone=?, address=?, skills=?, projects=?, experience=? WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setString(5, skills);
            ps.setString(6, projects);
            ps.setString(7, experience);
            ps.setInt(8, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void deleteCV(int id) {
        String sqlDelEdu = "DELETE FROM education WHERE cv_id = ?";
        String sqlDelCV = "DELETE FROM personal_info WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps1 = conn.prepareStatement(sqlDelEdu);
             PreparedStatement ps2 = conn.prepareStatement(sqlDelCV)) {
            conn.setAutoCommit(false);
            ps1.setInt(1, id);
            ps1.executeUpdate();

            ps2.setInt(1, id);
            ps2.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static ObservableList<CV> getAllCVs() {
        ObservableList<CV> out = FXCollections.observableArrayList();
        String sql = "SELECT id, name, email FROM personal_info ORDER BY id DESC";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new CV(rs.getInt("id"), rs.getString("name"), rs.getString("email")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return out;
    }

    public static CVData loadCV(int id) {
        String sql = "SELECT id, name, email, phone, address, skills, projects, experience FROM personal_info WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CVData d = new CVData(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("address"),
                            rs.getString("skills"),
                            rs.getString("projects"),
                            rs.getString("experience")
                    );
                    d.setEducations(getEducationsByCv(id));
                    return d;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    public static ObservableList<Education> getEducationsByCv(int cvId) {
        ObservableList<Education> list = FXCollections.observableArrayList();
        String sql = "SELECT exam, department, board, year, grade FROM education WHERE cv_id = ? ORDER BY id";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cvId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Education(
                            rs.getString("exam") == null ? "" : rs.getString("exam"),
                            rs.getString("department") == null ? "" : rs.getString("department"),
                            rs.getString("board") == null ? "" : rs.getString("board"),
                            rs.getString("year") == null ? "" : rs.getString("year"),
                            rs.getString("grade") == null ? "" : rs.getString("grade")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return list;
    }

    public static void saveEducationsForCv(int cvId, List<Education> educations) {
        String deleteSql = "DELETE FROM education WHERE cv_id = ?";
        String insertSql = "INSERT INTO education(exam, department, board, year, grade, cv_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement del = conn.prepareStatement(deleteSql);
             PreparedStatement ins = conn.prepareStatement(insertSql)) {
            conn.setAutoCommit(false);

            del.setInt(1, cvId);
            del.executeUpdate();

            for (Education edu : educations) {
                if (isBlank(edu.getExam()) && isBlank(edu.getDepartment()) && isBlank(edu.getBoard())
                        && isBlank(edu.getYear()) && isBlank(edu.getGrade())) continue;
                ins.setString(1, edu.getExam());
                ins.setString(2, edu.getDepartment());
                ins.setString(3, edu.getBoard());
                ins.setString(4, edu.getYear());
                ins.setString(5, edu.getGrade());
                ins.setInt(6, cvId);
                ins.addBatch();
            }
            ins.executeBatch();
            conn.commit();
            System.out.println("Saved educations for cvId=" + cvId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static class CVData {
        private final int id;
        private final String name;
        private final String email;
        private final String phone;
        private final String address;
        private final String skills;
        private final String projects;
        private final String experience;
        private ObservableList<Education> educations = FXCollections.observableArrayList();

        public CVData(int id, String name, String email, String phone, String address, String skills, String projects, String experience) {
            this.id = id;
            this.name = name == null ? "" : name;
            this.email = email == null ? "" : email;
            this.phone = phone == null ? "" : phone;
            this.address = address == null ? "" : address;
            this.skills = skills == null ? "" : skills;
            this.projects = projects == null ? "" : projects;
            this.experience = experience == null ? "" : experience;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getAddress() { return address; }
        public String getSkills() { return skills; }
        public String getProjects() { return projects; }
        public String getExperience() { return experience; }
        public ObservableList<Education> getEducations() { return educations; }
        public void setEducations(ObservableList<Education> list) { this.educations = list; }
    }
}
