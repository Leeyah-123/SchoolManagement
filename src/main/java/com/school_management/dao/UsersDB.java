package com.school_management.dao;

import com.school_management.models.RecursiveUser;
import com.school_management.models.User;
import com.school_management.utils.DBConstants;
import com.school_management.utils.DBUtil;
import com.school_management.utils.Hash;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsersDB {
    Statement statement = null;
    PreparedStatement preparedStatement = null;
    private final Connection connection = DBConnection.getInstance().connection();

    public UsersDB() {
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
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

    // editing user
    public int editUser(int id, String firstName, String lastName, String gender, String number) {
        String query = "UPDATE " + DBConstants.TABLE_USERS + " SET " + User.USER_FIRST_NAME + " =?, " +
                User.USER_LAST_NAME + " =?, " + User.USER_GENDER + "=?, " + User.USER_NUMBER + " =? " +
                " WHERE id=?;";
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

    //check if the user exist
    public int userExist(String email) {
        String query = "SELECT COUNT(" + User.USER_EMAIL + ") FROM " + DBConstants.TABLE_USERS +
                " WHERE " + User.USER_EMAIL + " = '" + email + "';";

        return DBUtil.getInstance().counter(query);
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
}
