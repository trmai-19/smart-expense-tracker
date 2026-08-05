package com.smartexpense.android.data.model;

import java.io.Serializable;

public class NotificationItem implements Serializable {

    public enum Type {
        BUDGET_ALERT,
        AI_INSIGHT,
        REMINDER,
        WEEKLY_REPORT,
        SYSTEM
    }

    private String id;
    private String title;
    private String message;
    private String timeAgo;
    private Type type;
    private boolean isRead;
    private Integer targetTab; // 0: Widget Grid, 1: Dashboard, 2: Camera, 3: AI Chat, null: None

    public NotificationItem(String id, String title, String message, String timeAgo, Type type, boolean isRead, Integer targetTab) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.timeAgo = timeAgo;
        this.type = type;
        this.isRead = isRead;
        this.targetTab = targetTab;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public Type getType() {
        return type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Integer getTargetTab() {
        return targetTab;
    }
}
