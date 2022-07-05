package com.school_management.dao;

import com.school_management.models.Expense;
import com.school_management.utils.DBConstants;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExpensesDB {
    Statement statement = null;
    static PreparedStatement preparedStatement = null;
    private static final Connection connection = DBConnection.getInstance().connection();

    public ExpensesDB() {
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static ResultSet getRecords() {
        String query = "SELECT * FROM " + DBConstants.TABLE_EXPENSES + " ORDER BY record_id ASC;";
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

    public static int addRecord(String item, String description, String cost) {
        String query = "INSERT INTO " + DBConstants.TABLE_EXPENSES + "(" + Expense.ITEM_PURCHASED + ", \"" + Expense.EXPENSE_DESCRIPTION + "\", " + Expense.EXPENSE_COST + ") VALUES (?, ?, ?);";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, item);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, Integer.parseInt(cost));
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(UsersDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return -1;
        }
    }
}
