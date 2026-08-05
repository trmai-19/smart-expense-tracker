package com.smartexpense.android.presentation.history;

public class ExpenseHistoryItem {
    private final String id;
    private final String caption;
    private final String amount;
    private final String category;
    private final String timeAgo;
    private final String imageUri;

    public ExpenseHistoryItem(String id, String caption, String amount, String category, String timeAgo, String imageUri) {
        this.id = id;
        this.caption = caption;
        this.amount = amount;
        this.category = category;
        this.timeAgo = timeAgo;
        this.imageUri = imageUri;
    }

    public String getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }

    public String getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public String getImageUri() {
        return imageUri;
    }
}
