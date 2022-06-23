/*
 *  Created by @Mak
 *  User: Ahmad
 *  Date: 8/14/2020
 *  Time: 2:35 PM
 */
package com.school_management.utils;

import com.school_management.dao.DBConnection;
import com.school_management.io.UserWriter;
import com.school_management.models.User;
import org.apache.commons.codec.DecoderException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBUtil {
    private static Statement statement = null;
    private static final DBUtil instance = new DBUtil();
//    private final Connection connection = DBConnection.getInstance().connection();

    public DBUtil() {
        try {
            statement = DBConnection.getInstance().connection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static DBUtil getInstance() {
        return instance;
    }

    public int counter(String query) {
        try {
            statement.executeQuery(query);
            ResultSet set = statement.getResultSet();
            set.absolute(1);
            return Integer.parseInt(set.getString(1));
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return -1;
        }
    }

    public static String currentUsername() throws DecoderException {
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

    public int statement(String query) {
        try {
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return -1;
        }
    }
}
