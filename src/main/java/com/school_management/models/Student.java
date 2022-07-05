package com.school_management.models;

import javafx.scene.control.cell.PropertyValueFactory;

public class Student {
    public static final String STUDENT_ID = "student_id";
    public static final String STUDENT_FIRST_NAME = "first_name";
    public static final String STUDENT_LAST_NAME = "last_name";
    public static final String STUDENT_GENDER = "gender";
    public static final String STUDENT_EMAIL_ADDRESS = "email_address";
    public static final String STUDENT_NUMBER = "mobile_number";
    public static final String STUDENT_CLASS = "class";
    public static final String STUDENT_REG_DATE = "reg_date";
    public static final String STUDENT_FEE = "amount_paid";
    public static final String STUDENT_BALANCE = "fee_balance";
    public static final String UPDATED_AT = "updated_at";

    private int studentID;
    private String amountPaid, balance, className, gender, email, firstName, lastName, mobileNumber, regDate, updatedAt;

    public Student() {
    }

    public Student(int studentID, String amountPaid, String balance, String className, String gender, String email, String firstName, String lastName, String mobileNumber, String regDate, String updatedAt) {
        this.studentID = studentID;
        this.amountPaid = amountPaid;
        this.balance = balance;
        this.email = email;
        this.className = className;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.regDate = regDate;
        this.updatedAt = updatedAt;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
