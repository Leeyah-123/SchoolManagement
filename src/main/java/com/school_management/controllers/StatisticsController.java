package com.school_management.controllers;

import com.school_management.dao.ClassesDB;
import com.school_management.dao.SessionDB;
import com.school_management.dao.StudentsDB;
import com.school_management.dao.UsersDB;
import com.school_management.models.Class;
import com.school_management.models.RecursiveUser;
import com.school_management.models.Session;
import com.school_management.models.User;
import com.school_management.utils.Alerts;
import com.school_management.utils.Animations;
import com.school_management.utils.Constants;
import com.school_management.utils.ShowTrayNotification;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyListView;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.effects.DepthLevel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import tray.animations.AnimationType;
import tray.notification.NotificationType;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class StatisticsController implements Initializable {
    @FXML
    private AnchorPane primaryPane;
    @FXML
    private Label lblSession, numStudents, numMales, numFemales;
    @FXML
    private MFXTextField txtSearch, txtStartDate, txtEndDate;
    @FXML
    private ImageView suspendIcon;
    @FXML
    private MFXButton btnSuspend;
    @FXML
    private MFXLegacyListView<String> listClasses;
    @FXML
    private MFXLegacyTableView<RecursiveUser> tableView;
    @FXML
    private TableColumn<RecursiveUser, String> colUserName, colEmail, colSuspended;
    ObservableList<RecursiveUser> users = FXCollections.observableArrayList();

    static String selectedEmail = "";
    static String userSuspended = "";

    @FXML
    private void search() throws SQLException {
        ResultSet rs = UsersDB.searchUsers(txtSearch.getText());
        users.clear();
        assert rs != null : "No results found";
        populateTable(rs);
    }

    @FXML
    private void editSession() throws SQLException {
        MFXGenericDialog dialog =  load_edit_session_dialog();
        Animations.slideDown(primaryPane,dialog, 250);

        AnchorPane centerPane = (AnchorPane) dialog.getChildren().get(1);
        DatePicker startDate = (DatePicker) centerPane.getChildren().get(0);
        DatePicker endDate = (DatePicker) centerPane.getChildren().get(1);

        AnchorPane bottomPane = (AnchorPane) dialog.getChildren().get(2);
        MFXButton saveBtn = (MFXButton) bottomPane.getChildren().get(0);

        saveBtn.setOnAction(event -> {
            if (Alerts.AlertConfirmation("Confirmation", "Are you sure you want to edit the current session details?") == ButtonType.OK) {
                if (SessionDB.editSessionDetails(startDate.getValue(), endDate.getValue()) != 1) {
                    Alerts.AlertError("Error", "Something went wrong");
                    return;
                }

                Alerts.AlertInfo("Success", "Session details updated successfully!");
                primaryPane.setEffect(null);
                Animations.slideUp(primaryPane, dialog, -250);
                ShowTrayNotification
                        .trayNotification("Success!!!", "Session details updated successfully!!!",
                                AnimationType.SLIDE, NotificationType.SUCCESS);

                try {
                    refresh();
                    setLabels();
                    setInputFields();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @FXML
    private void suspend() throws SQLException {
        if (selectedEmail.equals("")) {
            Alerts.AlertError("Error", "Please select user from table!");
            return;
        }
        if (userSuspended.equals("false")) {
            if (Alerts.AlertConfirmation("Confirmation", "Are you sure you want to suspend this user?") == ButtonType.OK) {
                if (UsersDB.suspend("true", selectedEmail) != 1) {
                    Alerts.AlertError("Error", "Something went wrong");
                    return;
                }
                Alerts.AlertInfo("Success", "User suspended successfully");
                ShowTrayNotification
                        .trayNotification("Success!!!", "User suspended successfully!!!",
                                AnimationType.SLIDE, NotificationType.SUCCESS);
            }
        } else {
            if (Alerts.AlertConfirmation("Confirmation", "Are you sure you want to unsuspend this user?") == ButtonType.OK) {
                if (UsersDB.suspend("false", selectedEmail) != 1) {
                    Alerts.AlertError("Error", "Something went wrong");
                    return;
                }
                Alerts.AlertInfo("Success", "User unsuspended successfully");
                ShowTrayNotification
                        .trayNotification("Success!!!", "User unsuspended successfully!!!",
                                AnimationType.SLIDE, NotificationType.SUCCESS);
            }        }
        refresh();
    }

    private MFXGenericDialog load_edit_session_dialog() throws SQLException {
        ResultSet rs = SessionDB.getSessionDetails();
        String start = "";
        String end = "";

        while (rs.next()) {
            start = rs.getString(Session.START_DATE);
            end = rs.getString(Session.END_DATE);
        }

        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        BoxBlur blur = new BoxBlur(3.0, 3.0, 3);
        primaryPane.setEffect(blur);

        MFXGenericDialog dialog = new MFXGenericDialog();
        dialog.setHeaderText("Edit Session Details");

        AnchorPane centerPane = new AnchorPane();
        centerPane.setStyle(" -fx-pref-height:142.0; -fx-pref-width:376.0;");

        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setEditable(false);
        startDatePicker.setPromptText("Start Date");
        startDatePicker.setValue(startDate);
        startDatePicker.setLayoutX(80);
        startDatePicker.setLayoutY(14);

        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setEditable(false);
        endDatePicker.setPromptText("End Date");
        endDatePicker.setValue(endDate);
        endDatePicker.setLayoutX(80);
        endDatePicker.setLayoutY(76);

        startDatePicker.getStyleClass().add("dial-date-picker");
        endDatePicker.getStyleClass().add("dial-date-picker");

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
        centerPane.getChildren().addAll(startDatePicker, endDatePicker);
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

    private void refresh() throws SQLException {
        setLabels();
        txtSearch.setText("");
        users.clear();
        populateTable(Objects.requireNonNull(UsersDB.getUsers()));
    }

    private void setTableView() {
        assert tableView != null : "No registered user!";
        colUserName.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colSuspended.setCellValueFactory(new PropertyValueFactory<>("suspended"));

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
           if (newVal != null) {
               selectedEmail = newVal.getEmail();
               userSuspended = newVal.getSuspended();
               if (userSuspended.equals("true")) {
                   btnSuspend.setText("Unsuspend");
                   Image image = new Image(Constants.UNSUSPEND_ICON);
                   suspendIcon.setImage(image);
               } else {
                   btnSuspend.setText("Suspend");
                   Image image = new Image(Constants.SUSPEND_ICON);
                   suspendIcon.setImage(image);
               }
           }
        });
    }

    private void setListView() throws SQLException {
        ResultSet rs = ClassesDB.getClasses();
        ObservableList<String> classes = FXCollections.observableArrayList();
        while (rs.next()) {
            String className = rs.getString(Class.CLASS_NAME);
            classes.add(className);
        }
        listClasses.setItems(classes);
    }

    private void setLabels() throws SQLException {
        lblSession.setText(SessionDB.sessionString());
        numStudents.setText(StudentsDB.countStudents());
        numMales.setText(StudentsDB.countGender("male"));
        numFemales.setText(StudentsDB.countGender("female"));
    }

    private void setInputFields() throws SQLException {
        ResultSet rs = SessionDB.getSessionDetails();
        String start = "";
        String end = "";
        while (rs.next()) {
            start = rs.getString(Session.START_DATE);
            end = rs.getString(Session.END_DATE);
        }
        txtStartDate.setText(start);
        txtEndDate.setText(end);
    }

    private void populateTable(ResultSet rs) throws SQLException {
        while (true) {
            assert rs != null;
            if (!rs.next()) break;
            RecursiveUser user = new RecursiveUser();
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString(User.USER_EMAIL));
            user.setSuspended(rs.getString(User.USER_SUSPENDED));
            users.add(user);
        }
        tableView.setItems(users);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setLabels();
            setListView();
            populateTable(Objects.requireNonNull(UsersDB.getUsers()));
            setTableView();
            setInputFields();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
