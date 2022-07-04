package com.school_management.controllers;

import com.school_management.dao.ClassesDB;
import com.school_management.dao.SessionDB;
import com.school_management.dao.UsersDB;
import com.school_management.models.Class;
import com.school_management.models.RecursiveUser;
import com.school_management.models.Session;
import com.school_management.models.User;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyListView;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class StatisticsController implements Initializable {
    @FXML
    private Label lblSession;
    @FXML
    MFXTextField txtSearch, txtStartDate, txtEndDate;
    @FXML
    private MFXLegacyListView<String> listClasses;
    @FXML
    private MFXLegacyTableView<RecursiveUser> tableView;
    @FXML
    private TableColumn<RecursiveUser, String> colUserName;
    @FXML
    private TableColumn<RecursiveUser, String> colEmail;

    ObservableList<RecursiveUser> users = FXCollections.observableArrayList();

    @FXML
    private void search() throws SQLException {
        ResultSet rs = UsersDB.searchUsers(txtSearch.getText());
        users.clear();
        assert rs != null : "No results found";
        populateTable(rs);
    }

    @FXML
    private void editSession() {

    }

    @FXML
    private void refresh() throws SQLException {
        txtSearch.setText("");
        users.clear();
        populateTable(Objects.requireNonNull(UsersDB.getUsers()));
    }

    private void setTableView() {
        assert tableView != null : "No registered user!";
        colUserName.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
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
