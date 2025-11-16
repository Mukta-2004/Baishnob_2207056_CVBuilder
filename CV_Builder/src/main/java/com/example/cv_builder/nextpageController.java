package com.example.cv_builder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class nextpageController implements Initializable {

    @FXML private TableView<Education> educationTable;
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

        examCol.setCellValueFactory(new PropertyValueFactory<>("exam"));
        deptCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        boardCol.setCellValueFactory(new PropertyValueFactory<>("board"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));

        for (int i = 0; i < 5; i++) {
            educationTable.getItems().add(new Education("", "", "", "", ""));
        }

        educationTable.setEditable(true);
    }
}
