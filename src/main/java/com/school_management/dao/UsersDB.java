package com.school_management.dao;

import com.school_management.models.RecursiveUser;
import com.school_management.models.User;
import com.school_management.utils.CurrentUser;
import com.school_management.utils.DBConstants;
import com.school_management.utils.DBUtil;
import com.school_management.utils.Hash;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsersDB {
    Statement statement = null;
    static PreparedStatement preparedStatement = null;
    private static final Connection connection = DBConnection.getInstance().connection();

    public UsersDB() {
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static ResultSet getUsers() {
        String query = "SELECT " + User.USER_ID + ", CONCAT(first_name, ' ', last_name) AS username, " + User.USER_EMAIL + ", " + User.USER_NUMBER + ", " + User.USER_ROLE + " FROM " + DBConstants.TABLE_USERS + " WHERE " + User.USER_ID + " != ?;";
        ResultSet rs;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, CurrentUser.getUserID());
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            Logger.getLogger(UsersDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
        return rs;
    }

    public static ResultSet searchUsers(String searchString) {
        String query = "SELECT " + User.USER_ID + ", CONCAT(first_name, ' ', last_name) AS username, " + User.USER_EMAIL + ", " + User.USER_NUMBER + ", " + User.USER_ROLE + " FROM " + DBConstants.TABLE_USERS + " WHERE (email ~* ? OR CONCAT(first_name, ' ', last_name) ~* ?) AND " + User.USER_ID + " != ?;";
        ResultSet rs;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, searchString);
            preparedStatement.setString(2, searchString);
            preparedStatement.setInt(3, CurrentUser.getUserID());
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            Logger.getLogger(UsersDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
        return rs;
    }

    // adding new user
    public int addUser(RecursiveUser user) {
        String query = "INSERT INTO " + DBConstants.TABLE_USERS + "(" + User.USER_FIRST_NAME + ", " + User.USER_LAST_NAME + ", " + User.USER_EMAIL +
                ", " + User.USER_GENDER + ", " + User.USER_NUMBER + ", " + User.USER_PASSWORD +
                ") VALUES (?, ?, ?, ?, ?, ?);";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getGender());
            preparedStatement.setString(5, user.getNumber());
            preparedStatement.setString(6, user.getPassword());

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return -1;
        }
    }

    // editing user details
    public int editUser(int id, String firstName, String lastName, String gender, String number) {
        String query = "UPDATE " + DBConstants.TABLE_USERS + " SET " + User.USER_FIRST_NAME + " =?, " +
                User.USER_LAST_NAME + " =?, " + User.USER_GENDER + "=?, " + User.USER_NUMBER + " =? " +
                " WHERE id = ?;";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, gender);
            preparedStatement.setString(4, number);
            preparedStatement.setInt(5, id);

            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return -1;
        }
    }
    
    // editing user role
    public static int editUserRole(int id, String role) {
        String query = "UPDATE " + DBConstants.TABLE_USERS + " SET " + User.USER_ROLE + " = ? WHERE " + User.USER_ID + " = ? ;";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, role);
            preparedStatement.setInt(2, id);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(UsersDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return -1;
        }
    }

    //checking if the user exists
    public int userExist(String email) {
        String query = "SELECT COUNT(" + User.USER_EMAIL + ") FROM " + DBConstants.TABLE_USERS +
                " WHERE " + User.USER_EMAIL + " = '" + email + "';";

        return DBUtil.getInstance().counter(query);
    }

    public boolean isSuspended(int id) {
        String query = "SELECT " + User.USER_SUSPENDED + " FROM " + DBConstants.TABLE_USERS + " WHERE " + User.USER_ID + " = ?;";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            String suspended = rs.getString(User.USER_SUSPENDED);
            return Boolean.parseBoolean(suspended);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int suspend(String suspended, int id) {
        String query = "UPDATE " + DBConstants.TABLE_USERS + " SET " + User.USER_SUSPENDED + " = ? WHERE " + User.USER_ID + " = ? ;";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, suspended);
            preparedStatement.setInt(2, id);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(UsersDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return -1;
        }
    }

    public static String getUserRole() {
        String query = "SELECT * FROM " + DBConstants.TABLE_USERS + " WHERE " + User.USER_ID + " = ?;";
        ResultSet rs;
        String role = "";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, CurrentUser.getUserID());
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                role = rs.getString(User.USER_ROLE);
            }
            return role;
        } catch (SQLException e) {
            Logger.getLogger(UsersDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return "";
        }
    }

    public Boolean authenticate(String email, String password) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
        String query = "SELECT * FROM " + DBConstants.TABLE_USERS + " WHERE email = '" + email + "';";

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        String hashedPassword;
        if (rs.next()) {
            hashedPassword = rs.getString(User.USER_PASSWORD);
        } else {
            return false;
        }
        return Hash.validatePassword(password, hashedPassword);
    }

    public static ObservableList<String> getTeacherIDS() {
        String query = "SELECT * FROM " + DBConstants.TABLE_USERS + ";";
        ObservableList<String> teacherList = FXCollections.observableArrayList();
        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            while (true) {
                assert rs != null;
                if (!rs.next()) break;
                String teacherID = String.valueOf(rs.getInt(User.USER_ID));
                teacherList.add(teacherID);
            }
            return teacherList;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
