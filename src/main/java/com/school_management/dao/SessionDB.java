package com.school_management.dao;

import com.school_management.models.Session;
import com.school_management.utils.DBConstants;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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

        double sessionProgress = 0.0;
        if (endYear >= currentYear && currentYear >= startYear && currentMonth >= startMonth) {
            if (currentMonth == startMonth && currentDate < startDate) sessionProgress = 0.0;
            else if (startMonth <= currentMonth) {
                sessionProgress = ((currentYear - startYear) + ((currentMonth - startMonth) / 12));
            } else {
                sessionProgress = ((currentYear - startYear) + (((currentMonth + 12) - startMonth) / 12));
            }
        }
        return sessionProgress;
    }

    public static int editSessionDetails(LocalDate startDate, LocalDate endDate) {
        String query = "UPDATE " + DBConstants.TABLE_SESSION + " SET " + Session.START_DATE + " = ?, " + Session.END_DATE + " = ?;";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, java.sql.Date.valueOf(startDate));
            preparedStatement.setDate(2, java.sql.Date.valueOf(endDate));
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
