package com.smartexpense.android.data.remote.dto.response;

public class AuthResponseDto {
    private String token;
    private UserProfileResponseDto userProfile;

    public String getToken() { return token; }
    public UserProfileResponseDto getUserProfile() { return userProfile; }
}
