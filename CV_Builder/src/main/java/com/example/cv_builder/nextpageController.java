package com.example.cv_builder;

import javafx.application.Platform;
import javafx.collections.ObservableList;
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

    @FXML private ComboBox<CV> cvSelector;

    private int currentCvId = -1;

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

        loadCvList();

        ObservableList<Education> items = educationTable.getItems();
        if (items.isEmpty()) {
            for (int i = 0; i < 5; i++) items.add(new Education("", "", "", "", ""));
        }
    }

    @FXML
    private void onNewCV(ActionEvent event) {
        currentCvId = -1;
        clearForm();
        educationTable.getItems().clear();
        for (int i = 0; i < 5; i++) educationTable.getItems().add(new Education("", "", "", "", ""));
        educationTable.refresh();
        cvSelector.getSelectionModel().clearSelection();
    }

    @FXML
    private void onLoadCV(ActionEvent event) {
        CV selected = cvSelector.getValue();
        if (selected == null) {
            showAlertInfo("Select CV", "Please choose a CV from the dropdown to load.");
            return;
        }
        loadCVIntoForm(selected.getId());
    }

    @FXML
    private void onSaveOrUpdateCV(ActionEvent event) {
        String name = txtName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String address = txtAddress.getText();
        String skills = txtSkills.getText();
        String projects = txtProjects.getText();
        String experience = txtExperience.getText();
        ObservableList<Education> educations = educationTable.getItems();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                if (currentCvId == -1) {
                    int newId = CVDAO.createCV(name, email, phone, address, skills, projects, experience);
                    if (newId > 0) {
                        CVDAO.saveEducationsForCv(newId, educations);
                        currentCvId = newId;
                    }
                } else {
                    CVDAO.updateCV(currentCvId, name, email, phone, address, skills, projects, experience);
                    CVDAO.saveEducationsForCv(currentCvId, educations);
                }
                return null;
            }
        };

        task.setOnSucceeded(t -> {
            Platform.runLater(() -> {
                showAlertInfo("Saved", currentCvId == -1 ? "CV created." : "CV updated.");
                loadCvList();
            });
        });

        task.setOnFailed(t -> {
            task.getException().printStackTrace();
            showAlert("Save Error", "Failed to save CV: " + task.getException().getMessage());
        });

        new Thread(task, "cv-save").start();
    }

    @FXML
    private void onDeleteCV(ActionEvent event) {
        CV selected = cvSelector.getValue();
        if (selected == null) {
            showAlertInfo("Select CV", "Please choose a CV from the dropdown to delete.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete CV");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete selected CV (" + selected.toString() + ")? This cannot be undone.");
        var res = confirm.showAndWait();
        if (res.isEmpty() || res.get() != ButtonType.OK) return;

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                CVDAO.deleteCV(selected.getId());
                return null;
            }
        };

        task.setOnSucceeded(t -> {
            Platform.runLater(() -> {
                showAlertInfo("Deleted", "CV deleted.");
                onNewCV(null);
                loadCvList();
            });
        });

        task.setOnFailed(t -> {
            task.getException().printStackTrace();
            showAlert("Delete Error", "Failed to delete CV: " + task.getException().getMessage());
        });

        new Thread(task, "cv-delete").start();
    }

    @FXML
    private void addRow(ActionEvent event) {
        educationTable.getItems().add(new Education("", "", "", "", ""));
        educationTable.refresh();
    }

    @FXML
    private void generateCV(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        if (source instanceof Button) ((Button) source).setDisable(true);

        String name = txtName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String address = txtAddress.getText();
        String skills = txtSkills.getText();
        String projects = txtProjects.getText();
        String experience = txtExperience.getText();
        ObservableList<Education> educations = educationTable.getItems();

        Task<Void> saveAndShow = new Task<>() {
            @Override
            protected Void call() {
                if (currentCvId == -1) {
                    int id = CVDAO.createCV(name, email, phone, address, skills, projects, experience);
                    if (id > 0) {
                        CVDAO.saveEducationsForCv(id, educations);
                        currentCvId = id;
                    }
                } else {
                    CVDAO.updateCV(currentCvId, name, email, phone, address, skills, projects, experience);
                    CVDAO.saveEducationsForCv(currentCvId, educations);
                }
                return null;
            }
        };

        saveAndShow.setOnSucceeded(t -> {
            if (source instanceof Button) ((Button) source).setDisable(false);
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

        saveAndShow.setOnFailed(t -> {
            if (source instanceof Button) ((Button) source).setDisable(false);
            saveAndShow.getException().printStackTrace();
            showAlert("Save Error", "Failed to save CV: " + saveAndShow.getException().getMessage());
        });

        new Thread(saveAndShow, "cv-save-and-show").start();
    }

    private void loadCvList() {
        Task<ObservableList<CV>> task = new Task<>() {
            @Override
            protected ObservableList<CV> call() {
                return CVDAO.getAllCVs();
            }
        };

        task.setOnSucceeded(t -> {
            cvSelector.setItems(task.getValue());
        });
        task.setOnFailed(t -> {
            task.getException().printStackTrace();
        });

        new Thread(task, "cv-list-loader").start();
    }

    private void loadCVIntoForm(int cvId) {
        Task<CVDAO.CVData> task = new Task<>() {
            @Override
            protected CVDAO.CVData call() {
                return CVDAO.loadCV(cvId);
            }
        };

        task.setOnSucceeded(t -> {
            CVDAO.CVData d = task.getValue();
            if (d == null) {
                showAlert("Load Error", "Could not load selected CV.");
                return;
            }
            currentCvId = d.getId();
            txtName.setText(d.getName());
            txtEmail.setText(d.getEmail());
            txtPhone.setText(d.getPhone());
            txtAddress.setText(d.getAddress());
            txtSkills.setText(d.getSkills());
            txtProjects.setText(d.getProjects());
            txtExperience.setText(d.getExperience());

            ObservableList<Education> ed = d.getEducations();
            educationTable.setItems(ed);
            if (ed.isEmpty()) {
                for (int i = 0; i < 5; i++) educationTable.getItems().add(new Education("", "", "", "", ""));
            }
            educationTable.refresh();
        });

        task.setOnFailed(t -> {
            task.getException().printStackTrace();
            showAlert("Load Error", "Failed to load CV: " + task.getException().getMessage());
        });

        new Thread(task, "cv-loader").start();
    }

    private void clearForm() {
        txtName.clear();
        txtEmail.clear();
        txtPhone.clear();
        txtAddress.clear();
        txtSkills.clear();
        txtProjects.clear();
        txtExperience.clear();
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

    private void showAlertInfo(String title, String message) {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle(title);
            a.setHeaderText(null);
            a.setContentText(message);
            a.showAndWait();
        });
    }
}
