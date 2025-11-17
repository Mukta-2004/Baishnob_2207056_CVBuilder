package com.example.cv_builder;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class FinalCVController {

    @FXML private Label lblName;
    @FXML private Label lblEmail;
    @FXML private Label lblPhone;
    @FXML private Label lblAddress;
    @FXML private Label lblExperience;
    @FXML private Label lblSkills;
    @FXML private Label lblProjects;
    @FXML private VBox educationList;
    public void setData(String name, String email, String phone, String address,
                        String skills, String projects, ObservableList<Education> educations,
                        String experience) {

        lblName.setText("Name: " + name);
        lblEmail.setText("Email: " + email);
        lblPhone.setText("Phone: " + phone);
        lblAddress.setText("Address: " + address);

        lblSkills.setText(skills);
        lblProjects.setText(projects);
        lblExperience.setText(experience);


        educationList.getChildren().clear();

        for (Education edu : educations) {

            if (edu.getExam().isBlank() &&
                    edu.getDepartment().isBlank() &&
                    edu.getBoard().isBlank() &&
                    edu.getYear().isBlank() &&
                    edu.getGrade().isBlank()) {
                continue;
            }

            Label row = new Label(
                    edu.getExam() + " – " +
                            edu.getDepartment() + " – " +
                            edu.getBoard() + " – " +
                            edu.getYear() + " – " +
                            edu.getGrade()
            );

            row.setStyle("-fx-text-fill: white; -fx-font-size: 14;");
            educationList.getChildren().add(row);
        }

    }
}
