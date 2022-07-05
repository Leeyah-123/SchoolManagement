package com.school_management.controllers;

import com.school_management.dao.ClassesDB;
import com.school_management.dao.GeneralDB;
import com.school_management.dao.SubjectsDB;
import com.school_management.dao.UsersDB;
import com.school_management.models.General;
import com.school_management.models.RecursiveUser;
import com.school_management.models.User;
import com.school_management.utils.Alerts;
import com.school_management.utils.Animations;
import com.school_management.utils.ShowTrayNotification;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
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

public class TeachersController implements Initializable {
    @FXML
    private AnchorPane primaryPane;
    @FXML
    private MFXLegacyTableView<RecursiveUser> tableView;
    @FXML
    private MFXLegacyTableView<General> tableView2;
    @FXML
    private TableColumn<RecursiveUser, String> columnStaffID, columnStaffName, columnStaffEmail, columnPhoneNum, columnRole;
    @FXML
    private TableColumn<General, String> columnRecordID, columnTeacherID, columnSubject, columnClass;
    @FXML
    MFXTextField txtSearch, txtSearch2;

    static int selectedID = 0;
    static String selectedName = "";
    static String selectedEmail = "";
    static String selectedRole = "";
    static int selectedRecordID = 0;
    static int selectedTeacherID = 0;
    static String selectedSubject = "";
    static String selectedClass = "";
    ObservableList<RecursiveUser> teachers = FXCollections.observableArrayList();
    ObservableList<General> records = FXCollections.observableArrayList();

    @FXML
    private void search() throws SQLException {
        ResultSet rs = UsersDB.searchUsers(txtSearch.getText());
        teachers.clear();
        assert rs != null : "No results found";
        populateTeacherTable(rs);
    }

    @FXML
    private void searchRecord() throws SQLException {
        if (txtSearch2.getText().equals("")) {
            refresh();
        }
        if (txtSearch2.getText().trim().matches("[0-9]+")) {
            ResultSet rs = GeneralDB.searchRecords(Integer.parseInt(txtSearch2.getText()));
            records.clear();
            assert rs != null : "No record found";
            populateRecordTable(rs);
        }
    }

    @FXML
    private void editRole() {
        if (selectedID == 0) {
            Alerts.AlertError("Error", "Please select staff from table");
            return;
        }
        MFXGenericDialog dialog = load_edit_role_dialog();
        Animations.slideDown(primaryPane,dialog, 250);

        AnchorPane centerPane = (AnchorPane) dialog.getChildren().get(1);
        ChoiceBox<String> txtTeacherRole = (ChoiceBox<String>) centerPane.getChildren().get(2);

        AnchorPane bottomPane = (AnchorPane) dialog.getChildren().get(2);
        MFXButton saveBtn = (MFXButton) bottomPane.getChildren().get(0);

        saveBtn.setOnAction(event -> {
            if (!txtTeacherRole.getValue().equals(selectedRole)) {
                if (Alerts.AlertConfirmation("Confirmation", "Are you sure you want to change the role of this staff member?") == ButtonType.OK) {
                    if ((UsersDB.editUserRole(selectedID, txtTeacherRole.getValue())) != 1){
                        Alerts.AlertError("Error", "Something went wrong");
                        txtTeacherRole.setValue(selectedRole);
                        return;
                    }
                    primaryPane.setEffect(null);
                    Animations.slideUp(primaryPane, dialog, -250);
                    ShowTrayNotification
                            .trayNotification("Success!!!", "Role changed successfully!!!",
                                    AnimationType.SLIDE, NotificationType.SUCCESS);

                    refresh();
                }
            }
        });
    }

    @FXML
    private void addRecord() {
        MFXGenericDialog dialog = load_add_record_dialog();
        Animations.slideDown(primaryPane,dialog, 250);

        AnchorPane centerPane = (AnchorPane) dialog.getChildren().get(1);
        ChoiceBox<String> teachers = (ChoiceBox<String>) centerPane.getChildren().get(0);
        ChoiceBox<String> classes = (ChoiceBox<String>) centerPane.getChildren().get(1);
        ChoiceBox<String> subjects = (ChoiceBox<String>) centerPane.getChildren().get(2);

        AnchorPane bottomPane = (AnchorPane) dialog.getChildren().get(2);
        MFXButton saveBtn = (MFXButton) bottomPane.getChildren().get(0);

        saveBtn.setOnAction(event -> {
            try {
                if (teachers.getValue() == null || classes.getValue() == null || subjects.getValue() == null ) {
                    Alerts.AlertError("Error", "Please fill in each field");
                    return;
                }
                if (GeneralDB.addRecord(Integer.parseInt(teachers.getValue()), classes.getValue(), subjects.getValue()) != 1){
                    Alerts.AlertError("Error", "Something went wrong");
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            primaryPane.setEffect(null);
            Animations.slideUp(primaryPane, dialog, -250);
            ShowTrayNotification
                    .trayNotification("Success!!!", "Record added successfully!!!",
                            AnimationType.SLIDE, NotificationType.SUCCESS);

            refresh();
        });
    }

    @FXML
    private void deleteRecord() throws SQLException {
        if (selectedRecordID == 0) {
            Alerts.AlertError("Error", "Please select record from table");
            return;
        }

        if (Alerts.AlertConfirmation("Confirmation", "Are you sure you want to delete this record?") == ButtonType.OK) {
            if (GeneralDB.deleteRecord(selectedRecordID) != 1) {
                Alerts.AlertError("Error", "Something went wrong");
                return;
            }
            ShowTrayNotification
                    .trayNotification("Success!!!", "Record added successfully!!!",
                            AnimationType.SLIDE, NotificationType.SUCCESS);

            refresh();
        }
    }

    private MFXGenericDialog load_edit_role_dialog() {
        BoxBlur blur = new BoxBlur(3.0, 3.0, 3);
        primaryPane.setEffect(blur);

        MFXGenericDialog dialog = new MFXGenericDialog();
        dialog.setHeaderText("Edit Teacher Role");

        AnchorPane centerPane = new AnchorPane();
        centerPane.setStyle("-fx-pref-height:148.0; -fx-pref-width:443.0;");

        MFXTextField teacherName = new MFXTextField();
        teacherName.setFloatingText("Name");
        teacherName.setFloatMode(FloatMode.BORDER);
        teacherName.setLayoutX(81);
        teacherName.setLayoutY(14);
        teacherName.setText(selectedName);
        teacherName.setEditable(false);

        MFXTextField teacherEmail = new MFXTextField();
        teacherEmail.setFloatingText("Email");
        teacherEmail.setFloatMode(FloatMode.BORDER);
        teacherEmail.setLayoutX(81);
        teacherEmail.setLayoutY(64);
        teacherEmail.setText(selectedEmail);
        teacherEmail.setEditable(false);

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        ObservableList<String> roles = FXCollections.observableArrayList();
        roles.addAll("teacher", "moderator", "admin");
        choiceBox.setItems(roles);
        choiceBox.setValue(selectedRole);
        choiceBox.setLayoutX(81);
        choiceBox.setLayoutY(114);

        teacherName.getStyleClass().add("dial-text-field");
        teacherEmail.getStyleClass().add("dial-text-field");
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
        centerPane.getChildren().addAll(teacherName, teacherEmail, choiceBox);
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

    private MFXGenericDialog load_add_record_dialog() {
        BoxBlur blur = new BoxBlur(3.0, 3.0, 3);
        primaryPane.setEffect(blur);

        MFXGenericDialog dialog = new MFXGenericDialog();
        dialog.setHeaderText("Add Record");

        AnchorPane centerPane = new AnchorPane();
        centerPane.setStyle("-fx-pref-height:148.0; -fx-pref-width:443.0;");

        ChoiceBox<String> teachers = new ChoiceBox<>();
        ObservableList<String> teacherIDS;
        teacherIDS = UsersDB.getTeacherIDS();
        teachers.setItems(teacherIDS);
        teachers.setLayoutX(81);
        teachers.setLayoutY(14);

        ChoiceBox<String> classes = new ChoiceBox<>();
        ObservableList<String> classNames;
        classNames = ClassesDB.getClassNames();
        classes.setItems(classNames);
        classes.setLayoutX(81);
        classes.setLayoutY(64);

        ChoiceBox<String> subjects = new ChoiceBox<>();
        ObservableList<String> subjectNames;
        subjectNames = SubjectsDB.getSubjectNames();
        subjects.setItems(subjectNames);
        subjects.setLayoutX(81);
        subjects.setLayoutY(114);

        teachers.getStyleClass().add("dial-choice-box");
        classes.getStyleClass().add("dial-choice-box");
        subjects.getStyleClass().add("dial-choice-box");

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
        centerPane.getChildren().addAll(teachers, classes, subjects);
        dialog.setCenter(centerPane);
        bottomPane.getChildren().addAll(saveBtn, cancelBtn);
        dialog.setBottom(bottomPane);

        dialog.setStyle("-fx-background-color: #00e7db; -fx-pref-height:249.0; -fx-pref-width:388.0;");
        dialog.setShowAlwaysOnTop(false);
        dialog.setShowMinimize(false);
        dialog.setAlwaysOnTop(true);

        dialog.setOnClose(event1 -> {
            primaryPane.setEffect(null);
            AnchorPane mainPane = (AnchorPane) primaryPane.getParent();
            Animations.slideUp(mainPane, dialog, -250);
        });

        dialog.setLayoutX(390.0);
        return dialog;
    }

    private void refresh() {
        teachers.clear();
        records.clear();
        setTableViews();
        selectedID = 0;
        selectedName = "";
        selectedEmail = "";
        selectedRole = "";
        selectedRecordID = 0;
        selectedTeacherID = 0;
        selectedSubject = "";
        selectedClass = "";
    }

    private void populateTeacherTable(ResultSet rs) throws SQLException {
        while (true) {
            assert rs != null : "No registered teacher";
            if (!rs.next()) break;
            RecursiveUser teacher = new RecursiveUser();
            teacher.setTeacherID(rs.getInt(User.USER_ID));
            teacher.setUsername(rs.getString("username"));
            teacher.setEmail(rs.getString(User.USER_EMAIL));
            teacher.setPhoneNum(rs.getString(User.USER_NUMBER));
            teacher.setRole(rs.getString(User.USER_ROLE));
            teachers.add(teacher);
        }
        tableView.setItems(teachers);
    }
    private void populateRecordTable(ResultSet rs) throws SQLException {
        while (true) {
            assert rs != null : "No saved record";
            if (!rs.next()) break;
            General record = new General();
            record.setRecordID(rs.getInt(General.GENERAL_RECORD_ID));
            record.setTeacherID(rs.getInt(General.GENERAL_TEACHER_ID));
            record.setClassName(rs.getString(General.GENERAL_CLASS_NAME));
            record.setSubjectName(rs.getString(General.GENERAL_SUBJECT_NAME));
            records.add(record);
        }
        tableView2.setItems(records);
    }

    private void setTeacherTableView() {
        assert tableView != null : "No registered teacher!";
        columnStaffID.setCellValueFactory(new PropertyValueFactory<>("teacherID"));
        columnStaffName.setCellValueFactory(new PropertyValueFactory<>("username"));
        columnStaffEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnPhoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        columnRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedID = newVal.getTeacherID();
                selectedName = newVal.getUsername();
                selectedEmail = newVal.getEmail();
                selectedRole = newVal.getRole();
            }
        });
    }

    private void setRecordTableView() {
        assert tableView2 != null: "No record found";
        columnRecordID.setCellValueFactory(new PropertyValueFactory<>("recordID"));
        columnTeacherID.setCellValueFactory(new PropertyValueFactory<>("teacherID"));
        columnClass.setCellValueFactory(new PropertyValueFactory<>("className"));
        columnSubject.setCellValueFactory(new PropertyValueFactory<>("subjectName"));

        tableView2.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedRecordID = newVal.getRecordID();
                selectedTeacherID = newVal.getTeacherID();
                selectedClass = newVal.getClassName();
                selectedSubject = newVal.getSubjectName();
            }
        });
    }

    private void setTableViews() {
        try {
            populateTeacherTable(Objects.requireNonNull(UsersDB.getUsers()));
            setTeacherTableView();
            populateRecordTable(Objects.requireNonNull(GeneralDB.getRecords()));
            setRecordTableView();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTableViews();
    }
}
