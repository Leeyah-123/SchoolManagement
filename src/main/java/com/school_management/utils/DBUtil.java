package com.school_management.utils;

import com.school_management.dao.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBUtil {
    private static Statement statement = null;
    private static final DBUtil instance = new DBUtil();

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

    public int statement(String query) {
        try {
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return -1;
        }
    }
}
