package com.school_management.utils;

import com.school_management.dao.DBConnection;
import com.school_management.models.Auth;
import com.school_management.models.User;
import org.apache.commons.codec.DecoderException;

import com.school_management.io.UserWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrentUser {
    static PreparedStatement preparedStatement = null;
    private static final Connection connection = DBConnection.getInstance().connection();

    public static int getUserID() throws SQLException {
        Auth auth;
        String decodedEmail = "";
        try {
            auth = UserWriter.getCurrentUser();
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }
        assert auth != null;
        decodedEmail = auth.getEmail();

        String query = "SELECT id FROM users WHERE email = ?;";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, decodedEmail);
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) return rs.getInt(User.USER_ID);
        return -1;
    }

    public String getUserRole() {
        return "";
    }

}
