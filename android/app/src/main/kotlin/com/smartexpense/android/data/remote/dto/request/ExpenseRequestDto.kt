package com.smartexpense.android.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class ExpenseRequestDto(
    @SerializedName("amount") val amount: Double,
    @SerializedName("category") val category: String,
    @SerializedName("photoUrl") val photoUrl: String,
    @SerializedName("caption") val caption: String,
    @SerializedName("expenseDate") val expenseDate: String
)
