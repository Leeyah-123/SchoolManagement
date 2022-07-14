package com.school_management.dao;

import com.school_management.models.General;
import com.school_management.models.Statistics;
import com.school_management.utils.DBConstants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GeneralDB {

    // getting connection details
    Statement statement = null;
    static PreparedStatement preparedStatement = null;
    private static final Connection connection = DBConnection.getInstance().connection();

    public GeneralDB() {
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static ResultSet getRecords() throws SQLException {
        String query = "SELECT * FROM " + DBConstants.TABLE_GENERAL + " ORDER BY record_id ASC;";
        preparedStatement = connection.prepareStatement(query);
        return preparedStatement.executeQuery();
    }

    public static int addRecord(int teacherID, String className, String subjectName) throws SQLException {
        String query = "INSERT INTO " + DBConstants.TABLE_GENERAL + "(teacher_id, class_name, subject_name) VALUES ('" + teacherID + "', '" + className + "', '" + subjectName + "');";
        preparedStatement = connection.prepareStatement(query);
        return preparedStatement.executeUpdate();
    }

    public static int deleteRecord(int recordID) throws SQLException {
        String query = "DELETE FROM " + DBConstants.TABLE_GENERAL + " WHERE " + General.GENERAL_RECORD_ID + " = ?;";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, recordID);
        return preparedStatement.executeUpdate();
    }

    public static ResultSet getTeacherStatistics(int TeacherID) throws SQLException {
        String query = "SELECT * FROM " + DBConstants.TABLE_GENERAL + " WHERE " + General.GENERAL_TEACHER_ID + " = ? ORDER BY record_id ASC;";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, TeacherID);
        return preparedStatement.executeQuery();
    }

    public static ObservableList<Statistics> getList(int TeacherID) throws SQLException {
        ResultSet rs = getTeacherStatistics(TeacherID);
        ObservableList<Statistics> listStatistics = FXCollections.observableArrayList();
        Statistics statistics;
        while (rs.next()) {
            statistics = new Statistics();
            statistics.setRecordID(rs.getInt(General.GENERAL_RECORD_ID));
            statistics.setSubjectName(rs.getString(General.GENERAL_SUBJECT_NAME));
            statistics.setClassName(rs.getString(General.GENERAL_CLASS_NAME));
            listStatistics.add(statistics);
        }
        return listStatistics;
    }

    public static ObservableList<String> getClasses(int TeacherID) throws SQLException {
        ObservableList<String> classes = FXCollections.observableArrayList();
        ObservableList<Statistics> listStatistics = getList(TeacherID);
        listStatistics.forEach(statistics -> classes.add(statistics.getClassName()));
        return classes;
    }

    public static int getClassNum(int TeacherID) throws SQLException {
        int classNum = 0;
        ObservableList<Statistics> listStatistics = getList(TeacherID);
        for (Statistics ignored : listStatistics) {
            classNum += 1;
        }
        return classNum;
    }

    public static ResultSet searchRecords(int searchInt) {
        String query = "SELECT * FROM " + DBConstants.TABLE_GENERAL + " WHERE " + General.GENERAL_TEACHER_ID + " = ?;";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, searchInt);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            Logger.getLogger(UsersDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
}
