package com.school_management.models;

public class Expense {
    public static final String EXPENSE_RECORD_ID = "record_id";
    public static final String EXPENSE_DESCRIPTION = "desc";
    public static final String EXPENSE_COST = "cost";
    public static final String EXPENSE_DATE = "date";
    public static final String ITEM_PURCHASED = "item_purchased";

    private int recordID;
    private String description, cost, date, itemPurchased;

    public Expense() {
    }

    public Expense(int recordID, String description, String cost, String date, String itemPurchased) {
        this.recordID = recordID;
        this.description = description;
        this.cost = cost;
        this.date = date;
        this.itemPurchased = itemPurchased;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItemPurchased() {
        return itemPurchased;
    }

    public void setItemPurchased(String itemPurchased) {
        this.itemPurchased = itemPurchased;
    }
}
