package com.school_management.dao;

import com.school_management.models.Account;
import com.school_management.utils.CurrentUser;
import com.school_management.utils.DBConstants;

import java.sql.*;
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
        String query = "SELECT * FROM " + DBConstants.TABLE_ACCOUNTS + " ORDER BY record_id ASC;";
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

    public static String stripMoney(String money) {
        String strippedMoney = "";
        if (money.charAt(0) != 0 || money.charAt(0) != 1 || money.charAt(0) != 2 || money.charAt(0) != 3 || money.charAt(0) != 4 || money.charAt(0) != 5 || money.charAt(0) != 6 || money.charAt(0) != 7 || money.charAt(0) != 8 || money.charAt(0) != 9) {
            money = money.substring(1);
        }
        String[] floatingMoneyArray = money.split(",");
        String floatingMoney = "";
        for (String s : floatingMoneyArray) {
            floatingMoney = floatingMoney.concat(s);
        }
        String[] balanceArray = floatingMoney.split("\\.");
        strippedMoney = balanceArray[0];
        return strippedMoney;
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
                currentBalance = Integer.parseInt(stripMoney(balanceString));
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
