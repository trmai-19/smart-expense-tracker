package com.smartexpense.android.data.remote.dto.request;

public class RegisterRequestDto {
    private String email;
    private String password;
    private String displayName;

    public RegisterRequestDto(String email, String password, String displayName) {
        this.email = email;
        this.password = password;
        this.displayName = displayName;
    }
}
