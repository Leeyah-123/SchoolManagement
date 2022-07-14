package com.school_management.controllers;

import com.school_management.dao.UsersDB;
import com.school_management.io.UserWriter;
import com.school_management.utils.Alerts;
import com.school_management.utils.Constants;
import com.school_management.utils.SceneController;
import com.school_management.utils.StageDraggable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private AnchorPane mainPane;

    public void viewDashboard() {
        SceneController.switchScene(mainPane, Constants.DASHBOARD_FXML_DIR);
    }
    public void viewStudents() {
        SceneController.switchScene(mainPane, Constants.STUDENTS_FXML_DIR);
    }

    public void viewTeachers() {
        if (UsersDB.getUserRole().equals("teacher") || UsersDB.getUserRole().equals("moderator")) {
            Alerts.AlertError("Error", "Sorry, you do not have access to this resource");
            return;
        }
        SceneController.switchScene(mainPane, Constants.TEACHERS_FXML_DIR);
    }

    public void viewClasses() {
        if (UsersDB.getUserRole().equals("teacher")) {
            Alerts.AlertError("Error", "Sorry, you do not have access to this resource");
            return;
        }
        SceneController.switchScene(mainPane, Constants.CLASSES_FXML_DIR);
    }

    public void viewStatistics() {
        if (UsersDB.getUserRole().equals("teacher") || UsersDB.getUserRole().equals("moderator")) {
            Alerts.AlertError("Error", "Sorry, you do not have access to this resource");
            return;
        }
        SceneController.switchScene(mainPane, Constants.STATISTICS_FXML_DIR);
    }

    public void viewAccount() {
        if (UsersDB.getUserRole().equals("teacher") || UsersDB.getUserRole().equals("moderator")) {
            Alerts.AlertError("Error", "Sorry, you do not have access to this resource");
            return;
        }
        SceneController.switchScene(mainPane, Constants.ACCOUNTS_FXML_DIR);
    }

    public void viewExpenses() {
        if (UsersDB.getUserRole().equals("teacher")) {
            Alerts.AlertError("Error", "Sorry, you do not have access to this resource");
            return;
        }
        SceneController.switchScene(mainPane, Constants.EXPENSES_FXML_DIR);
    }

    public void logout() {
        if(Alerts.AlertConfirmation("Logout", "Are you sure you want to logout?") == ButtonType.OK) {
            UserWriter.clearCurrentUser();
            Stage stage = (Stage) mainPane.getScene().getWindow();
            stage.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SceneController.initStage(mainPane, Constants.DASHBOARD_FXML_DIR);
        StageDraggable.setStageDraggable(mainPane);
    }
}