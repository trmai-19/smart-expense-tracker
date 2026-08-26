package com.smartexpense.android.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class ExpenseResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("category") val category: String,
    @SerializedName("photoUrl") val photoUrl: String,
    @SerializedName("caption") val caption: String,
    @SerializedName("expenseDate") val expenseDate: String
)
