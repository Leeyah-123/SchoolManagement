package com.school_management.controllers;

import com.school_management.dao.UsersDB;
import com.school_management.io.UserWriter;
import com.school_management.utils.Alerts;
import com.school_management.utils.Constants;
import com.school_management.utils.SceneController;
import com.school_management.utils.StageDraggable;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private AnchorPane mainPane;
    @FXML
    private MFXButton btnDashboard, btnStudents, btnTeachers, btnClasses, btnStatistics, btnAccount, btnExpenses;

    private void setSelected(MFXButton selectedBtn) {

    }

    public void viewDashboard() {
        SceneController.switchScene(mainPane, Constants.DASHBOARD_FXML_DIR);
        setSelected(btnDashboard);
    }
    public void viewStudents() {
        SceneController.switchScene(mainPane, Constants.STUDENTS_FXML_DIR);
        setSelected(btnStudents);
    }

    public void viewTeachers() {
        if (UsersDB.getUserRole().equals("teacher") || UsersDB.getUserRole().equals("moderator")) {
            Alerts.AlertError("Error", "Sorry, you do not have access to this resource");
            return;
        }
        SceneController.switchScene(mainPane, Constants.TEACHERS_FXML_DIR);
        setSelected(btnTeachers);
    }

    public void viewClasses() {
        if (UsersDB.getUserRole().equals("teacher")) {
            Alerts.AlertError("Error", "Sorry, you do not have access to this resource");
            return;
        }
        SceneController.switchScene(mainPane, Constants.CLASSES_FXML_DIR);
        setSelected(btnClasses);
    }

    public void viewStatistics() {
        if (UsersDB.getUserRole().equals("teacher") || UsersDB.getUserRole().equals("moderator")) {
            Alerts.AlertError("Error", "Sorry, you do not have access to this resource");
            return;
        }
        SceneController.switchScene(mainPane, Constants.STATISTICS_FXML_DIR);
        setSelected(btnStatistics);
    }

    public void viewAccount() {
        if (UsersDB.getUserRole().equals("teacher") || UsersDB.getUserRole().equals("moderator")) {
            Alerts.AlertError("Error", "Sorry, you do not have access to this resource");
            return;
        }
        SceneController.switchScene(mainPane, Constants.ACCOUNTS_FXML_DIR);
        setSelected(btnAccount);
    }

    public void viewExpenses() {
        if (UsersDB.getUserRole().equals("teacher")) {
            Alerts.AlertError("Error", "Sorry, you do not have access to this resource");
            return;
        }
        SceneController.switchScene(mainPane, Constants.EXPENSES_FXML_DIR);
        setSelected(btnExpenses);
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
        setSelected(btnDashboard);
    }
}