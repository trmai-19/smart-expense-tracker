package com.smartexpense.android.data.remote.dto.request;

public class UpdateProfileRequestDto {
    private String displayName;
    private String avatarUrl;
    private Double monthlyBudget;
    private String themeColor;

    public UpdateProfileRequestDto(String displayName, String avatarUrl, Double monthlyBudget, String themeColor) {
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
        this.monthlyBudget = monthlyBudget;
        this.themeColor = themeColor;
    }
}
