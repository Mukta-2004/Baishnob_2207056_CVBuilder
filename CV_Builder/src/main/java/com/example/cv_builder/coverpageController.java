package com.example.cv_builder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class coverpageController {

    @FXML
    private void nextPage(ActionEvent event) throws IOException {
        System.out.println("Next button clicked!");

        Parent nextRoot = FXMLLoader.load(getClass().getResource("nextpage.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(nextRoot));
        stage.show();
    }
}
