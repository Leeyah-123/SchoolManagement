package com.school_management.dao;

import com.school_management.models.Session;
import com.school_management.utils.DBConstants;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SessionDB {

    // getting connection details
    static Statement statement = null;
    static PreparedStatement preparedStatement = null;
    private static final Connection connection = DBConnection.getInstance().connection();

    public SessionDB() {
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static ResultSet getSessionDetails() throws SQLException {
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM " + DBConstants.TABLE_SESSION + ";";
        return statement.executeQuery(query);
    }

    public static double SessionProgress() throws SQLException {
        ResultSet rs = getSessionDetails();
        String start = "";
        String end = "";
        while (rs.next()) {
            start = rs.getString(Session.START_DATE);
            end = rs.getString(Session.END_DATE);
        }
        String[] startDateArray = start.split("-");
        String[] endDateArray = end.split("-");

        double startYear = Double.parseDouble(startDateArray[0]);
        double startMonth = Double.parseDouble(startDateArray[1]);
        double startDate = Double.parseDouble(startDateArray[2]);
        double endYear = Double.parseDouble(endDateArray[0]);

        Date currentDay = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String[] currentDateArray = dateFormat.format(currentDay).split("-");

        double currentDate = Double.parseDouble(currentDateArray[0]);
        double currentMonth = Double.parseDouble(currentDateArray[1]);
        double currentYear = Double.parseDouble(currentDateArray[2]);
        double interval = (endYear-startYear) * 12;

        if (currentYear < endYear) {
            if (startMonth < currentMonth) {
                if (currentDate >= startDate) {
                    return ((currentMonth - startMonth) /interval);
                } else return 0;
            } else return 0.0;
        } else return 0.0;
    }

    public static int editSessionDetails(String startDate, String endDate) throws SQLException {
        String query = "UPDATE " + DBConstants.TABLE_SESSION + " SET " + Session.START_DATE + " = ?, " + Session.END_DATE + " = ?;";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, startDate);
        preparedStatement.setString(2, endDate);
        return preparedStatement.executeUpdate();
    }

    public static String sessionString() throws SQLException {
        ResultSet rs = getSessionDetails();
        String startDate = "";
        String endDate = "";
        while (rs.next()) {
            startDate = rs.getString(Session.START_DATE);
            endDate = rs.getString(Session.END_DATE);
        }
        String[] startDateArray = startDate.split("-");
        String[] endDateArray = endDate.split("-");

        String startYear = startDateArray[0];
        String endYear = endDateArray[0];
        return startYear + "/" + endYear;
    }
}
