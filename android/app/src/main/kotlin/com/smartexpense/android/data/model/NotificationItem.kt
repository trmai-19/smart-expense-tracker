package com.smartexpense.android.data.model

import java.io.Serializable

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val type: Type,
    var isRead: Boolean,
    val targetTab: Int? // 0: Widget Grid, 1: Dashboard, 2: Camera, 3: AI Chat, null: None
) : Serializable {
    enum class Type {
        BUDGET_ALERT,
        AI_INSIGHT,
        REMINDER,
        WEEKLY_REPORT,
        SYSTEM
    }
}
