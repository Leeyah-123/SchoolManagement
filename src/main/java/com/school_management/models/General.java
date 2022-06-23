package com.school_management.models;

public class General {
    public static final String GENERAL_RECORD_ID = "record_id";
    public static final String GENERAL_TEACHER_ID = "teacher_id";
    public static final String GENERAL_SUBJECT_NAME = "subject_name";
    public static final String GENERAL_CLASS_NAME = "class_name";

    private int recordID;
    private int teacherID;
    private String subjectName;
    private String className;

    public General() {
    }

    public General(int recordID, int teacherID, String subjectName, String className) {
        this.recordID = recordID;
        this.teacherID = teacherID;
        this.subjectName = subjectName;
        this.className = className;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
