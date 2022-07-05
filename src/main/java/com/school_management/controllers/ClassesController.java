package com.school_management.controllers;

import com.school_management.dao.ClassesDB;
import com.school_management.dao.SubjectsDB;
import com.school_management.dao.UsersDB;
import com.school_management.models.Class;
import com.school_management.models.Subject;
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
import javafx.scene.control.ButtonType;
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
import java.util.ResourceBundle;

public class ClassesController implements Initializable {
    @FXML
    private AnchorPane primaryPane;
    @FXML
    private MFXButton btnAddClass, btnEditClass, btnDeleteClass, btnAddSubject, btnEditSubject, btnDeleteSubject;
    @FXML
    private MFXLegacyTableView<Class> classTableView;
    @FXML
    private MFXLegacyTableView<Subject> subjectTableView;
    @FXML
    private TableColumn<Class, String> columnClassID, columnClassName, columnFee, columnSubjectID, columnSubjectName;
    static int selectedClassID = 0;
    static int selectedSubjectID = 0;
    static String selectedClassName = "";
    static String selectedSubjectName = "";
    static String selectedFee = "";
    ObservableList<Class> classes = FXCollections.observableArrayList();
    ObservableList<Subject> subjects = FXCollections.observableArrayList();

    private MFXGenericDialog load_add_class_dialog() {
        BoxBlur blur = new BoxBlur(3.0, 3.0, 3);
        primaryPane.setEffect(blur);

        MFXGenericDialog dialog = new MFXGenericDialog();
        dialog.setHeaderText("Add Class");

        AnchorPane centerPane = new AnchorPane();
        centerPane.setStyle(" -fx-pref-height:142.0; -fx-pref-width:376.0;");

        MFXTextField className = new MFXTextField();
        className.setFloatingText("Class Name");
        className.setFloatMode(FloatMode.BORDER);
        className.setLayoutX(80);
        className.setLayoutY(14);

        MFXTextField classFee = new MFXTextField();
        classFee.setFloatingText("Class Fee");
        classFee.setFloatMode(FloatMode.BORDER);
        classFee.setLayoutX(80);
        classFee.setLayoutY(76);

        className.getStyleClass().add("dial-text-field");
        classFee.getStyleClass().add("dial-text-field");

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
        centerPane.getChildren().addAll(className, classFee);
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
    private MFXGenericDialog load_add_subject_dialog() {
        BoxBlur blur = new BoxBlur(3.0, 3.0, 3);
        primaryPane.setEffect(blur);

        MFXGenericDialog dialog = new MFXGenericDialog();
        dialog.setHeaderText("Add Subject");

        AnchorPane centerPane = new AnchorPane();
        centerPane.setStyle(" -fx-pref-height:148.0; -fx-pref-width:366.0;");

        MFXTextField subjectName = new MFXTextField();
        subjectName.setFloatingText("Subject Name");
        subjectName.setFloatMode(FloatMode.BORDER);
        subjectName.setLayoutX(80);
        subjectName.setLayoutY(45);

        subjectName.getStyleClass().add("dial-text-field");

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
        bottomPane.setStyle("-fx-pref-height: 74.0; -fx-pref-width:366.0;");
        bottomPane.setId("bottomPane");

        saveBtn.setLayoutX(84.0);
        cancelBtn.setLayoutX(207.0);
        saveBtn.setLayoutY(37.0);
        cancelBtn.setLayoutY(37.0);

        dialog.addActions(saveBtn, cancelBtn);
        centerPane.getChildren().addAll(subjectName);
        dialog.setCenter(centerPane);
        bottomPane.getChildren().addAll(saveBtn, cancelBtn);
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

    private MFXGenericDialog load_edit_class_dialog() {
        BoxBlur blur = new BoxBlur(3.0, 3.0, 3);
        primaryPane.setEffect(blur);

        MFXGenericDialog dialog = new MFXGenericDialog();
        dialog.setHeaderText("Edit Class");

        AnchorPane centerPane = new AnchorPane();
        centerPane.setStyle(" -fx-pref-height:142.0; -fx-pref-width:376.0;");

        MFXTextField className = new MFXTextField();
        className.setFloatingText("Class Name");
        className.setText(selectedClassName);
        className.setFloatMode(FloatMode.BORDER);
        className.setLayoutX(80);
        className.setLayoutY(14);

        MFXTextField classFee = new MFXTextField();
        classFee.setFloatingText("Class Fee");
        classFee.setText(selectedFee);
        classFee.setFloatMode(FloatMode.BORDER);
        classFee.setLayoutX(80);
        classFee.setLayoutY(76);

        className.getStyleClass().add("dial-text-field");
        classFee.getStyleClass().add("dial-text-field");

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
        centerPane.getChildren().addAll(className, classFee);
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
    private MFXGenericDialog load_edit_subject_dialog() {
        BoxBlur blur = new BoxBlur(3.0, 3.0, 3);
        primaryPane.setEffect(blur);

        MFXGenericDialog dialog = new MFXGenericDialog();
        dialog.setHeaderText("Edit Subject");

        AnchorPane centerPane = new AnchorPane();
        centerPane.setStyle(" -fx-pref-height:148.0; -fx-pref-width:366.0;");

        MFXTextField subjectName = new MFXTextField();
        subjectName.setFloatingText("Subject Name");
        subjectName.setText(selectedSubjectName);
        subjectName.setFloatMode(FloatMode.BORDER);
        subjectName.setLayoutX(80);
        subjectName.setLayoutY(45);

        subjectName.getStyleClass().add("dial-text-field");

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
        bottomPane.setStyle("-fx-pref-height: 74.0; -fx-pref-width:366.0;");
        bottomPane.setId("bottomPane");

        saveBtn.setLayoutX(84.0);
        cancelBtn.setLayoutX(207.0);
        saveBtn.setLayoutY(37.0);
        cancelBtn.setLayoutY(37.0);

        dialog.addActions(saveBtn, cancelBtn);
        centerPane.getChildren().addAll(subjectName);
        dialog.setCenter(centerPane);
        bottomPane.getChildren().addAll(saveBtn, cancelBtn);
        dialog.setBottom(bottomPane);

        dialog.setStyle("-fx-background-color: #00e7db; -fx-pref-height:212.0; -fx-pref-width:388.0;");
        dialog.setShowAlwaysOnTop(false);
        dialog.setShowMinimize(false);
        dialog.setAlwaysOnTop(true);

        dialog.setOnClose(event1 -> {
            primaryPane.setEffect(null);;
            Animations.slideUp(primaryPane, dialog, -250);
        });

        dialog.setLayoutX(390.0);
        return dialog;
    }
    @FXML
    private void addClass() {
        MFXGenericDialog dialog =  load_add_class_dialog();
        Animations.slideDown(primaryPane,dialog, 250);

        AnchorPane centerPane = (AnchorPane) dialog.getChildren().get(1);
        MFXTextField txtClassName = (MFXTextField) centerPane.getChildren().get(0);
        MFXTextField txtClassFee = (MFXTextField) centerPane.getChildren().get(1);

        AnchorPane bottomPane = (AnchorPane) dialog.getChildren().get(2);
        MFXButton saveBtn = (MFXButton) bottomPane.getChildren().get(0);
        saveBtn.setOnAction(event -> {
            if (txtClassName.getText().isEmpty() || txtClassName.getText().trim().isEmpty()) {
                Alerts.AlertError("Error", "Class Name Field cannot be empty");
                return;
            }

            if (txtClassFee.getText().isEmpty() || txtClassFee.getText().trim().isEmpty()) {
                Alerts.AlertError("Error", "Fee Field cannot be empty");
                return;
            }

            if (Validators.isValidName(txtClassName.getText())) {
                Alerts.AlertError("Error", "Class name must exceed 2 letters");
                return;
            }

            if (!txtClassFee.getText().matches("[0-9]+")) {
                Alerts.AlertError("Error", "Invalid Fee");
                return;
            }

            try {
                if ((ClassesDB.addClass(txtClassName.getText(), txtClassFee.getText())) != 1){
                    Alerts.AlertError("Error", "Something went wrong");
                    txtClassName.setText("");
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            txtClassName.setText("");
            txtClassFee.setText("");
            primaryPane.setEffect(null);
            Animations.slideUp(primaryPane, dialog, -250);
            ShowTrayNotification
                    .trayNotification("Success!!!", "Class added successfully!!!",
                            AnimationType.SLIDE, NotificationType.SUCCESS);

            try {
                refresh();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        });
    }

    @FXML
    private void addSubject() {
        MFXGenericDialog dialog =  load_add_subject_dialog();
        Animations.slideDown(primaryPane,dialog, 250);

        AnchorPane centerPane = (AnchorPane) dialog.getChildren().get(1);
        MFXTextField txtSubjectName = (MFXTextField) centerPane.getChildren().get(0);

        AnchorPane bottomPane = (AnchorPane) dialog.getChildren().get(2);
        MFXButton saveBtn = (MFXButton) bottomPane.getChildren().get(0);
        saveBtn.setOnAction(event -> {
            if (txtSubjectName.getText().isEmpty() || txtSubjectName.getText().trim().isEmpty()) {
                Alerts.AlertError("Error", "Subject Name Field cannot be empty");
                return;
            }

            if (Validators.isValidName(txtSubjectName.getText())) {
                Alerts.AlertError("Error", "Subject name must exceed 2 letters");
                return;
            }

            try {
                if ((SubjectsDB.addSubject(txtSubjectName.getText())) != 1){
                    Alerts.AlertError("Error", "Something went wrong");
                    txtSubjectName.setText("");
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            txtSubjectName.setText("");
            primaryPane.setEffect(null);
            Animations.slideUp(primaryPane, dialog, -250);
            ShowTrayNotification
                    .trayNotification("Success!!!", "Subject added successfully!!!",
                            AnimationType.SLIDE, NotificationType.SUCCESS);

            try {
                refresh();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        });
    }

    @FXML
    private void editClass() {
        if (selectedClassID == 0) {
            Alerts.AlertError("Error", "Please select class from table!");
            return;
        }

        MFXGenericDialog dialog =  load_edit_class_dialog();
        Animations.slideDown(primaryPane,dialog, 250);

        AnchorPane centerPane = (AnchorPane) dialog.getChildren().get(1);
        MFXTextField txtClassName = (MFXTextField) centerPane.getChildren().get(0);
        MFXTextField txtClassFee = (MFXTextField) centerPane.getChildren().get(1);

        AnchorPane bottomPane = (AnchorPane) dialog.getChildren().get(2);
        MFXButton saveBtn = (MFXButton) bottomPane.getChildren().get(0);
        saveBtn.setOnAction(event -> {
            if (txtClassName.getText().isEmpty() || txtClassName.getText().trim().isEmpty()) {
                Alerts.AlertError("Error", "Class Name Field cannot be empty");
                return;
            }

            if (txtClassFee.getText().isEmpty() || txtClassFee.getText().trim().isEmpty()) {
                Alerts.AlertError("Error", "Fee Field cannot be empty");
                return;
            }

            if (Validators.isValidName(txtClassName.getText())) {
                Alerts.AlertError("Error", "Class name must exceed 2 letters");
                return;
            }

            if (txtClassFee.getText().matches("[a-zA-Z]+")) {
                Alerts.AlertError("Error", "Invalid Fee");
                return;
            }

            try {
                if ((ClassesDB.classExist(txtClassName.getText()) == 1) && !txtClassName.getText().equals(selectedClassName)) {
                    Alerts.AlertError("Error", "Class with this name already exists");
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if ((ClassesDB.editClass(selectedClassID, txtClassName.getText(), txtClassFee.getText()) != 1)){
                    Alerts.AlertError("Error", "Something went wrong");
                    ShowTrayNotification
                            .trayNotification("Error", "Class update unsuccessful",
                                    AnimationType.SLIDE, NotificationType.ERROR);
                    txtClassName.setText(selectedClassName);
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            primaryPane.setEffect(null);
            Animations.slideUp(primaryPane, dialog, -250);
            ShowTrayNotification
                    .trayNotification("Success!!!", "Class updated successfully!!!",
                            AnimationType.SLIDE, NotificationType.SUCCESS);

            try {
                refresh();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        });
    }

    @FXML
    private void editSubject() {
        if (selectedSubjectID == 0) {
            Alerts.AlertError("Error", "Please select subject from table!");
            return;
        }

        MFXGenericDialog dialog =  load_edit_subject_dialog();
        Animations.slideDown(primaryPane,dialog, 250);

        AnchorPane centerPane = (AnchorPane) dialog.getChildren().get(1);
        MFXTextField txtSubjectName = (MFXTextField) centerPane.getChildren().get(0);

        AnchorPane bottomPane = (AnchorPane) dialog.getChildren().get(2);
        MFXButton saveBtn = (MFXButton) bottomPane.getChildren().get(0);
        saveBtn.setOnAction(event -> {
            if (txtSubjectName.getText().isEmpty() || txtSubjectName.getText().trim().isEmpty()) {
                Alerts.AlertError("Error", "Subject Name Field cannot be empty");
                return;
            }

            if (Validators.isValidName(txtSubjectName.getText())) {
                Alerts.AlertError("Error", "Subject name must exceed 2 letters");
                return;
            }

            try {
                if ((SubjectsDB.subjectExist(txtSubjectName.getText()) == 1) && !txtSubjectName.getText().equals(selectedSubjectName)) {
                    Alerts.AlertError("Error", "Subject with this name already exists");
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if ((SubjectsDB.editSubject(selectedSubjectID, txtSubjectName.getText()) != 1)){
                    Alerts.AlertError("Error", "Something went wrong");
                    ShowTrayNotification
                            .trayNotification("Error", "Subject update unsuccessful",
                                    AnimationType.SLIDE, NotificationType.ERROR);
                    txtSubjectName.setText(selectedSubjectName);
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            primaryPane.setEffect(null);
            Animations.slideUp(primaryPane, dialog, -250);
            ShowTrayNotification
                    .trayNotification("Success!!!", "Subject updated successfully!!!",
                            AnimationType.SLIDE, NotificationType.SUCCESS);

            try {
                refresh();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        });
    }

    @FXML
    private void deleteClass() throws SQLException {
        if (selectedClassID == 0) {
            Alerts.AlertError("Error", "Please select class from table!");
            return;
        }

        if (Alerts.AlertConfirmation("Delete", "Are you sure you want to delete this class?") == ButtonType.OK) {
            if (ClassesDB.deleteClass(selectedClassID) != 1) {
                Alerts.AlertError("Error", "Something went wrong");
                ShowTrayNotification
                        .trayNotification("Error", "Class delete unsuccessful",
                                AnimationType.SLIDE, NotificationType.ERROR);
                return;
            }

            ShowTrayNotification
                    .trayNotification("Success!!!", "Class deleted successfully!!!",
                            AnimationType.SLIDE, NotificationType.SUCCESS);

            refresh();
        }
    }

    @FXML
    private void deleteSubject() throws SQLException {
        if (selectedSubjectID == 0) {
            Alerts.AlertError("Error", "Please select subject from table!");
            return;
        }

        if (Alerts.AlertConfirmation("Delete", "Are you sure you want to delete this subject?") == ButtonType.OK) {
            if (SubjectsDB.deleteSubject(selectedSubjectID) != 1) {
                Alerts.AlertError("Error", "Something went wrong");
                ShowTrayNotification
                        .trayNotification("Error", "Subject delete unsuccessful",
                                AnimationType.SLIDE, NotificationType.ERROR);
                return;
            }

            ShowTrayNotification
                    .trayNotification("Success!!!", "Subject deleted successfully!!!",
                            AnimationType.SLIDE, NotificationType.SUCCESS);

            refresh();
        }
    }

    @FXML
    private void refresh() throws SQLException {
        classes.clear();
        subjects.clear();
        setClassesTable();
        setSubjectsTable();
        selectedClassID = 0;
        selectedClassName = "";
        selectedSubjectID = 0;
        selectedSubjectName = "";
    }

    private void setClassesTable() throws SQLException {
        ResultSet rs = ClassesDB.getClasses();
        while (rs.next()) {
            Class classItem = new Class();
            classItem.setClassID(rs.getInt(Class.CLASS_ID));
            classItem.setClassName(rs.getString(Class.CLASS_NAME));
            classItem.setClassFee(rs.getString(Class.CLASS_FEE));
            classes.add(classItem);
        }
        classTableView.setItems(classes);

        assert classTableView != null : "No registered class!";
        columnClassID.setCellValueFactory(new PropertyValueFactory<>("classID"));
        columnClassName.setCellValueFactory(new PropertyValueFactory<>("className"));
        columnFee.setCellValueFactory(new PropertyValueFactory<>("classFee"));

        classTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedClassID = newVal.getClassID();
                selectedClassName = newVal.getClassName();
                selectedFee = newVal.getClassFee();
            }
        });
    }

    private void setSubjectsTable() throws SQLException {
        ResultSet rs = SubjectsDB.getSubjects();
        while (rs.next()) {
            Subject subjectItem = new Subject();
            subjectItem.setSubjectID(rs.getInt(Subject.SUBJECT_ID));
            subjectItem.setSubjectName(rs.getString(Subject.SUBJECT_NAME));
            subjects.add(subjectItem);
        }
        subjectTableView.setItems(subjects);

        assert subjectTableView != null : "No registered subject!";
        columnSubjectID.setCellValueFactory(new PropertyValueFactory<>("subjectID"));
        columnSubjectName.setCellValueFactory(new PropertyValueFactory<>("subjectName"));

        subjectTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedSubjectID = newVal.getSubjectID();
                selectedSubjectName = newVal.getSubjectName();
            }
        });
    }

    private void roleAuthentication() {
        if (UsersDB.getUserRole().equals("teacher")) {
            btnAddClass.setVisible(false);
            btnEditClass.setVisible(false);
            btnDeleteClass.setVisible(false);
            btnAddSubject.setVisible(false);
            btnEditSubject.setVisible(false);
            btnDeleteSubject.setVisible(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setClassesTable();
            setSubjectsTable();
            roleAuthentication();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
