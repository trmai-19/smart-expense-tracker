package com.smartexpense.api.application.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpdateProfileRequestDto {
    private String displayName;
    private String avatarUrl;
    private BigDecimal monthlyBudget;
    private String themeColor;
}
