package com.smartexpense.android.data.remote.dto.response;

public class NotificationResponseDto {
    private String id;
    private String type;
    private String content;
    private boolean isRead;
    private String createdAt;

    public String getId() { return id; }
    public String getType() { return type; }
    public String getContent() { return content; }
    public boolean getIsRead() { return isRead; }
    public String getCreatedAt() { return createdAt; }
}
