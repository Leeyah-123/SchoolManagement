package com.school_management.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Alerts {
    public static void AlertInfo(String title, String body) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("Info");
        alert.setContentText(body);
        alert.showAndWait();
    }

    public static ButtonType AlertConfirmation(String title, String body) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText("Confirmation");
        alert.setContentText(body);
        if (alert.showAndWait().isPresent()) {
            return alert.getResult();
        }
        return ButtonType.CLOSE;
    }

    public static void AlertError(String title, String body) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("Error");
        alert.setContentText(body);
        alert.showAndWait();
    }
}
