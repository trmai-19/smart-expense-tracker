package com.smartexpense.android.data.remote.dto.response;

public class UserProfileResponseDto {
    private String id;
    private String email;
    private String displayName;
    private String avatarUrl;
    private double monthlyBudget;
    private int streakDays;
    private String themeColor;

    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getDisplayName() { return displayName; }
    public String getAvatarUrl() { return avatarUrl; }
    public double getMonthlyBudget() { return monthlyBudget; }
    public int getStreakDays() { return streakDays; }
    public String getThemeColor() { return themeColor; }
}
