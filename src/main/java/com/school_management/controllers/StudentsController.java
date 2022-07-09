package com.school_management.controllers;

import com.school_management.dao.AccountsDB;
import com.school_management.dao.ClassesDB;
import com.school_management.dao.StudentsDB;
import com.school_management.models.Class;
import com.school_management.models.Student;
import com.school_management.utils.Alerts;
import com.school_management.utils.Animations;
import com.school_management.utils.ShowTrayNotification;
import com.school_management.utils.Validators;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXRadioButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import tray.animations.AnimationType;
import tray.notification.NotificationType;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StudentsController implements Initializable {
    @FXML
    private TableColumn<Student, String> columnAmountPaid, columnBalance, columnClass, columnFirstName, columnGender, columnEmail, columnLastName, columnMobileNumber, columnRegDate, columnStudentID, columnUpdatedAt;
    @FXML
    private AnchorPane primaryPane;
    @FXML
    private MFXLegacyTableView<Student> tableView;
    @FXML
    private MFXTextField txtSearch;

    ObservableList<Student> students = FXCollections.observableArrayList();

    static int selectedID = 0;
    static String selectedAmount = "";
    static String selectedBalance = "";
    static String selectedClass = "";
    static String selectedFirstName = "";
    static String selectedGender = "";
    static String selectedLastName = "";
    static String selectedNumber = "";
    static String selectedRegDate = "";
    static String selectedEmail = "";

    private void refresh() throws SQLException {
        students.clear();
        selectedID = 0;
        populateTable(StudentsDB.getStudents());
        setTableView();
    }

    @FXML
    private void addStudent() throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StudentsController.class.getResource("/com/school_management/fxml/add_student_dialog.fxml"));
        MFXGenericDialog dialog = loader.load();

        BoxBlur blur = new BoxBlur(3.0, 3.0, 3);
        AnchorPane mainPane = (AnchorPane) primaryPane.getParent();
        mainPane.getChildren().get(0).setEffect(blur);
        primaryPane.setEffect(blur);
        Animations.slideDown(primaryPane,dialog, 100);

        dialog.setOnClose(event -> {
            mainPane.getChildren().get(0).setEffect(null);
            primaryPane.setEffect(null);
            Animations.slideUp(primaryPane, dialog, -250);
        });

        AnchorPane centerPane = (AnchorPane) dialog.getChildren().get(1);
        AnchorPane bottomPane = (AnchorPane) dialog.getChildren().get(2);

        AnchorPane pane1 = (AnchorPane) centerPane.getChildren().get(0);
        AnchorPane pane2 = (AnchorPane) centerPane.getChildren().get(2);

        MFXTextField txtFirstname = (MFXTextField) pane1.getChildren().get(0);
        MFXTextField txtLastname = (MFXTextField) pane1.getChildren().get(1);
        MFXTextField txtMobileNumber = (MFXTextField) pane1.getChildren().get(2);
        MFXTextField txtEmail = (MFXTextField) pane1.getChildren().get(3);
        MFXRadioButton maleBtn = (MFXRadioButton) pane1.lookup("#maleRadioBtn");
        MFXRadioButton femaleBtn = (MFXRadioButton) pane1.lookup("#femaleRadioBtn");

        ChoiceBox<String> choiceBox = (ChoiceBox<String>) pane2.getChildren().get(0);
        AnchorPane amountPane = (AnchorPane) pane2.getChildren().get(1);
        MFXTextField txtBalance = (MFXTextField) pane2.getChildren().get(2);

        AnchorPane pane = (AnchorPane) amountPane.getChildren().get(0);
        Label txtAmountPayable = (Label) pane.lookup("#txtAmountPayable");

        MFXTextField txtAmount = (MFXTextField) amountPane.getChildren().get(1);

        ResultSet rs = ClassesDB.getClasses();
        ObservableList<String> classes = FXCollections.observableArrayList();

        while (rs.next()) {
            String className = rs.getString(Class.CLASS_NAME);
            classes.add(className);
        }
        choiceBox.setItems(classes);

        MFXButton saveBtn = (MFXButton) bottomPane.lookup("#saveBtn");
        MFXButton cancelBtn = (MFXButton) bottomPane.lookup("#cancelBtn");

        choiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    txtAmountPayable.setText(ClassesDB.getFee(choiceBox.getValue()));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if (txtAmountPayable.getText().equals("")) return;
                if (txtAmount.getText().equals("")) {
                    txtBalance.setText("");
                    return;
                }
                if (!txtAmount.getText().matches("[0-9]+")) {
                    Alerts.AlertError("Error", "Invalid Amount");
                    return;
                }
                if ((Integer.parseInt(AccountsDB.stripMoney(txtAmountPayable.getText())) < Integer.parseInt(txtAmount.getText()))) {
                    txtBalance.setText("");
                    Alerts.AlertError("Error", "Amount cannot exceed " + txtAmountPayable.getText());
                    return;
                }
                txtBalance.setText("\u20A6" + (Integer.parseInt(AccountsDB.stripMoney(txtAmountPayable.getText())) - Integer.parseInt(txtAmount.getText())));
            }
        });

        try {
            txtAmount.setOnKeyReleased(event -> {
                if (txtAmountPayable.getText().equals("")) return;
                if (txtAmount.getText().equals("")) {
                    txtBalance.setText("");
                    return;
                }
                if (!txtAmount.getText().matches("[0-9]+")) {
                    Alerts.AlertError("Error", "Invalid Amount");
                    return;
                }
                if ((Integer.parseInt(AccountsDB.stripMoney(txtAmountPayable.getText())) < Integer.parseInt(txtAmount.getText()))) {
                    txtBalance.setText("");
                    Alerts.AlertError("Error", "Amount cannot exceed " + txtAmountPayable.getText());
                    return;
                }
                txtBalance.setText("\u20A6" + (Integer.parseInt(AccountsDB.stripMoney(txtAmountPayable.getText())) - Integer.parseInt(txtAmount.getText())));
            });
        } catch (NumberFormatException e) {
            Alerts.AlertError("Error", "Invalid Amount");
        }

        saveBtn.setOnAction(event -> {
            if (txtFirstname.getText().equals("") || txtFirstname.getText().trim().equals("")) {
                Alerts.AlertError("Error", "Firstname field cannot be empty");
                return;
            }

            if (txtLastname.getText().equals("") || txtLastname.getText().trim().equals("")) {
                Alerts.AlertError("Error", "Lastname field cannot be empty");
                return;
            }

            if (txtMobileNumber.getText().equals("") || txtMobileNumber.getText().trim().equals("")) {
                Alerts.AlertError("Error", "Mobile number field cannot be empty");
                return;
            }

            if (txtEmail.getText().equals("") || txtEmail.getText().trim().equals("")) {
                Alerts.AlertError("Error", "Email field cannot be empty");
                return;
            }

            if (!maleBtn.isSelected() && !femaleBtn.isSelected()) {
                Alerts.AlertError("Error", "Gender must be selected");
                return;
            }

            if (choiceBox.getValue() == null) {
                Alerts.AlertError("Error", "Class must be selected");
                return;
            }

            if (txtAmount.getText().equals("")) {
                Alerts.AlertError("Error", "Please enter amount paid");
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

            if (!Validators.isValidNumber(txtMobileNumber.getText())) {
                Alerts.AlertError("Error", "Invalid Mobile Number");
                return;
            }

            if (!Validators.isValidEmail(txtEmail.getText())) {
                Alerts.AlertError("Error", "Invalid Email");
                return;
            }

            if (txtBalance.getText().equals("")) {
                Alerts.AlertError("Error", "Please enter valid amount");
                return;
            }

            if (StudentsDB.studentExists(txtEmail.getText()) == 1) {
                Alerts.AlertError("Error", "Student with this email already exists");
                return;
            }

            String gender;
            if (maleBtn.isSelected()) gender = "male";
            else gender = "female";

            try {
                if (StudentsDB.addStudent(txtFirstname.getText(), txtLastname.getText(), gender, txtEmail.getText(), txtMobileNumber.getText(), choiceBox.getValue(), txtAmount.getText(), AccountsDB.stripMoney(txtBalance.getText())) != 1) {
                    Alerts.AlertError("Error", "Something went wrong");
                    return;
                }
                if (AccountsDB.addRecord("credit", "Payment of student registration fee", txtAmount.getText(), AccountsDB.currentBalance() + Integer.parseInt(AccountsDB.stripMoney(txtAmount.getText()))) != 1) {
                    Alerts.AlertError("Error", "Something went wrong");
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            Alerts.AlertInfo("Success", "Student added successfully!");
            mainPane.getChildren().get(0).setEffect(null);
            primaryPane.setEffect(null);
            Animations.slideUp(primaryPane, dialog, -250);
            ShowTrayNotification
                    .trayNotification("Success!!!", "Student added successfully!!!",
                            AnimationType.SLIDE, NotificationType.SUCCESS);

            try {
                refresh();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        cancelBtn.setOnAction(event -> {
            mainPane.getChildren().get(0).setEffect(null);
            primaryPane.setEffect(null);
            Animations.slideUp(primaryPane, dialog, -250);
        });
    }

    @FXML
    private void editStudent() throws IOException, SQLException {
        if (selectedID == 0) {
            Alerts.AlertError("Error", "Please select student from table");
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StudentsController.class.getResource("/com/school_management/fxml/edit_student_dialog.fxml"));
        MFXGenericDialog dialog = loader.load();

        BoxBlur blur = new BoxBlur(3.0, 3.0, 3);
        AnchorPane mainPane = (AnchorPane) primaryPane.getParent();
        mainPane.getChildren().get(0).setEffect(blur);
        primaryPane.setEffect(blur);
        Animations.slideDown(primaryPane,dialog, 100);

        dialog.setOnClose(event -> {
            mainPane.getChildren().get(0).setEffect(null);
            primaryPane.setEffect(null);
            Animations.slideUp(primaryPane, dialog, -250);
        });

        AnchorPane centerPane = (AnchorPane) dialog.getChildren().get(1);
        AnchorPane bottomPane = (AnchorPane) dialog.getChildren().get(2);

        AnchorPane pane1 = (AnchorPane) centerPane.getChildren().get(0);
        AnchorPane pane2 = (AnchorPane) centerPane.getChildren().get(2);

        MFXTextField txtFirstname = (MFXTextField) pane1.getChildren().get(0);
        txtFirstname.setText(selectedFirstName);

        MFXTextField txtLastname = (MFXTextField) pane1.getChildren().get(1);
        txtLastname.setText(selectedLastName);

        MFXTextField txtMobileNumber = (MFXTextField) pane1.getChildren().get(2);
        txtMobileNumber.setText(selectedNumber);

        MFXTextField txtEmail = (MFXTextField) pane1.getChildren().get(3);
        txtEmail.setText(selectedEmail);

        MFXRadioButton maleBtn = (MFXRadioButton) pane1.lookup("#maleRadioBtn");
        MFXRadioButton femaleBtn = (MFXRadioButton) pane1.lookup("#femaleRadioBtn");

        if (selectedGender.equals("male")) maleBtn.setSelected(true);
        else femaleBtn.setSelected(true);

        ChoiceBox<String> choiceBox = (ChoiceBox<String>) pane2.getChildren().get(0);
        choiceBox.setValue(selectedClass);

        AnchorPane amountPane = (AnchorPane) pane2.getChildren().get(1);
        MFXTextField txtBalance = (MFXTextField) pane2.getChildren().get(2);
        txtBalance.setText(selectedBalance);

        MFXTextField txtRegDate = (MFXTextField) pane2.getChildren().get(3);
        txtRegDate.setText(selectedRegDate);

        AnchorPane pane = (AnchorPane) amountPane.getChildren().get(0);
        Label txtAmountPayable = (Label) pane.lookup("#txtAmountPayable");
        txtAmountPayable.setText(ClassesDB.getFee(choiceBox.getValue()));

        MFXTextField txtAmount = (MFXTextField) amountPane.getChildren().get(1);
        txtAmount.setText(AccountsDB.stripMoney(selectedAmount));

        ResultSet rs = ClassesDB.getClasses();
        ObservableList<String> classes = FXCollections.observableArrayList();

        while (rs.next()) {
            String className = rs.getString(Class.CLASS_NAME);
            classes.add(className);
        }
        choiceBox.setItems(classes);

        MFXButton saveBtn = (MFXButton) bottomPane.lookup("#saveBtn");
        MFXButton cancelBtn = (MFXButton) bottomPane.lookup("#cancelBtn");

        choiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    txtAmountPayable.setText(ClassesDB.getFee(choiceBox.getValue()));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if (txtAmountPayable.getText().equals("")) return;
                if (txtAmount.getText().equals("")) {
                    txtBalance.setText("");
                    return;
                }
                if (!txtAmount.getText().matches("[0-9]+")) {
                    Alerts.AlertError("Error", "Invalid Amount");
                    return;
                }
                if ((Integer.parseInt(AccountsDB.stripMoney(txtAmountPayable.getText())) < Integer.parseInt(txtAmount.getText()))) {
                    txtBalance.setText("");
                    Alerts.AlertError("Error", "Amount cannot exceed " + txtAmountPayable.getText());
                    return;
                }
                txtBalance.setText("\u20A6" + (Integer.parseInt(AccountsDB.stripMoney(txtAmountPayable.getText())) - Integer.parseInt(txtAmount.getText())));
            }
        });

        try {
            txtAmount.setOnKeyReleased(event -> {
                if (txtAmountPayable.getText().equals("")) return;
                if (txtAmount.getText().equals("")) {
                    txtBalance.setText("");
                    return;
                }
                if (!txtAmount.getText().matches("[0-9]+")) {
                    Alerts.AlertError("Error", "Invalid Amount");
                    return;
                }
                if ((Integer.parseInt(AccountsDB.stripMoney(txtAmountPayable.getText())) < Integer.parseInt((txtAmount.getText())))) {
                    txtBalance.setText("");
                    Alerts.AlertError("Error", "Amount cannot exceed " + txtAmountPayable.getText());
                    return;
                }
                txtBalance.setText("\u20A6" + (Integer.parseInt(AccountsDB.stripMoney(txtAmountPayable.getText())) - Integer.parseInt(txtAmount.getText())));
            });
        } catch (NumberFormatException e) {
            Alerts.AlertError("Error", "Invalid amount");
        }

        saveBtn.setOnAction(event -> {
            if (txtFirstname.getText().equals("") || txtFirstname.getText().trim().equals("")) {
                Alerts.AlertError("Error", "Firstname field cannot be empty");
                return;
            }

            if (txtLastname.getText().equals("") || txtLastname.getText().trim().equals("")) {
                Alerts.AlertError("Error", "Lastname field cannot be empty");
                return;
            }

            if (txtMobileNumber.getText().equals("") || txtMobileNumber.getText().trim().equals("")) {
                Alerts.AlertError("Error", "Mobile number field cannot be empty");
                return;
            }

            if (txtEmail.getText().equals("") || txtEmail.getText().trim().equals("")) {
                Alerts.AlertError("Error", "Email field cannot be empty");
                return;
            }

            if (!maleBtn.isSelected() && !femaleBtn.isSelected()) {
                Alerts.AlertError("Error", "Gender must be selected");
                return;
            }

            if (choiceBox.getValue() == null) {
                Alerts.AlertError("Error", "Class must be selected");
                return;
            }

            if (txtAmount.getText().equals("")) {
                Alerts.AlertError("Error", "Please enter amount paid");
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

            if (!Validators.isValidNumber(txtMobileNumber.getText())) {
                Alerts.AlertError("Error", "Invalid Mobile Number");
                return;
            }

            if (!Validators.isValidEmail(txtEmail.getText())) {
                Alerts.AlertError("Error", "Invalid Email");
                return;
            }

            if (txtBalance.getText().equals("")) {
                Alerts.AlertError("Error", "Please enter valid amount");
                return;
            }

            if (StudentsDB.studentExists(txtEmail.getText()) == 1 && !txtEmail.getText().equals(selectedEmail)) {
                Alerts.AlertError("Error", "Student with this email already exists");
                return;
            }

            String gender;
            if (maleBtn.isSelected()) gender = "male";
            else gender = "female";

            try {
                if (Alerts.AlertConfirmation("Confirmation", "Are you sure you want to edit this student's records?") == ButtonType.OK) {
                    if (!txtAmount.getText().equals(AccountsDB.stripMoney(selectedAmount))) {
                        String eventType;
                        String desc;
                        int amount;
                        int balance;
                        if (Integer.parseInt(txtAmount.getText()) > Integer.parseInt(AccountsDB.stripMoney(selectedAmount))) {
                            eventType = "credit";
                            desc = "Payment of student registration fee balance";
                            amount = Integer.parseInt(txtAmount.getText()) - Integer.parseInt(selectedAmount);
                            balance = AccountsDB.currentBalance() + amount;
                        } else {
                            eventType = "debit";
                            desc = "Student fee payment correction";
                            amount = Integer.parseInt(AccountsDB.stripMoney(selectedAmount)) -  Integer.parseInt(txtAmount.getText());
                            balance = AccountsDB.currentBalance() - amount;

                            if (balance < 1) {
                                Alerts.AlertError("Error", "Insufficient account balance to make deduction");
                                return;
                            }
                        }
                        if (StudentsDB.updateStudent(selectedID, txtFirstname.getText(), txtLastname.getText(), gender, txtEmail.getText(), txtMobileNumber.getText(), choiceBox.getValue(), txtAmount.getText(), AccountsDB.stripMoney(txtBalance.getText())) != 1) {
                            Alerts.AlertError("Error", "Something went wrong");
                            return;
                        }
                        AccountsDB.addRecord(eventType, desc, String.valueOf(amount), balance);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            Alerts.AlertInfo("Success", "Student updated successfully!");
            mainPane.getChildren().get(0).setEffect(null);
            primaryPane.setEffect(null);
            Animations.slideUp(primaryPane, dialog, -250);
            ShowTrayNotification
                    .trayNotification("Success!!!", "Student updated successfully!!!",
                            AnimationType.SLIDE, NotificationType.SUCCESS);

            try {
                refresh();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        cancelBtn.setOnAction(event -> {
            mainPane.getChildren().get(0).setEffect(null);
            primaryPane.setEffect(null);
            Animations.slideUp(primaryPane, dialog, -250);
        });
    }

    @FXML
    private void deleteStudent() throws SQLException {
        if (selectedID == 0) {
            Alerts.AlertError("Error", "Please select student from table");
            return;
        }

        if (Alerts.AlertConfirmation("Confirmation", "Are you sure you want to delete this student record?") == ButtonType.OK) {
           if (StudentsDB.deleteStudent(selectedID) != 1) {
               Alerts.AlertError("Error", "Something went wrong");
               return;
           }
           Alerts.AlertInfo("Success", "Student deleted successfully!");
           refresh();
           ShowTrayNotification
                    .trayNotification("Success!!!", "Student deleted successfully!!!",
                            AnimationType.SLIDE, NotificationType.SUCCESS);
        }
    }

    @FXML
    private void search() throws SQLException {
        students.clear();
        populateTable(StudentsDB.search(txtSearch.getText()));
    }

    private void populateTable(ResultSet rs) throws SQLException {
        while (true) {
            assert rs != null : "No registered student";
            if (!rs.next()) break;
            Student student = new Student();
            student.setStudentID(rs.getInt(Student.STUDENT_ID));
            student.setAmountPaid(rs.getString(Student.STUDENT_FEE));
            student.setBalance(rs.getString(Student.STUDENT_BALANCE));
            student.setClassName(rs.getString(Student.STUDENT_CLASS));
            student.setEmail(rs.getString(Student.STUDENT_EMAIL_ADDRESS));
            student.setGender(rs.getString(Student.STUDENT_GENDER));
            student.setFirstName(rs.getString(Student.STUDENT_FIRST_NAME));
            student.setLastName(rs.getString(Student.STUDENT_LAST_NAME));
            student.setMobileNumber(rs.getString(Student.STUDENT_NUMBER));
            student.setRegDate(rs.getString(Student.STUDENT_REG_DATE));
            students.add(student);
        }
        tableView.setItems(students);
    }

    private void setTableView() {
        assert tableView != null : "No registered teacher!";
        columnStudentID.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        columnAmountPaid.setCellValueFactory(new PropertyValueFactory<>("amountPaid"));
        columnBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        columnClass.setCellValueFactory(new PropertyValueFactory<>("className"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        columnMobileNumber.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
        columnRegDate.setCellValueFactory(new PropertyValueFactory<>("regDate"));
        columnUpdatedAt.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedID = newVal.getStudentID();
                selectedAmount = newVal.getAmountPaid();
                selectedBalance = newVal.getBalance();
                selectedClass = newVal.getClassName();
                selectedEmail = newVal.getEmail();
                selectedGender = newVal.getGender();
                selectedFirstName = newVal.getFirstName();
                selectedLastName = newVal.getLastName();
                selectedNumber = newVal.getMobileNumber();
                selectedRegDate = newVal.getRegDate();
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTableView();
        try {
            populateTable(StudentsDB.getStudents());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
