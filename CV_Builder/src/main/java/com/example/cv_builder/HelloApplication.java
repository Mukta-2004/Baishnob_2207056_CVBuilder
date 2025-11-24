package com.example.cv_builder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Database.initialize();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("coverpage.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("CV Builder");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
