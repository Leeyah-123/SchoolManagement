package com.school_management.controllers;

import com.school_management.dao.ClassesDB;
import com.school_management.models.Class;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ClassesController implements Initializable {
    @FXML
    private MFXLegacyTableView<Class> tableView;
    @FXML
    private TableColumn<Class, String> columnClassID;
    @FXML
    private TableColumn<Class, String> columnClassName;

    ObservableList<Class> classes = FXCollections.observableArrayList();
    @FXML
    private void addClass() {

    }

    @FXML
    private void editClass() {

    }

    @FXML
    private void deleteClass() {

    }

    @FXML
    private void refreshClass() {

    }

    private void setTable() throws SQLException {
        ResultSet rs = ClassesDB.getClasses();
        ObservableList<Class> classList = FXCollections.observableArrayList();
        while (rs.next()) {
            Class classItem = new Class();
            classItem.setClassID(rs.getInt(Class.CLASS_ID));
            classItem.setClassName(rs.getString(Class.CLASS_NAME));
            classList.add(classItem);
        }
        tableView.setItems(classList);

        assert tableView != null : "No registered class!";
        columnClassID.setCellValueFactory(new PropertyValueFactory<>("classID"));
        columnClassName.setCellValueFactory(new PropertyValueFactory<>("className"));

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {

            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
