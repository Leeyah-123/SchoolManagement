package com.school_management.dao;

import com.school_management.models.Reminder;
import com.school_management.utils.CurrentUser;
import com.school_management.utils.DBConstants;
import com.school_management.utils.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RemindersDB {

    // getting connection details
    Statement statement = null;
    static PreparedStatement preparedStatement = null;
    private static final Connection connection = DBConnection.getInstance().connection();

    public RemindersDB() {
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    // get all reminders
    public static ResultSet getAllReminders(int TeacherID) {
        String query = "SELECT * FROM " + DBConstants.TABLE_REMINDERS + " WHERE "+ Reminder.TEACHER_ID + " = " + TeacherID + " ORDER BY reminder_id ASC;";

        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return resultSet;
    }

    // getting the titles of each reminder for display
    public static ObservableList<String> getReminderTitle(int TeacherID) throws SQLException {
        ResultSet resultSet = getAllReminders(TeacherID);
        ObservableList<String> reminderTitles = FXCollections.observableArrayList();
        while (resultSet.next()) {
            String reminderTitle = resultSet.getString(Reminder.REMINDER_TITLE);
            reminderTitles.add(reminderTitle);
        }
        return reminderTitles;
    }

    // getting all info about a reminder based on their title
    public static Reminder getReminderDetails(String reminderTitle) throws SQLException {
        String query = "SELECT * FROM " + DBConstants.TABLE_REMINDERS + " WHERE "+ Reminder.REMINDER_TITLE + " = '" + reminderTitle + "' AND " + Reminder.TEACHER_ID + " = " + CurrentUser.getUserID() + ";";

        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        Reminder reminder = new Reminder();
        while(true) {
            assert resultSet != null;
            if (!resultSet.next()) break;
            reminder.setReminderID(resultSet.getInt(Reminder.REMINDER_ID));
            reminder.setReminderTitle(resultSet.getString(Reminder.REMINDER_TITLE));
            reminder.setReminderContent(resultSet.getString(Reminder.REMINDER_CONTENT));
            reminder.setReminderStatus(resultSet.getString(Reminder.REMINDER_STATUS));
        }
        return reminder;
    }

    // adding a reminder to the database
    public static int addReminder(int TeacherID, String title, String content, String status) throws SQLException {
        String query = "INSERT INTO reminders(teacher_id, title, content, status) VALUES (?, ?, ?, ?)";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, TeacherID);
        preparedStatement.setString(2, title);
        preparedStatement.setString(3, content);
        preparedStatement.setString(4, status);
        return preparedStatement.executeUpdate();
    }

    // deleting a reminder from the database based on its id
    public static void deleteReminder(int reminderID) throws SQLException {
        String query = "DELETE FROM reminders WHERE reminder_id = ?;";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, reminderID);
        preparedStatement.executeUpdate();
    }

    // updating the details of a reminder
    public static int editReminder(String title, String content, String status, int ReminderID) throws SQLException {
        String query = "UPDATE reminders SET title = ?, content = ?, status = ? WHERE reminder_id = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, title);
        preparedStatement.setString(2, content);
        preparedStatement.setString(3, status);
        preparedStatement.setInt(4, ReminderID);
        return preparedStatement.executeUpdate();
    }

    // checking to see if a reminder with the specified title already exists in the database
    public static int titleExist(String title) throws SQLException {
        String query = "SELECT COUNT(" + Reminder.REMINDER_TITLE + ") FROM " + DBConstants.TABLE_REMINDERS +
                " WHERE " + Reminder.REMINDER_TITLE + " = '" + title + "' AND " + Reminder.TEACHER_ID + " = " + CurrentUser.getUserID() + ";";

        return DBUtil.getInstance().counter(query);
    }
}
