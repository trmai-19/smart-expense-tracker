package com.smartexpense.android.presentation.history

data class ExpenseHistoryItem(
    val id: String,
    val caption: String,
    val amount: String,
    val category: String,
    val timeAgo: String,
    val imageUri: String?
)
