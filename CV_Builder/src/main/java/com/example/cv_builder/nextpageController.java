package com.example.cv_builder;

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

        for (int i = 0; i < 5; i++) {
            educationTable.getItems().add(new Education("", "", "", "", ""));
        }
    }

    @FXML
    private void addRow(ActionEvent event) {
        educationTable.getItems().add(new Education("", "", "", "", ""));
    }

    @FXML
    private void generateCV(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("finalcv.fxml"));
        Parent root = loader.load();

        FinalCVController controller = loader.getController();

        controller.setData(
                txtName.getText(),
                txtEmail.getText(),
                txtPhone.getText(),
                txtAddress.getText(),
                txtSkills.getText(),
                txtProjects.getText(),
                educationTable.getItems(),
                txtExperience.getText()
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, 900, 700);  // << RECOMMENDED SIZE
        stage.setScene(scene);

        stage.setMinWidth(900);   // prevents layout breaking
        stage.setMinHeight(700);

        stage.show();
    }

}
