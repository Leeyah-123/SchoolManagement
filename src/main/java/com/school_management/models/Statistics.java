package com.school_management.models;

public class Statistics {
    private int recordID;
    private String subjectName;
    private String className;

    public Statistics() {
    }

    public Statistics(int recordID, String subjectName, String className) {
        this.recordID = recordID;
        this.subjectName = subjectName;
        this.className = className;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
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
