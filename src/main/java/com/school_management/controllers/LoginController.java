package com.school_management.controllers;

import com.school_management.dao.UsersDB;
import com.school_management.io.UserWriter;
import com.school_management.utils.*;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.codec.DecoderException;
import tray.animations.AnimationType;
import tray.notification.NotificationType;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private AnchorPane mainPane;
    @FXML
    private MFXTextField txtEmail;
    @FXML
    private MFXPasswordField txtPassword;
    @FXML
    private MFXCheckbox rememberMe;
    UsersDB usersDB = new UsersDB();

    public void login() throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException, DecoderException {
        if (txtEmail.getText().isEmpty() || txtEmail.getText().trim().isEmpty()) {
            Alerts.AlertError("Error", "Email Field cannot be empty");
            loginFail();
            return;
        }

        if (!Validators.isValidEmail(txtEmail.getText())) {
            Alerts.AlertError("Error", "Invalid email address");
            loginFail();
            return;
        }

        if (!usersDB.authenticate(txtEmail.getText(),txtPassword.getText())) {
            Alerts.AlertError("Error", "Email or password incorrect");
            return;
        }

        if (usersDB.isSuspended(txtEmail.getText())) {
            Alerts.AlertError("Access denied", "Sorry, your account has been suspended, please contact admin");
            return;
        }

        // writing user into file
        UserWriter.encodeUserToFile(txtEmail.getText(), txtPassword.getText(), rememberMe.isSelected());

        String username = CurrentUser.currentUsername();
        resetLoginProperties();
        ShowTrayNotification
                .trayNotification("Login success", "Welcome back " + username,
                        AnimationType.SLIDE, NotificationType.SUCCESS);

       SceneController.switchScene(mainPane);
    }

    public void switchToSignup() {
        SceneController.switchScene(Constants.SIGNUP_FXML_DIR, mainPane);
    }

    private void resetLoginProperties() {
        txtEmail.setText("");
        txtPassword.setText("");
    }

    private void loginFail() {
        ShowTrayNotification
                .trayNotification("Login failed", "Invalid email or password",
                        AnimationType.SLIDE, NotificationType.ERROR);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // set stage Draggable
        StageDraggable.setStageDraggable(mainPane);
    }
}
