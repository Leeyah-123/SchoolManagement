package com.school_management.models;

public class Reminder {
    public static final String REMINDER_ID = "reminder_id";
    public static final String TEACHER_ID = "teacher_id";
    public static final String REMINDER_TITLE = "title";
    public static final String REMINDER_CONTENT = "content";
    public static final String REMINDER_STATUS = "status";

    private int reminderID, teacherID;
    private String reminderTitle, reminderContent, reminderStatus;

    public Reminder() {
    }

    public Reminder(int reminderID, int teacherID, String reminderTitle, String reminderContent, String reminderStatus) {
        this.reminderID = reminderID;
        this.teacherID = teacherID;
        this.reminderTitle = reminderTitle;
        this.reminderContent = reminderContent;
        this.reminderStatus = reminderStatus;
    }

    public int getReminderID() {
        return reminderID;
    }

    public void setReminderID(int reminderID) {
        this.reminderID = reminderID;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    public String getReminderTitle() {
        return reminderTitle;
    }

    public void setReminderTitle(String reminderTitle) {
        this.reminderTitle = reminderTitle;
    }

    public String getReminderContent() {
        return reminderContent;
    }

    public void setReminderContent(String reminderContent) {
        this.reminderContent = reminderContent;
    }

    public String getReminderStatus() {
        return reminderStatus;
    }

    public void setReminderStatus(String reminderStatus) {
        this.reminderStatus = reminderStatus;
    }
}
