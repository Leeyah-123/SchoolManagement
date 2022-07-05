package com.school_management.models;

public class Class {
    public static final String CLASS_ID = "class_id";
    public static final String CLASS_NAME = "class_name";
    public static final String CLASS_FEE = "class_fee";

    private int classID;
    private String className, classFee;

    public Class() {
    }

    public Class(int classID, String className, String classFee) {
        this.classID = classID;
        this.className = className;
        this.classFee = classFee;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassFee() {
        return classFee;
    }

    public void setClassFee(String classFee) {
        this.classFee = classFee;
    }
}
