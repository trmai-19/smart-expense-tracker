package com.smartexpense.android.data.remote.dto.response;

public class ExpenseResponseDto {
    private String id;
    private double amount;
    private String category;
    private String photoUrl;
    private String caption;
    private String expenseDate;

    public String getId() { return id; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getPhotoUrl() { return photoUrl; }
    public String getCaption() { return caption; }
    public String getExpenseDate() { return expenseDate; }
}
