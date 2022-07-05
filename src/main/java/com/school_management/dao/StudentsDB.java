package com.school_management.dao;

import com.school_management.models.Student;
import com.school_management.utils.DBConstants;
import com.school_management.utils.DBUtil;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentsDB {
    Statement statement = null;
    static PreparedStatement preparedStatement = null;
    private static final Connection connection = DBConnection.getInstance().connection();

    public StudentsDB() {
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static ResultSet getStudents() throws SQLException {
        String query = "SELECT * FROM " + DBConstants.TABLE_STUDENTS + ";";
        preparedStatement = connection.prepareStatement(query);
        return preparedStatement.executeQuery();
    }

    public static int addStudent(String firstName, String lastName, String gender, String email, String number, String className, String fee, String balance) throws SQLException {
        String query = "INSERT INTO " + DBConstants.TABLE_STUDENTS + "(" +
                Student.STUDENT_FIRST_NAME + ", " +
                Student.STUDENT_LAST_NAME + ", " +
                Student.STUDENT_GENDER + ", " +
                Student.STUDENT_EMAIL_ADDRESS + ", " +
                Student.STUDENT_NUMBER + ", " +
                Student.STUDENT_CLASS + ", " +
                Student.STUDENT_FEE + ", " +
                Student.STUDENT_BALANCE +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, firstName);
        preparedStatement.setString(2, lastName);
        preparedStatement.setString(3, gender);
        preparedStatement.setString(4, email);
        preparedStatement.setString(5, number);
        preparedStatement.setString(6, className);
        preparedStatement.setInt(7, Integer.parseInt(fee));
        preparedStatement.setInt(8, Integer.parseInt(balance));
        return preparedStatement.executeUpdate();
    }

    public static int updateStudent(int studentID, String firstName, String lastName, String gender, String email, String number, String className, String fee, String balance) throws SQLException {
        String query = "UPDATE " + DBConstants.TABLE_STUDENTS + " SET " +
                Student.STUDENT_FIRST_NAME + " = ?," +
                Student.STUDENT_LAST_NAME + " = ?," +
                Student.STUDENT_GENDER + " = ?," +
                Student.STUDENT_EMAIL_ADDRESS + " = ?," +
                Student.STUDENT_NUMBER + " = ?," +
                Student.STUDENT_CLASS + " = ?," +
                Student.STUDENT_FEE + " = ?," +
                Student.STUDENT_BALANCE + " = ?," +
                Student.UPDATED_AT + " = CURRENT_TIMESTAMP" +
                " WHERE " + Student.STUDENT_ID + " = ?;";

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, firstName);
        preparedStatement.setString(2, lastName);
        preparedStatement.setString(3, gender);
        preparedStatement.setString(4, email);
        preparedStatement.setString(5, number);
        preparedStatement.setString(6, className);
        preparedStatement.setInt(7, Integer.parseInt(fee));
        preparedStatement.setInt(8, Integer.parseInt(balance));
        preparedStatement.setInt(9, studentID);
        return preparedStatement.executeUpdate();
    }

    public static int deleteStudent(int studentID) throws SQLException {
        String query = "DELETE FROM " + DBConstants.TABLE_STUDENTS + " WHERE " + Student.STUDENT_ID + " = ?;";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, studentID);
        return preparedStatement.executeUpdate();
    }

    public static int studentExists(String email) {
        String query = "SELECT COUNT(" + Student.STUDENT_EMAIL_ADDRESS + ") FROM " + DBConstants.TABLE_STUDENTS +
                " WHERE " + Student.STUDENT_EMAIL_ADDRESS + " = '" + email + "';";

        return DBUtil.getInstance().counter(query);
    }

    public static String countStudents() throws SQLException {
        ResultSet rs = getStudents();
        int totalStudents = 0;
        while (rs.next()) totalStudents ++;
        return String.valueOf(totalStudents);
    }

    public static String countGender(String gender) throws SQLException {
        String query = "SELECT * FROM " + DBConstants.TABLE_STUDENTS + " WHERE " + Student.STUDENT_GENDER + " = '" + gender + "';";
        preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();
        int numStudents = 0;
        while (rs.next()) numStudents++;
        return String.valueOf(numStudents);
    }

    public static ResultSet search(String searchString) throws SQLException {
        String query = "SELECT * FROM " + DBConstants.TABLE_STUDENTS + "WHERE (" + Student.STUDENT_FIRST_NAME + " ~* ? OR " + Student.STUDENT_LAST_NAME + " ~* ? OR " + Student.STUDENT_EMAIL_ADDRESS + " ~* ?);";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, searchString);
        preparedStatement.setString(2, searchString);
        preparedStatement.setString(3, searchString);
        return preparedStatement.executeQuery();
    }
}
