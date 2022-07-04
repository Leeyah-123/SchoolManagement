package com.school_management.models;

public class Subject {
    public static final String SUBJECT_ID = "subject_id";
    public static final String SUBJECT_NAME = "subject_name";

    int subjectID;
    String subjectName;

    public Subject() {
    }

    public Subject(int subjectID, String subjectName) {
        this.subjectID = subjectID;
        this.subjectName = subjectName;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
