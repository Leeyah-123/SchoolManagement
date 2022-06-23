package com.school_management.dao;

import com.school_management.models.*;
import com.school_management.models.Class;
import com.school_management.utils.DBConstants;
import com.school_management.utils.config.db.DBDataSource;
import javafx.scene.Scene;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {
    /*
     * getting the connection data from the dbconfigclass
     * @Reference DBDataSource()
     * */
    private final String CONNECTION_STRING = DBDataSource.getInstance().getDatasource();
    private final String USER = DBDataSource.getInstance().getUser();
    private final String PASSWORD = DBDataSource.getInstance().getPassword();

    private static final DBConnection instance = new DBConnection();

    public static DBConnection getInstance() {
        return instance;
    }

    public DBConnection() {
        createTables();
    }

    // connection method for connecting to the database
    public Connection connection() {
        try {
            Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Database Connection success");
            return connection;
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }

    // automating table creation
    private void createTables() {
        try {
            Statement statement = connection().createStatement();

            // the users table
            String usersTable = "CREATE TABLE IF NOT EXISTS public." + DBConstants.TABLE_USERS + "(" +
                    User.USER_ID + " SERIAL PRIMARY KEY NOT NULL," +
                    User.USER_FIRST_NAME + " varchar(15) NOT NULL," +
                    User.USER_LAST_NAME + " varchar(15) NOT NULL," +
                    User.USER_EMAIL + " varchar(64) UNIQUE NOT NULL," +
                    User.USER_GENDER + " varchar(6) NOT NULL," +
                    User.USER_NUMBER + " varchar(11) NOT NULL," +
                    User.USER_PASSWORD + " varchar NOT NULL," +
                    User.USER_ROLE + " varchar DEFAULT 'teacher'" +
                    ");";

            // students table
            String studentsTable = "CREATE TABLE IF NOT EXISTS public." + DBConstants.TABLE_STUDENTS + "(" +
                    Student.STUDENT_ID + " SERIAL PRIMARY KEY NOT NULL," +
                    Student.STUDENT_FIRST_NAME + " varchar NOT NULL," +
                    Student.STUDENT_LAST_NAME + " varchar NOT NULL," +
                    Student.STUDENT_GENDER + " varchar(6) NOT NULL," +
                    Student.STUDENT_NUMBER + " varchar NOT NULL," +
                    Student.STUDENT_CLASS + " varchar NOT NULL," +
                    Student.STUDENT_FEE + " money NOT NULL," +
                    Student.STUDENT_REG_DATE + " date NOT NULL DEFAULT CURRENT_DATE," +
                    Student.STUDENT_BALANCE + " money" +
                    ");";

            // accounts table
            String accountsTable = "CREATE TABLE IF NOT EXISTS public." + DBConstants.TABLE_ACCOUNTS + "(" +
                    Account.ACCOUNT_RECORD_ID + " SERIAL PRIMARY KEY NOT NULL," +
                    Account.ACCOUNT_EVENT + " varchar NOT NULL," +
                    Account.ACCOUNT_EVENT_DESCRIPTION + " varchar NOT NULL," +
                    Account.ACCOUNT_EVENT_DATE + " date NOT NULL," +
                    Account.ACCOUNT_BALANCE + " money NOT NULL" +
                    ");";

            // expenses table
            String expensesTable = "CREATE TABLE IF NOT EXISTS public." + DBConstants.TABLE_EXPENSES + "(" +
                    Expense.EXPENSE_RECORD_ID + " SERIAL PRIMARY KEY NOT NULL," +
                    Expense.ITEM_PURCHASED + " varchar NOT NULL,\"" +
                    Expense.EXPENSE_DESCRIPTION + "\" varchar NOT NULL," +
                    Expense.EXPENSE_COST + " money NOT NULL," +
                    Expense.EXPENSE_DATE + " date NOT NULL" +
                    ");";

            // general table
            String generalTable = "CREATE TABLE IF NOT EXISTS public." + DBConstants.TABLE_GENERAL + "(" +
                    General.GENERAL_RECORD_ID + " SERIAL PRIMARY KEY NOT NULL," +
                    General.GENERAL_TEACHER_ID + " int NOT NULL," +
                    General.GENERAL_CLASS_NAME + " varchar NOT NULL," +
                    General.GENERAL_SUBJECT_NAME + " varchar NOT NULL" +
                    ");";

            // reminders table
            String remindersTable = "CREATE TABLE IF NOT EXISTS public." + DBConstants.TABLE_REMINDERS + "(" +
                    Reminder.REMINDER_ID + " SERIAL PRIMARY KEY NOT NULL," +
                    Reminder.TEACHER_ID + " int NOT NULL," +
                    Reminder.REMINDER_TITLE + " varchar NOT NULL," +
                    Reminder.REMINDER_CONTENT + " varchar NOT NULL," +
                    Reminder.REMINDER_STATUS + " varchar NOT NULL DEFAULT 'To Do'" +
                    ");";

            // students table
            String subjectsTable = "CREATE TABLE IF NOT EXISTS public." + DBConstants.TABLE_SUBJECTS + "(" +
                    Subject.SUBJECT_ID + " SERIAL PRIMARY KEY NOT NULL," +
                    Subject.SUBJECT_NAME + " varchar UNIQUE NOT NULL" +
                    ");";

            // classes table
            String classesTable = "CREATE TABLE IF NOT EXISTS public." + DBConstants.TABLE_CLASSES + "(" +
                    Class.CLASS_ID + " SERIAL PRIMARY KEY NOT NULL," +
                    Class.CLASS_NAME + " varchar UNIQUE NOT NULL" +
                    ");";

            // session table
            String sessionTable = "CREATE TABLE IF NOT EXISTS public." + DBConstants.TABLE_SESSION + "(" +
                    Session.SESSION_ID + " SERIAL PRIMARY KEY NOT NULL," +
                    Session.SESSION_NAME + " varchar UNIQUE NOT NULL," +
                    Session.START_DATE + " date NOT NULL," +
                    Session.END_DATE + " date NOT NULL" +
                    ");";

            // executing the "create table" queries
            statement.executeUpdate(usersTable);
            statement.executeUpdate(studentsTable);
            statement.executeUpdate(accountsTable);
            statement.executeUpdate(expensesTable);
            statement.executeUpdate(generalTable);
            statement.executeUpdate(remindersTable);
            statement.executeUpdate(subjectsTable);
            statement.executeUpdate(classesTable);
            statement.executeUpdate(sessionTable);

            Logger.getLogger(getClass().getName()).log(Level.INFO, "All tables successfully loaded");
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            e.printStackTrace();
        }
    }
}