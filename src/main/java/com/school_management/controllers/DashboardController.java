package com.school_management.controllers;

import com.school_management.dao.GeneralDB;
import com.school_management.dao.RemindersDB;
import com.school_management.dao.SessionDB;
import com.school_management.models.Reminder;
import com.school_management.utils.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyListView;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.effects.DepthLevel;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import org.apache.commons.codec.DecoderException;
import tray.animations.AnimationType;
import tray.notification.NotificationType;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private AnchorPane primaryPane;
    @FXML
    private ImageView imageView;
    @FXML
    private Label lblName, lblSession, numClasses,lblReminderTitle, lblReminderStatus;
    @FXML
    private MFXButton btnProfile;
    @FXML
    private TextArea txtReminderContent;
    @FXML
    private MFXLegacyListView<String> listReminders, listClasses;
    @FXML
    private ProgressIndicator sessionProgress;

    int loggedUserID = CurrentUser.getUserID();
    static int selectedReminderID = 0;
    static String selectedTitle = "";
    ObservableList<String> titles = FXCollections.observableArrayList();
    ObservableList<String> classes = FXCollections.observableArrayList();


    public DashboardController() throws SQLException {

    }

    private MFXGenericDialog load_add_reminder_dialog() {
        BoxBlur blur = new BoxBlur(3.0, 3.0, 3);
        primaryPane.setEffect(blur);

        MFXGenericDialog dialog = new MFXGenericDialog();
        dialog.setHeaderText("Add Reminder");

        AnchorPane centerPane = new AnchorPane();
        centerPane.setStyle(" -fx-pref-height:148.0; -fx-pref-width:443.0;");

        MFXTextField reminderTitle = new MFXTextField();
        reminderTitle.setFloatingText("Title");
        reminderTitle.setFloatMode(FloatMode.BORDER);
        reminderTitle.setLayoutX(81);
        reminderTitle.setLayoutY(14);

        MFXTextField reminderContent = new MFXTextField();
        reminderContent.setFloatingText("Description");
        reminderContent.setFloatMode(FloatMode.BORDER);
        reminderContent.setLayoutX(81);
        reminderContent.setLayoutY(64);

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        ObservableList<String> statuses = FXCollections.observableArrayList();
        statuses.addAll("Not Done", "In Progress", "Done");
        choiceBox.setItems(statuses);
        choiceBox.setValue("Not Done");
        choiceBox.setLayoutX(81);
        choiceBox.setLayoutY(114);

        reminderTitle.getStyleClass().add("dial-text-field");
        reminderContent.getStyleClass().add("dial-text-field");
        choiceBox.getStyleClass().add("dial-choice-box");

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
        bottomPane.setStyle("-fx-pref-height: 52.0; -fx-pref-width:443.0;");
        bottomPane.setId("bottomPane");

        saveBtn.setLayoutX(83.0);
        cancelBtn.setLayoutX(209.0);
        saveBtn.setLayoutY(13.0);
        cancelBtn.setLayoutY(13.0);

        dialog.addActions(saveBtn, cancelBtn);
        centerPane.getChildren().addAll(reminderTitle, reminderContent, choiceBox);
        dialog.setCenter(centerPane);
        bottomPane.getChildren().addAll(saveBtn, cancelBtn);
        dialog.setBottom(bottomPane);

        dialog.setStyle("-fx-background-color: #00e7db; -fx-pref-height:249.0; -fx-pref-width:388.0;");
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

    private MFXGenericDialog load_edit_reminder_dialog() throws SQLException {
        BoxBlur blur = new BoxBlur(3.0, 3.0, 3);
        primaryPane.setEffect(blur);

        MFXGenericDialog dialog = new MFXGenericDialog();
        dialog.setHeaderText("Edit Reminder");

        AnchorPane centerPane = new AnchorPane();
        centerPane.setStyle(" -fx-pref-height:148.0; -fx-pref-width:443.0;");

        Reminder reminder = RemindersDB.getReminderDetails(selectedTitle);

        MFXTextField reminderTitle = new MFXTextField();
        reminderTitle.setFloatingText("Title");
        reminderTitle.setText(reminder.getReminderTitle());
        reminderTitle.setFloatMode(FloatMode.BORDER);
        reminderTitle.setLayoutX(81);
        reminderTitle.setLayoutY(14);

        MFXTextField reminderContent = new MFXTextField();
        reminderContent.setFloatingText("Description");
        reminderContent.setText(reminder.getReminderContent());
        reminderContent.setFloatMode(FloatMode.BORDER);
        reminderContent.setLayoutX(81);
        reminderContent.setLayoutY(64);

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        ObservableList<String> statuses = FXCollections.observableArrayList();
        statuses.addAll("Not Done", "In Progress", "Done");
        choiceBox.setItems(statuses);
        choiceBox.setValue(reminder.getReminderStatus());
        choiceBox.setLayoutX(81);
        choiceBox.setLayoutY(114);

        reminderTitle.getStyleClass().add("dial-text-field");
        reminderContent.getStyleClass().add("dial-text-field");
        choiceBox.getStyleClass().add("dial-choice-box");

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
        bottomPane.setStyle("-fx-pref-height: 52.0; -fx-pref-width:443.0;");
        bottomPane.setId("bottomPane");

        saveBtn.setLayoutX(83.0);
        cancelBtn.setLayoutX(209.0);
        saveBtn.setLayoutY(13.0);
        cancelBtn.setLayoutY(13.0);

        dialog.addActions(saveBtn, cancelBtn);
        centerPane.getChildren().addAll(reminderTitle, reminderContent, choiceBox);
        dialog.setCenter(centerPane);
        bottomPane.getChildren().addAll(saveBtn, cancelBtn);
        dialog.setBottom(bottomPane);

        dialog.setStyle("-fx-background-color: #00e7db; -fx-pref-height:249.0; -fx-pref-width:388.0;");
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
    private void addReminder() {
        MFXGenericDialog dialog = load_add_reminder_dialog();
        Animations.slideDown(primaryPane,dialog, 250);
        AnchorPane centerPane = (AnchorPane) dialog.getChildren().get(1);
        MFXTextField txtTitle = (MFXTextField) centerPane.getChildren().get(0);
        MFXTextField txtContent = (MFXTextField) centerPane.getChildren().get(1);
        ChoiceBox<String> status = (ChoiceBox<String>) centerPane.getChildren().get(2);

        AnchorPane bottomPane = (AnchorPane) dialog.getChildren().get(2);
        MFXButton saveBtn = (MFXButton) bottomPane.getChildren().get(0);
        saveBtn.setOnAction(event -> {
            if (txtTitle.getText().isEmpty() || txtTitle.getText().trim().isEmpty()) {
                Alerts.AlertError("Error", "Title Field cannot be empty");
                return;
            }

            if (txtContent.getText().isEmpty() || txtContent.getText().trim().isEmpty()) {
                Alerts.AlertError("Error", "Content Field cannot be empty");
                return;
            }

            try {
                if (RemindersDB.titleExist(txtTitle.getText()) == 1) {
                    Alerts.AlertError("Error", "Reminder with this title already exists");
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if ((RemindersDB.addReminder(loggedUserID, txtTitle.getText(), txtContent.getText(), status.getValue()) != 1)){
                    Alerts.AlertError("Error", "Something went wrong");
                    txtTitle.setText("");
                    txtContent.setText("");
                    status.setValue("Not Done");
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            txtTitle.setText("");
            txtContent.setText("");
            status.setValue("Not Done");
            primaryPane.setEffect(null);
            Animations.slideUp(primaryPane, dialog, -250);
            ShowTrayNotification
                    .trayNotification("Success!!!", "Reminder added successfully!!!",
                            AnimationType.SLIDE, NotificationType.SUCCESS);
            try {
                refreshReminder();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        });
    }
    @FXML
    private void editReminder() throws SQLException {
        if (selectedReminderID == 0) {
            Alerts.AlertError("Error", "Please select reminder from list!");
            return;
        }

        MFXGenericDialog dialog =  load_edit_reminder_dialog();
        Animations.slideDown(primaryPane,dialog, 250);

        AnchorPane centerPane = (AnchorPane) dialog.getChildren().get(1);
        MFXTextField txtTitle = (MFXTextField) centerPane.getChildren().get(0);
        MFXTextField txtContent = (MFXTextField) centerPane.getChildren().get(1);
        ChoiceBox<String> status = (ChoiceBox<String>) centerPane.getChildren().get(2);

        AnchorPane bottomPane = (AnchorPane) dialog.getChildren().get(2);
        MFXButton saveBtn = (MFXButton) bottomPane.getChildren().get(0);
        saveBtn.setOnAction(event -> {
            if (txtTitle.getText().isEmpty() || txtTitle.getText().trim().isEmpty()) {
                Alerts.AlertError("Error", "Title Field cannot be empty");
                return;
            }

            if (txtContent.getText().isEmpty() || txtContent.getText().trim().isEmpty()) {
                Alerts.AlertError("Error", "Content Field cannot be empty");
                return;
            }

            try {
                if (RemindersDB.titleExist(txtTitle.getText()) == 1 && !txtTitle.getText().equals(selectedTitle)) {
                    Alerts.AlertError("Error", "Reminder with this title already exists");
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            Reminder reminder;
            try {
                reminder = RemindersDB.getReminderDetails(selectedTitle);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if ((RemindersDB.editReminder(txtTitle.getText(), txtContent.getText(), status.getValue(), selectedReminderID) != 1)){
                    Alerts.AlertError("Error", "Something went wrong");
                    txtTitle.setText(reminder.getReminderTitle());
                    txtContent.setText(reminder.getReminderContent());
                    status.setValue(reminder.getReminderStatus());
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            primaryPane.setEffect(null);
            Animations.slideUp(primaryPane, dialog, -250);
            ShowTrayNotification
                    .trayNotification("Success!!!", "Reminder updated successfully!!!",
                            AnimationType.SLIDE, NotificationType.SUCCESS);
            try {
                refreshReminder();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        });
    }
    @FXML
    private void deleteReminder() throws SQLException{
        if (selectedReminderID == 0){
            Alerts.AlertError("Error", "Please select reminder from list!");
            return;
        }
        if (Alerts.AlertConfirmation("Delete", "Are you sure you want to delete this reminder?") == ButtonType.OK) {
            RemindersDB.deleteReminder(selectedReminderID);
            Alerts.AlertInfo("Success", "Delete Successful");
            ShowTrayNotification
                    .trayNotification("Success", "Reminder deleted successfully",
                            AnimationType.SLIDE, NotificationType.SUCCESS);
            refreshReminder();
        }
    }
    @FXML
    private void refreshReminder() throws SQLException {
        titles.clear();
        titles = RemindersDB.getReminderTitle(loggedUserID);
        listReminders.setItems(titles);
        listReminders.getSelectionModel().clearSelection();
        lblReminderTitle.setText("");
        lblReminderStatus.setText("");
        txtReminderContent.setText("");
        selectedReminderID = 0;
        selectedTitle = "";
    }
    private void setLabels() throws DecoderException, SQLException {
        numClasses.setText(String.valueOf(GeneralDB.getClassNum(loggedUserID)));
        lblName.setText(CurrentUser.currentUsername());
        lblSession.setText(SessionDB.sessionString());
    }

    private void setImage() throws SQLException {
        if (CurrentUser.getUserGender().equals("Male")) {
            Image image = new Image(Constants.MALE_PROFILE);
            imageView.setImage(image);
        } else {
            Image image = new Image(Constants.FEMALE_PROFILE);
            imageView.setImage(image);
        }
    }

    private void setClassList() throws SQLException {
        classes = GeneralDB.getClasses(loggedUserID);
        listClasses.setItems(classes);
    }

    private void setReminderList() throws SQLException {
        titles = RemindersDB.getReminderTitle(loggedUserID);
        listReminders.setItems(titles);
        listReminders.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Reminder reminder;
                try {
                    reminder = RemindersDB.getReminderDetails(newVal);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                lblReminderTitle.setText(reminder.getReminderTitle());
                selectedTitle = reminder.getReminderTitle();
                txtReminderContent.setText(reminder.getReminderContent());
                lblReminderStatus.setText(reminder.getReminderStatus());
                selectedReminderID = reminder.getReminderID();
            }
        });
    }

    private void setListViews() throws SQLException {
        setReminderList();
        setClassList();
    }

    private void setSessionProgress() throws SQLException {
        sessionProgress.setProgress(SessionDB.SessionProgress());
    }

    public void viewProfile() {
        AnchorPane mainPane = (AnchorPane) ((btnProfile.getParent()).getParent());
        SceneController.switchScene(mainPane, Constants.PROFILE_FXML_DIR);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setImage();
            setListViews();
            SessionDB.sessionString();
            setSessionProgress();
            setLabels();
        } catch (SQLException | DecoderException e) {
            throw new RuntimeException(e);
        }
    }
}
