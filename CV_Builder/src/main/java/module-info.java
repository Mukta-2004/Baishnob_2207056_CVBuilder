module com.example.cv_builder {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens com.example.cv_builder to javafx.fxml;
    exports com.example.cv_builder;
}