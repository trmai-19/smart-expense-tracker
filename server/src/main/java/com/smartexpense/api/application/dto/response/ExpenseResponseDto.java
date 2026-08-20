package com.smartexpense.api.application.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ExpenseResponseDto {
    private UUID id;
    private BigDecimal amount;
    private String category;
    private String photoUrl;
    private String caption;
    private LocalDateTime expenseDate;
    private LocalDateTime createdAt;
}
