package com.school_management.models;

public class Account {
    public static final String ACCOUNT_RECORD_ID = "record_id";
    public static final String ACCOUNT_EVENT = "event";
    public static final String ACCOUNT_EVENT_DESCRIPTION = "description";
    public static final String ACCOUNT_AMOUNT = "amount";
    public static final String ACCOUNT_EVENT_DATE = "date";
    public static final String ACCOUNT_BALANCE = "balance";
    public static final String ACCOUNT_SESSION = "session";
    public static final String RECORDED_BY = "recorded_by";

    private int recordID, recordedBy;
    private String event, description, date, session, amount, balance;

    public Account() {

    }

    public Account(int recordID, String amount, String balance, String event, String description, String date, String session, int recordedBy) {
        this.recordID = recordID;
        this.amount = amount;
        this.balance = balance;
        this.event = event;
        this.description = description;
        this.date = date;
        this.session = session;
        this.recordedBy = recordedBy;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public int getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(int recordedBy) {
        this.recordedBy = recordedBy;
    }
}
