package com.school_management.utils;

public final class Constants {
    //fxml files
    public static String MAIN_FXML_DIR = "/com/school_management/fxml/main.fxml";
    // icon
    //    public static final String STAGE_ICON = "/com/inventorymanagement/resources/images/icons8_attach_100px_3.png";
    public static String LOGIN_FXML_DIR = "/com/school_management/fxml/login.fxml";
    public static String SIGNUP_FXML_DIR = "/com/school_management/fxml/signup.fxml";
    public static String ACCOUNTS_FXML_DIR = "/com/school_management/fxml/accounts.fxml";
    public static String CLASSES_FXML_DIR = "/com/school_management/fxml/classes.fxml";
    public static String EXPENSES_FXML_DIR = "/com/school_management/fxml/expenses.fxml";
    public static String PROFILE_FXML_DIR = "/com/school_management/fxml/profile.fxml";
    public static String SESSION_FXML_DIR = "/com/school_management/fxml/session.fxml";
    public static String STUDENTS_FXML_DIR = "/com/school_management/fxml/students.fxml";
    public static String TEACHERS_FXML_DIR = "/com/school_management/fxml/teachers.fxml";
    public static String STATISTICS_FXML_DIR = "/com/school_management/fxml/statistics.fxml";
    public static String DASHBOARD_FXML_DIR = "/com/school_management/fxml/dashboard.fxml";

    //css
    public static String STYLESHEET_DIR = "/com/school_management/styles/styles.css";

    //config
    public static String DATABASE_CONFIG_DIR = "dbConfig.properties";

    // file data
    public static String[] dir = System.getProperty("user.dir").split("/");

    public static String USERDATAFILE = "/" + dir[1] + "/" + dir[2] + "/" + "userData/currentUser.sm";
}