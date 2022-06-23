module com.student_management.studentmanagement {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires commons.codec;
    requires com.google.gson;
    requires TrayNotification;
    requires MaterialFX;

    opens com.school_management.models to javafx.base;
    opens com.school_management.controllers to javafx.fxml;
    exports com.school_management.main to javafx.graphics;
    exports com.school_management.controllers;
}