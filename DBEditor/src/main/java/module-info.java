module com.example.autificationsql {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.java;

    opens com.example.autificationsql to javafx.fxml;
    exports com.example.autificationsql;
}