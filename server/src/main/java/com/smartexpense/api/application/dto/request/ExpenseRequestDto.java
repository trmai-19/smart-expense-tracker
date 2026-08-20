package com.smartexpense.api.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExpenseRequestDto {
    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Photo URL is required")
    private String photoUrl;

    private String caption;

    @NotNull(message = "Expense date is required")
    private LocalDateTime expenseDate;
}
