package com.school_management.controllers;

import com.school_management.dao.AccountsDB;
import com.school_management.models.Account;
import com.school_management.utils.Alerts;
import com.school_management.utils.Animations;
import com.school_management.utils.ShowTrayNotification;
import com.school_management.utils.Validators;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.effects.DepthLevel;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import tray.animations.AnimationType;
import tray.notification.NotificationType;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class AccountsController implements Initializable {
    @FXML
    private AnchorPane primaryPane;
    @FXML
    private MFXLegacyTableView<Account> tableView;
    @FXML
    private TableColumn<Account, String> columnRecordID, columnEvent, columnDescription, columnAmount, columnDate, columnSession, columnBalance, columnRecordedBy;
    @FXML
    private Label txtBalance;
    ObservableList<Account> records = FXCollections.observableArrayList();

    @FXML
    private void addRecord() {
        MFXGenericDialog dialog = load_add_record_dialog();
        Animations.slideDown(primaryPane,dialog, 250);

        AnchorPane centerPane = (AnchorPane) dialog.getChildren().get(1);
        ChoiceBox<String> events = (ChoiceBox<String>) centerPane.getChildren().get(0);
        MFXTextField description = (MFXTextField) centerPane.getChildren().get(1);
        MFXTextField amount = (MFXTextField) centerPane.getChildren().get(2);

        AnchorPane bottomPane = (AnchorPane) dialog.getChildren().get(2);
        MFXButton saveBtn = (MFXButton) bottomPane.getChildren().get(0);

        saveBtn.setOnAction(event -> {
            if (description.getText().equals("") || amount.getText().equals("")) {
                Alerts.AlertError("Error", "Please fill out each field");
                return;
            }

            if (!Validators.isValidName(description.getText().trim())) {
                Alerts.AlertError("Error", "Invalid description");
                return;
            }

            if (!amount.getText().matches("[0-9]+")) {
                Alerts.AlertError("Error", "Invalid amount");
                return;
            }

            int balance;
            if (events.getValue().equals("debit")) {
                if (Integer.parseInt(amount.getText()) > AccountsDB.currentBalance()) {
                    Alerts.AlertError("Error", "Insufficient account balance");
                    return;
                }
                balance = AccountsDB.currentBalance() - Integer.parseInt(amount.getText());
            } else {
                balance = AccountsDB.currentBalance() + Integer.parseInt(amount.getText());
            }

            if (AccountsDB.addRecord(events.getValue(), description.getText(), amount.getText(), balance) != 1) {
                Alerts.AlertError("Error", "Something went wrong");
                return;
            }

            Alerts.AlertInfo("Success", "Record added successfully!");
            primaryPane.setEffect(null);
            Animations.slideUp(primaryPane, dialog, -250);
            ShowTrayNotification
                    .trayNotification("Success!!!", "Record added successfully!!!",
                            AnimationType.SLIDE, NotificationType.SUCCESS);

            refresh();
        });
    }

    private MFXGenericDialog load_add_record_dialog() {
        BoxBlur blur = new BoxBlur(3.0, 3.0, 3);
        primaryPane.setEffect(blur);

        MFXGenericDialog dialog = new MFXGenericDialog();
        dialog.setHeaderText("Add Record");

        AnchorPane centerPane = new AnchorPane();
        centerPane.setStyle("-fx-pref-height:148.0; -fx-pref-width:443.0;");

        ChoiceBox<String> events = new ChoiceBox<>();
        ObservableList<String> eventTypes = FXCollections.observableArrayList("credit", "debit");
        events.setItems(eventTypes);
        events.setValue("credit");
        events.setLayoutX(81);
        events.setLayoutY(14);

        MFXTextField description = new MFXTextField();
        description.setFloatingText("Description");
        description.setFloatMode(FloatMode.BORDER);
        description.setLayoutX(81);
        description.setLayoutY(64);

        MFXTextField amount = new MFXTextField();
        amount.setFloatingText("Amount");
        amount.setFloatMode(FloatMode.BORDER);
        amount.setLayoutX(81);
        amount.setLayoutY(114);

        events.getStyleClass().add("dial-text-field");
        description.getStyleClass().add("dial-text-field");
        amount.getStyleClass().add("dial-text-field");

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
        centerPane.getChildren().addAll(events, description, amount);
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

    private void setLabel() {
        txtBalance.setText("\u20A6" + AccountsDB.currentBalance());
    }

    private void populateTable(ResultSet rs) throws SQLException {
        while (true) {
            assert rs != null : "No account found";
            if (!rs.next()) break;
            Account record = new Account();
            record.setRecordID(rs.getInt(Account.ACCOUNT_RECORD_ID));
            record.setEvent(rs.getString(Account.ACCOUNT_EVENT));
            record.setDescription(rs.getString(Account.ACCOUNT_EVENT_DESCRIPTION));
            record.setAmount(rs.getString(Account.ACCOUNT_AMOUNT));
            record.setDate(rs.getString(Account.ACCOUNT_EVENT_DATE));
            record.setSession(rs.getString(Account.ACCOUNT_SESSION));
            record.setBalance(rs.getString(Account.ACCOUNT_BALANCE));
            record.setRecordedBy(rs.getInt(Account.RECORDED_BY));
            records.add(record);
        }
        tableView.setItems(records);
    }

    private void setTableView() {
        assert tableView != null : "No account found!";
        columnRecordID.setCellValueFactory(new PropertyValueFactory<>("recordID"));
        columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        columnBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnEvent.setCellValueFactory(new PropertyValueFactory<>("event"));
        columnRecordedBy.setCellValueFactory(new PropertyValueFactory<>("recordedBy"));
        columnSession.setCellValueFactory(new PropertyValueFactory<>("session"));
    }
    
    private void refresh() {
        records.clear();
        try {
            populateTable(Objects.requireNonNull(AccountsDB.getRecords()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setTableView();
        setLabel();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            populateTable(Objects.requireNonNull(AccountsDB.getRecords()));
            setTableView();
            setLabel();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
