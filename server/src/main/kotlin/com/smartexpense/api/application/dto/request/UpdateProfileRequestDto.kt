package com.smartexpense.api.application.dto.request

import java.math.BigDecimal

data class UpdateProfileRequestDto(
    val displayName: String? = null,
    val avatarUrl: String? = null,
    val monthlyBudget: BigDecimal? = null,
    val themeColor: String? = null
)
