package com.school_management.dao;

import com.school_management.models.Account;
import com.school_management.utils.CurrentUser;
import com.school_management.utils.DBConstants;

import java.sql.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountsDB {
    Statement statement = null;
    static PreparedStatement preparedStatement = null;
    private static final Connection connection = DBConnection.getInstance().connection();

    public AccountsDB() {
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static ResultSet getRecords() {
        String query = "SELECT * FROM " + DBConstants.TABLE_ACCOUNTS + ";";
        ResultSet rs;
        try {
            preparedStatement = connection.prepareStatement(query);
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            Logger.getLogger(UsersDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
        return rs;
    }

    public static int currentBalance() {
        String query = "SELECT * FROM " + DBConstants.TABLE_ACCOUNTS + " ORDER BY " + Account.ACCOUNT_RECORD_ID + " DESC LIMIT 1;";
        ResultSet rs;
        int currentBalance = 0;
        try {
            preparedStatement = connection.prepareStatement(query);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                String balanceString = rs.getString(Account.ACCOUNT_BALANCE);
                if (balanceString.charAt(0) != 0 || balanceString.charAt(0) != 1 || balanceString.charAt(0) != 2 || balanceString.charAt(0) != 3 || balanceString.charAt(0) != 4 || balanceString.charAt(0) != 5 || balanceString.charAt(0) != 6 || balanceString.charAt(0) != 7 || balanceString.charAt(0) != 8 || balanceString.charAt(0) != 9) {
                    balanceString = balanceString.substring(1);
                    String[] floatingbalanceArray = balanceString.split(",");
                    String floatingbalance = "";
                    for (String s : floatingbalanceArray) {
                        floatingbalance = floatingbalance.concat(s);
                    }
                    String[] balanceArray = floatingbalance.split("\\.");
                    String balance = balanceArray[0];
                    currentBalance = Integer.parseInt(balance);
                }
                return currentBalance;
            }
        } catch(SQLException e) {
            Logger.getLogger(UsersDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return -1;
        }
        return currentBalance;
    }

    public static int addRecord(String event, String description, String amount, int balance) {
        String query = "INSERT INTO " + DBConstants.TABLE_ACCOUNTS + "(" + Account.ACCOUNT_EVENT + ", " + Account.ACCOUNT_EVENT_DESCRIPTION + ", " + Account.ACCOUNT_AMOUNT + ", " + Account.ACCOUNT_BALANCE + ", " + Account.ACCOUNT_SESSION + ", " + Account.RECORDED_BY + ") VALUES (?, ?, ?, ?, ?, ?);";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, event);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, Integer.parseInt(amount));
            preparedStatement.setInt(4, balance);
            preparedStatement.setString(5, SessionDB.sessionString());
            preparedStatement.setInt(6, CurrentUser.getUserID());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(UsersDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return -1;
        }
    }
}
