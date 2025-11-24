package com.example.cv_builder;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class nextpageController implements Initializable {

    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private TextField txtAddress;

    @FXML private TextArea txtSkills;
    @FXML private TextArea txtProjects;
    @FXML private TextArea txtExperience;

    @FXML private TableView<Education> educationTable;
    @FXML private TableColumn<Education, String> examCol;
    @FXML private TableColumn<Education, String> deptCol;
    @FXML private TableColumn<Education, String> boardCol;
    @FXML private TableColumn<Education, String> yearCol;
    @FXML private TableColumn<Education, String> gradeCol;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        educationTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        educationTable.setEditable(true);

        examCol.setCellValueFactory(new PropertyValueFactory<>("exam"));
        deptCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        boardCol.setCellValueFactory(new PropertyValueFactory<>("board"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));

        examCol.setCellFactory(TextFieldTableCell.forTableColumn());
        deptCol.setCellFactory(TextFieldTableCell.forTableColumn());
        boardCol.setCellFactory(TextFieldTableCell.forTableColumn());
        yearCol.setCellFactory(TextFieldTableCell.forTableColumn());
        gradeCol.setCellFactory(TextFieldTableCell.forTableColumn());

        examCol.setOnEditCommit(e -> e.getRowValue().setExam(e.getNewValue()));
        deptCol.setOnEditCommit(e -> e.getRowValue().setDepartment(e.getNewValue()));
        boardCol.setOnEditCommit(e -> e.getRowValue().setBoard(e.getNewValue()));
        yearCol.setOnEditCommit(e -> e.getRowValue().setYear(e.getNewValue()));
        gradeCol.setOnEditCommit(e -> e.getRowValue().setGrade(e.getNewValue()));

        Task<Void> loadTask = new Task<>() {
            @Override
            protected Void call() {
                var list = CVDAO.getAllEducations();
                Platform.runLater(() -> {
                    educationTable.setItems(list);
                    if (list.isEmpty()) {
                        for (int i = 0; i < 5; i++) {
                            educationTable.getItems().add(new Education("", "", "", "", ""));
                        }
                    }
                });
                return null;
            }
        };

        loadTask.setOnFailed(e -> {
            loadTask.getException().printStackTrace();
            if (educationTable.getItems().isEmpty()) {
                for (int i = 0; i < 5; i++) {
                    educationTable.getItems().add(new Education("", "", "", "", ""));
                }
            }
        });

        new Thread(loadTask, "edu-loader").start();
    }

    @FXML
    private void addRow(ActionEvent event) {
        educationTable.getItems().add(new Education("", "", "", "", ""));
    }

    @FXML
    private void generateCV(ActionEvent event) throws IOException {

        Node source = (Node) event.getSource();
        if (source instanceof Button) {
            ((Button) source).setDisable(true);
        }

        String name = txtName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String address = txtAddress.getText();
        String skills = txtSkills.getText();
        String projects = txtProjects.getText();
        String experience = txtExperience.getText();

        Task<Void> saveTask = new Task<>() {
            @Override
            protected Void call() {
                CVDAO.saveCV(name, email, phone, address, skills, projects, experience);
                CVDAO.saveEducations(educationTable.getItems());
                return null;
            }
        };

        saveTask.setOnSucceeded(t -> {
            if (source instanceof Button) {
                ((Button) source).setDisable(false);
            }
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("finalcv.fxml"));
                    Parent root = loader.load();
                    FinalCVController controller = loader.getController();
                    controller.setData(
                            name,
                            email,
                            phone,
                            address,
                            skills,
                            projects,
                            educationTable.getItems(),
                            experience
                    );

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root, 900, 700);
                    stage.setScene(scene);
                    stage.setMinWidth(900);
                    stage.setMinHeight(700);
                    stage.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert("Error", "Unable to open Final CV view: " + ex.getMessage());
                }
            });
        });

        saveTask.setOnFailed(t -> {
            if (source instanceof Button) {
                ((Button) source).setDisable(false);
            }
            saveTask.getException().printStackTrace();
            showAlert("Save Error", "Failed to save CV: " + saveTask.getException().getMessage());
        });

        new Thread(saveTask, "cv-saver").start();
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle(title);
            a.setHeaderText(null);
            a.setContentText(message);
            a.showAndWait();
        });
    }
}
