package com.smartexpense.android.data.remote.dto.request;

public class ExpenseRequestDto {
    private double amount;
    private String category;
    private String photoUrl;
    private String caption;
    private String expenseDate;

    public ExpenseRequestDto(double amount, String category, String photoUrl, String caption, String expenseDate) {
        this.amount = amount;
        this.category = category;
        this.photoUrl = photoUrl;
        this.caption = caption;
        this.expenseDate = expenseDate;
    }
}
