package com.school_management.dao;

import com.school_management.models.Class;
import com.school_management.utils.DBConstants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClassesDB {
    Statement statement = null;
    static PreparedStatement preparedStatement = null;
    private static final Connection connection = DBConnection.getInstance().connection();

    public ClassesDB() {
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static ResultSet getClasses() throws SQLException {
        String query = "SELECT * FROM " + DBConstants.TABLE_CLASSES + ";";
        preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();
        return rs;
    }
}
