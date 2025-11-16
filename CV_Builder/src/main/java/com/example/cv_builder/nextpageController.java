package com.example.cv_builder;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class nextpageController implements Initializable {

    @FXML
    private TableView<Education> educationTable;

    @FXML private TableColumn<Education, String> examCol;
    @FXML private TableColumn<Education, String> deptCol;
    @FXML private TableColumn<Education, String> boardCol;
    @FXML private TableColumn<Education, String> yearCol;
    @FXML private TableColumn<Education, String> gradeCol;

    @FXML
    private void generateCV(ActionEvent event) {
        System.out.println("Generate CV clicked!");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

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

        examCol.setOnEditCommit(e ->
                e.getRowValue().setExam(e.getNewValue()));
        deptCol.setOnEditCommit(e ->
                e.getRowValue().setDepartment(e.getNewValue()));
        boardCol.setOnEditCommit(e ->
                e.getRowValue().setBoard(e.getNewValue()));
        yearCol.setOnEditCommit(e ->
                e.getRowValue().setYear(e.getNewValue()));
        gradeCol.setOnEditCommit(e ->
                e.getRowValue().setGrade(e.getNewValue()));

        for (int i = 0; i < 5; i++) {
            educationTable.getItems().add(
                    new Education("", "", "", "", "")
            );
        }
    }
}
