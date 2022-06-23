package com.school_management.controllers;

import com.school_management.dao.UsersDB;
import com.school_management.models.RecursiveUser;
import com.school_management.utils.*;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXRadioButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import tray.animations.AnimationType;
import tray.notification.NotificationType;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;
import java.util.ResourceBundle;

public class SignupController implements Initializable {
    @FXML
    private MFXTextField txtFirstname, txtLastname, txtPhonenum, txtEmail;
    @FXML
    private MFXPasswordField txtPassword, txtConfirmPassword;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private MFXRadioButton maleRadioBtn, femaleRadioBtn;
    @FXML
    private ToggleGroup gender;
    UsersDB usersDB = new UsersDB();

    public void signUp() throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (txtFirstname.getText().isEmpty() || txtFirstname.getText().trim().isEmpty()) {
            Alerts.AlertError("Error", "First Name Field cannot be empty");
            registrationFail();
            return;
        }

        if (txtLastname.getText().isEmpty() || txtLastname.getText().trim().isEmpty()) {
            Alerts.AlertError("Error", "Last Name Field cannot be empty");
            registrationFail();
            return;
        }

        if (txtEmail.getText().isEmpty() || txtEmail.getText().trim().isEmpty()) {
            Alerts.AlertError("Error", "Email Field cannot be empty");
            registrationFail();
            return;
        }

        if (txtPhonenum.getText().isEmpty() || txtPhonenum.getText().trim().isEmpty()) {
            Alerts.AlertError("Error", "Phone Number field cannot be empty");
            registrationFail();
            return;
        }

        if (txtPassword.getText().isEmpty() || txtPassword.getText().trim().isEmpty()) {
            Alerts.AlertError("Error", "Password Field cannot be empty");
            registrationFail();
            return;
        }

        if (txtConfirmPassword.getText().isEmpty() || txtConfirmPassword.getText().trim().isEmpty()) {
            Alerts.AlertError("Error", "Confirm Password Field cannot be empty");
            registrationFail();
            return;
        }

        if (!maleRadioBtn.isSelected()) {
            if (!femaleRadioBtn.isSelected()) {
                Alerts.AlertError("Error", "Gender must be selected");
                registrationFail();
                return;
            }
        }

        if (!Validators.isValidName(txtFirstname.getText())) {
            Alerts.AlertError("Error", "Invalid Firstname");
            registrationFail();
            return;
        }

        if (!Validators.isValidName(txtLastname.getText())) {
            Alerts.AlertError("Error", "Invalid Lastname");
            registrationFail();
            return;
        }

        if (!Validators.isValidEmail(txtEmail.getText())) {
            Alerts.AlertError("Error", "Invalid email address");
            registrationFail();
            return;
        }


        if (!Validators.isValidPassword(txtPassword.getText())) {
            Alerts.AlertError("Error", "Invalid password");
            registrationFail();
            return;
        }

        if (!Validators.isValidNumber(txtPhonenum.getText())) {
            Alerts.AlertError("Error", "Invalid Phone Number");
            registrationFail();
            return;
        }

        if (!Objects.equals(txtPassword.getText(), txtConfirmPassword.getText())) {
            Alerts.AlertError("Error", "Passwords do not match!");
            registrationFail();
            return;
        }

        if (usersDB.userExist(txtEmail.getText()) == 1) {
            Alerts.AlertError("Error", "Email already registered");
            ShowTrayNotification
                    .trayNotification("Account creation unsuccessful", "Email already registered",
                            AnimationType.SLIDE, NotificationType.ERROR);
            return;
        }

        String getGender = null;

        if (maleRadioBtn.isSelected()) getGender = "Male";
        if (femaleRadioBtn.isSelected()) getGender = "Female";

        RecursiveUser user = new RecursiveUser(
                txtFirstname.getText(), txtLastname.getText(), txtEmail.getText(),
                getGender, txtPhonenum.getText(), Hash.createHash(txtPassword.getText())
        );
        if (usersDB.addUser(user) != 1) {
            Alerts.AlertError("Error", "An error occurred");
            registrationFail();
            return;
        }

        resetSignUpProperties();

        Alerts.AlertInfo("Success!!!", "Account was successfully created");

        SceneController.switchScene(Constants.LOGIN_FXML_DIR, mainPane);

        ShowTrayNotification
                .trayNotification("Account creation success", "Account Successfully created, login now",
                        AnimationType.SLIDE, NotificationType.SUCCESS);

    }

    public void switchToLogin() {
        SceneController.switchScene(Constants.LOGIN_FXML_DIR, mainPane);
    }

    private void resetSignUpProperties() {
        txtFirstname.setText("");
        txtLastname.setText("");
        txtEmail.setText("");
        txtPhonenum.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        maleRadioBtn.setSelected(false);
        femaleRadioBtn.setSelected(false);
    }
    private void registrationFail() {
        ShowTrayNotification
                .trayNotification("Account registration fails", "Account creation was unsuccessful",
                        AnimationType.SLIDE, NotificationType.ERROR);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            //setting stage draggable
            StageDraggable.setStageDraggable(mainPane);
    }
}
