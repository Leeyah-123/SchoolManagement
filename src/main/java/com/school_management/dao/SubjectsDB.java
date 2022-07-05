package com.school_management.dao;

import com.school_management.models.Subject;
import com.school_management.utils.DBConstants;
import com.school_management.utils.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubjectsDB {
    Statement statement = null;
    static PreparedStatement preparedStatement = null;
    private static final Connection connection = DBConnection.getInstance().connection();

    public SubjectsDB() {
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static ResultSet getSubjects() throws SQLException {
        String query = "SELECT * FROM " + DBConstants.TABLE_SUBJECTS + " ORDER BY subject_id ASC;";
        preparedStatement = connection.prepareStatement(query);
        return preparedStatement.executeQuery();
    }

    public static int addSubject(String subjectName) throws SQLException {
        String query = "INSERT INTO " + DBConstants.TABLE_SUBJECTS + "(" + Subject.SUBJECT_NAME + ") VALUES (?);";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, subjectName);
        return preparedStatement.executeUpdate();
    }

    public static int editSubject(int subjectID, String subjectName) throws SQLException {
        String query = "UPDATE " + DBConstants.TABLE_SUBJECTS + " SET " + Subject.SUBJECT_NAME + " = ? WHERE " + Subject.SUBJECT_ID + " = ?;";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, subjectName);
        preparedStatement.setInt(2, subjectID);
        return preparedStatement.executeUpdate();
    }

    public static int deleteSubject(int subjectID) throws SQLException {
        String query = "DELETE FROM " + DBConstants.TABLE_SUBJECTS + " WHERE " + Subject.SUBJECT_ID+ " = ?;";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, subjectID);
        return preparedStatement.executeUpdate();
    }

    public static int subjectExist(String subjectName) throws SQLException {
        String query = "SELECT COUNT(" + Subject.SUBJECT_NAME + ") FROM " + DBConstants.TABLE_SUBJECTS +
                " WHERE " + Subject.SUBJECT_NAME + " = '" + subjectName + "';";

        return DBUtil.getInstance().counter(query);
    }

    public static ObservableList<String> getSubjectNames() {
        ResultSet rs;
        ObservableList<String> subjectList = FXCollections.observableArrayList();
        try {
            rs = getSubjects();
            while (rs.next()) {
                String subjectName = rs.getString(Subject.SUBJECT_NAME);
                subjectList.add(subjectName);
            }
            return subjectList;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}

