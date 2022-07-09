package com.school_management.utils;

import com.school_management.dao.DBConnection;
import com.school_management.models.Auth;
import com.school_management.models.User;
import org.apache.commons.codec.DecoderException;

import com.school_management.io.UserWriter;

import java.sql.*;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CurrentUser {
    static PreparedStatement preparedStatement = null;
    private static final Connection connection = DBConnection.getInstance().connection();

    public static String getEmail() {
        Auth auth;
        try {
            auth = UserWriter.getCurrentUser();
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }
        assert auth != null;
        return auth.getEmail();
    }

    public static int getUserID() throws SQLException {
        String query = "SELECT id FROM users WHERE email = ?;";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, getEmail());
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) return rs.getInt(User.USER_ID);
        return -1;
    }

    public static String getUserGender() throws SQLException {
        Auth auth;
        String decodedEmail = "";
        String gender = "";
        try {
            auth = UserWriter.getCurrentUser();
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }
        assert auth != null;
        decodedEmail = auth.getEmail();

        String query = "SELECT gender FROM users WHERE email = ?;";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, decodedEmail);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            gender = rs.getString("gender");
        }
        return gender;
    }

    public static String currentUsername() throws DecoderException, SQLException {
        Statement statement = connection.createStatement();
        String email = Objects.requireNonNull(UserWriter.getCurrentUser()).getEmail();
        try {
            ResultSet rs = statement.executeQuery("SELECT " + User.USER_FIRST_NAME + " FROM " + DBConstants.TABLE_USERS + " WHERE " + User.USER_EMAIL + " = '" + email + "';");
            if (rs.next()) {
                return rs.getString(User.USER_FIRST_NAME);
            }
        } catch (SQLException e) {
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return "";
        }
        return "";
    }

}
