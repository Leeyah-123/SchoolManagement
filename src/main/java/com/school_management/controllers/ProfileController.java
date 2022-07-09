package com.school_management.controllers;

import com.school_management.dao.GeneralDB;
import com.school_management.dao.UsersDB;
import com.school_management.io.UserWriter;
import com.school_management.models.General;
import com.school_management.models.User;
import com.school_management.utils.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.effects.DepthLevel;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import tray.animations.AnimationType;
import tray.notification.NotificationType;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private AnchorPane primaryPane;
    @FXML
    private MFXTextField txtFirstname;
    @FXML
    private MFXTextField txtLastname;
    @FXML
    private MFXTextField txtNumber;
    @FXML
    private MFXTextField txtEmail;
    @FXML
    private MFXLegacyComboBox<String> txtGender;
    @FXML
    private MFXButton btnSave;
    @FXML
    private MFXLegacyTableView<General> tableView;
    @FXML
    private TableColumn<General, String> columnClassName;
    @FXML
    private TableColumn<General, String> columnSubject;

    UsersDB usersDB = new UsersDB();
    ObservableList<General> userDetails = FXCollections.observableArrayList();
    static String userEmail = "";

    private MFXGenericDialog load_old_password_dialog() {
        BoxBlur blur = new BoxBlur(3.0, 3.0, 3);
        primaryPane.setEffect(blur);

        MFXGenericDialog dialog = new MFXGenericDialog();
        dialog.setHeaderText("Enter old password");

        AnchorPane centerPane = new AnchorPane();
        centerPane.setStyle(" -fx-pref-height:148.0; -fx-pref-width:366.0;");

        MFXPasswordField txtPword = new MFXPasswordField();
        txtPword.setFloatingText("Old Password");
        txtPword.setFloatMode(FloatMode.BORDER);
        txtPword.setLayoutX(80);
        txtPword.setLayoutY(45);

        txtPword.getStyleClass().add("dial-text-field");

        MFXButton btn = new MFXButton("Ok");
        btn.setRippleAnimateShadow(true);
        btn.setRippleColor(Paint.valueOf("#00e7db"));
        btn.setButtonType(io.github.palexdev.materialfx.enums.ButtonType.RAISED);
        btn.setDepthLevel(DepthLevel.LEVEL4);

        MFXButton cancelBtn = new MFXButton("Cancel");
        cancelBtn.setRippleAnimateShadow(true);
        cancelBtn.setRippleColor(Paint.valueOf("#00e7db"));
        btn.setButtonType(io.github.palexdev.materialfx.enums.ButtonType.RAISED);
        cancelBtn.setDepthLevel(DepthLevel.LEVEL4);

        btn.getStyleClass().add("dial-btn");
        btn.setId("save-btn");

        cancelBtn.getStyleClass().add("dial-btn");

        cancelBtn.setOnAction(event1 -> {
            primaryPane.setEffect(null);
            Animations.slideUp(primaryPane, dialog, -250);
        });

        AnchorPane bottomPane = new AnchorPane();
        bottomPane.setStyle("-fx-pref-height: 74.0; -fx-pref-width:366.0;");
        bottomPane.setId("bottomPane");

        btn.setLayoutX(84.0);
        cancelBtn.setLayoutX(207.0);
        btn.setLayoutY(37.0);
        cancelBtn.setLayoutY(37.0);

        dialog.addActions(btn, cancelBtn);
        centerPane.getChildren().addAll(txtPword);
        dialog.setCenter(centerPane);
        bottomPane.getChildren().addAll(btn, cancelBtn);
        dialog.setBottom(bottomPane);

        dialog.setStyle("-fx-background-color: #00e7db; -fx-pref-height:212.0; -fx-pref-width:388.0;");
        dialog.setShowAlwaysOnTop(false);
        dialog.setShowMinimize(false);
        dialog.setAlwaysOnTop(true);

        dialog.setOnClose(event1 -> {
            primaryPane.setEffect(null);
            Animations.slideUp(primaryPane, dialog, -250);
        });

        dialog.setLayoutX(390.0);
        return dialog;
    }

    private MFXGenericDialog load_new_password_dialog() {
        BoxBlur blur = new BoxBlur(3.0, 3.0, 3);
        primaryPane.setEffect(blur);

        MFXGenericDialog dialog = new MFXGenericDialog();
        dialog.setHeaderText("New Password");

        AnchorPane centerPane = new AnchorPane();
        centerPane.setStyle(" -fx-pref-height:142.0; -fx-pref-width:376.0;");

        MFXPasswordField txtNewPassword = new MFXPasswordField();
        txtNewPassword.setFloatingText("Enter new password");
        txtNewPassword.setFloatMode(FloatMode.BORDER);
        txtNewPassword.setLayoutX(80);
        txtNewPassword.setLayoutY(14);

        MFXPasswordField txtConfirmPassword = new MFXPasswordField();
        txtConfirmPassword.setFloatingText("Confirm Password");
        txtConfirmPassword.setFloatMode(FloatMode.BORDER);
        txtConfirmPassword.setLayoutX(80);
        txtConfirmPassword.setLayoutY(76);

        txtNewPassword.getStyleClass().add("dial-text-field");
        txtConfirmPassword.getStyleClass().add("dial-text-field");

        MFXButton saveBtn = new MFXButton("Save");
        saveBtn.setRippleAnimateShadow(true);
        saveBtn.setRippleColor(Paint.valueOf("#00e7db"));
        saveBtn.setButtonType(io.github.palexdev.materialfx.enums.ButtonType.RAISED);
        saveBtn.setDepthLevel(DepthLevel.LEVEL4);

        MFXButton cancelBtn = new MFXButton("Cancel");
        cancelBtn.setRippleAnimateShadow(true);
        cancelBtn.setRippleColor(Paint.valueOf("#00e7db"));
        saveBtn.setButtonType(io.github.palexdev.materialfx.enums.ButtonType.RAISED);
        cancelBtn.setDepthLevel(DepthLevel.LEVEL4);

        saveBtn.getStyleClass().add("dial-btn");
        saveBtn.setId("save-btn");

        cancelBtn.getStyleClass().add("dial-btn");

        cancelBtn.setOnAction(event1 -> {
            primaryPane.setEffect(null);
            Animations.slideUp(primaryPane, dialog, -250);
        });

        AnchorPane bottomPane = new AnchorPane();
        bottomPane.setStyle("-fx-pref-height: 46.0; -fx-pref-width:376.0;");
        bottomPane.setId("bottomPane");

        saveBtn.setLayoutX(81.0);
        cancelBtn.setLayoutX(204.0);
        saveBtn.setLayoutY(9.0);
        cancelBtn.setLayoutY(9.0);

        dialog.addActions(saveBtn, cancelBtn);
        centerPane.getChildren().addAll(txtNewPassword, txtConfirmPassword);
        dialog.setCenter(centerPane);
        bottomPane.getChildren().addAll(saveBtn, cancelBtn);
        dialog.setBottom(bottomPane);

        dialog.setStyle("-fx-background-color: #00e7db; -fx-pref-height:235.0; -fx-pref-width:401.0;");
        dialog.setShowAlwaysOnTop(false);
        dialog.setShowMinimize(false);
        dialog.setAlwaysOnTop(true);

        dialog.setOnClose(event1 -> {
            primaryPane.setEffect(null);
            Animations.slideUp(primaryPane, dialog, -250);
        });

        dialog.setLayoutX(390.0);
        return dialog;
    }

    @FXML
    private void changePassword() {
        MFXGenericDialog dialog =  load_old_password_dialog();
        Animations.slideDown(primaryPane,dialog, 250);

        AnchorPane centerPane = (AnchorPane) dialog.getChildren().get(1);
        MFXPasswordField txtOldPassword = (MFXPasswordField) centerPane.getChildren().get(0);

        AnchorPane bottomPane = (AnchorPane) dialog.getChildren().get(2);
        MFXButton okBtn = (MFXButton) bottomPane.getChildren().get(0);
        okBtn.setOnAction(event -> {
            try {
                if (!usersDB.authenticate(CurrentUser.getEmail(),txtOldPassword.getText())) {
                    Alerts.AlertError("Error", "Incorrect password");
                    return;
                }

                AnchorPane mainPane = (AnchorPane) primaryPane.getParent();
                mainPane.getChildren().remove(dialog);

                MFXGenericDialog dialog2 =  load_new_password_dialog();
                dialog2.setLayoutY(250);
                mainPane.getChildren().add(dialog2);

                AnchorPane centerPane2 = (AnchorPane) dialog2.getChildren().get(1);
                MFXPasswordField txtNewPassword = (MFXPasswordField) centerPane2.getChildren().get(0);
                MFXPasswordField txtConfirmPassword = (MFXPasswordField) centerPane2.getChildren().get(1);

                AnchorPane bottomPane2 = (AnchorPane) dialog2.getChildren().get(2);
                MFXButton saveBtn = (MFXButton) bottomPane2.getChildren().get(0);

                saveBtn.setOnAction(event2 -> {
                    if (txtNewPassword.getText().equals("")) {
                        Alerts.AlertError("Error", "Password field cannot be empty");
                        return;
                    }

                    if (txtConfirmPassword.getText().equals("")) {
                        Alerts.AlertError("Error", "Confirm Password field cannot be empty");
                        return;
                    }

                    if (!Validators.isValidPassword(txtNewPassword.getText())) {
                        Alerts.AlertError("Invalid Password", "Password must contain at least one uppercase letter, lowercase letter, number and special character, a minimum of 8 characters, maximum of 20");
                        return;
                    }

                    if (!txtNewPassword.getText().equals(txtConfirmPassword.getText())) {
                        Alerts.AlertError("Error", "Passwords do not match");
                        return;
                    }

                    if (Alerts.AlertConfirmation("Confirmation", "Changing your password will require you to login again, are you sure you want to continue?") == ButtonType.OK) {
                        try {
                            if (UsersDB.editPassword(Hash.createHash(txtNewPassword.getText())) != 1) {
                                Alerts.AlertError("Error", "Something went wrong");
                                return;
                            }
                        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                            throw new RuntimeException(e);
                        }

                        txtNewPassword.setText("");
                        Alerts.AlertInfo("Info", "Password changed successfully");
                        primaryPane.setEffect(null);
                        mainPane.getChildren().remove(dialog);
                        ShowTrayNotification
                                .trayNotification("Success!!!", "Password changed successfully!!!",
                                        AnimationType.SLIDE, NotificationType.SUCCESS);
                        logout(mainPane);
                    }
                });

            } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    private void saveDetails() throws SQLException {
        if (txtFirstname.getText().isEmpty() || txtFirstname.getText().trim().isEmpty()) {
            Alerts.AlertError("Error", "First Name Field cannot be empty");
            return;
        }

        if (txtLastname.getText().isEmpty() || txtLastname.getText().trim().isEmpty()) {
            Alerts.AlertError("Error", "Last Name Field cannot be empty");
            return;
        }

        if (txtEmail.getText().isEmpty() || txtEmail.getText().trim().isEmpty()) {
            Alerts.AlertError("Error", "Email Field cannot be empty");
            return;
        }

        if (txtNumber.getText().isEmpty() || txtNumber.getText().trim().isEmpty()) {
            Alerts.AlertError("Error", "Phone Number field cannot be empty");
            return;
        }

        if (!Validators.isValidName(txtFirstname.getText())) {
            Alerts.AlertError("Error", "Invalid Firstname");
            return;
        }

        if (!Validators.isValidName(txtLastname.getText())) {
            Alerts.AlertError("Error", "Invalid Lastname");
            return;
        }

        if (!Validators.isValidEmail(txtEmail.getText())) {
            Alerts.AlertError("Error", "Invalid email address");
            return;
        }

        if (!Validators.isValidNumber(txtNumber.getText())) {
            Alerts.AlertError("Error", "Invalid Phone Number");
            return;
        }

        if (usersDB.userExist(txtEmail.getText()) == 1 && !userEmail.equals(txtEmail.getText())) {
            Alerts.AlertError("Error", "Email already registered");
            return;
        }

        if (!userEmail.equals(txtEmail.getText())) {
            if (Alerts.AlertConfirmation("Confirm", "Changing your email will require you to login again, are you sure you want to continue?") == ButtonType.OK) {
                if (UsersDB.editPersonalDetails(CurrentUser.getUserID(), txtFirstname.getText(), txtLastname.getText(), txtGender.getValue(), txtNumber.getText(), txtEmail.getText()) != 1) {
                    Alerts.AlertError("Error", "Something went wrong");
                    return;
                }
                logout((AnchorPane) primaryPane.getParent());
            }
        } else {
            if (Alerts.AlertConfirmation("Confirm", "Are you sure you want to edit your personal details?") == ButtonType.OK) {
                if (UsersDB.editPersonalDetails(CurrentUser.getUserID(), txtFirstname.getText(), txtLastname.getText(), txtGender.getValue(), txtNumber.getText(), txtEmail.getText()) != 1) {
                    Alerts.AlertError("Error", "Something went wrong");
                }
                Alerts.AlertInfo("Success!!!", "Personal Details updated successfully");
                refresh();
            }
        }

        ShowTrayNotification
                .trayNotification("Update Success", "Details updated successfully",
                        AnimationType.SLIDE, NotificationType.SUCCESS);

        btnSave.setVisible(false);
        txtFirstname.setEditable(false);
        txtLastname.setEditable(false);
        txtNumber.setEditable(false);
        txtGender.setDisable(true);
        txtEmail.setEditable(false);
    }

    @FXML
    private void editDetails() {
        btnSave.setVisible(true);
        txtFirstname.setEditable(true);
        txtLastname.setEditable(true);
        txtNumber.setEditable(true);
        txtGender.setDisable(false);
        txtEmail.setEditable(true);
    }

    private void logout(AnchorPane mainPane) {
        UserWriter.clearCurrentUser();
        Stage stage = (Stage) mainPane.getScene().getWindow();
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.LOGIN_FXML_DIR)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void setTableView(ResultSet rs) throws SQLException {
        while (rs.next()) {
            General general = new General();
            general.setClassName(rs.getString(General.GENERAL_CLASS_NAME));
            general.setSubjectName(rs.getString(General.GENERAL_SUBJECT_NAME));
            userDetails.add(general);
        }
        tableView.setItems(userDetails);

        assert tableView != null;

        columnClassName.setCellValueFactory(new PropertyValueFactory<>("className"));
        columnSubject.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
    }

    private void setUserDetails(ResultSet rs) throws SQLException {
        ObservableList<String> gender = FXCollections.observableArrayList("Male", "Female");
        txtGender.setItems(gender);

        while (rs.next()) {
            txtFirstname.setText(rs.getString(User.USER_FIRST_NAME));
            txtLastname.setText(rs.getString(User.USER_LAST_NAME));
            txtNumber.setText(rs.getString(User.USER_NUMBER));
            txtGender.setValue(rs.getString(User.USER_GENDER));
            String email = rs.getString(User.USER_EMAIL);
            txtEmail.setText(email);
            userEmail = email;
        }
    }

    private void refresh() throws SQLException {
        setUserDetails(UsersDB.getUserDetails());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setTableView(GeneralDB.getTeacherStatistics(CurrentUser.getUserID()));
            setUserDetails(UsersDB.getUserDetails());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
