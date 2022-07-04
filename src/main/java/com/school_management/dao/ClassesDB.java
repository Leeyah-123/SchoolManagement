package com.school_management.dao;

import com.school_management.models.Class;
import com.school_management.utils.DBConstants;
import com.school_management.utils.DBUtil;
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
        return preparedStatement.executeQuery();
    }

    public static int addClass(String className) throws SQLException {
        String query = "INSERT INTO " + DBConstants.TABLE_CLASSES + "(" + Class.CLASS_NAME + ") VALUES (?);";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, className);
        return preparedStatement.executeUpdate();
    }

    public static int editClass(int classID, String className) throws SQLException {
        String query = "UPDATE " + DBConstants.TABLE_CLASSES + " SET " + Class.CLASS_NAME + " = ? WHERE " + Class.CLASS_ID + " = ?;";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, className);
        preparedStatement.setInt(2, classID);
        return preparedStatement.executeUpdate();
    }

    public static int deleteClass(int classID) throws SQLException {
        String query = "DELETE FROM " + DBConstants.TABLE_CLASSES + " WHERE " + Class.CLASS_ID + " = ?;";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, classID);
        return preparedStatement.executeUpdate();
    }

    public static int classExist(String className) throws SQLException {
        String query = "SELECT COUNT(" + Class.CLASS_NAME + ") FROM " + DBConstants.TABLE_CLASSES +
                " WHERE " + Class.CLASS_NAME + " = '" + className + "';";

        return DBUtil.getInstance().counter(query);
    }

    public static ObservableList<String> getClassNames() {
        ResultSet rs;
        ObservableList<String> classList = FXCollections.observableArrayList();
        try {
            rs = getClasses();
            while (rs.next()) {
                String className = rs.getString(Class.CLASS_NAME);
                classList.add(className);
            }
            return classList;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
