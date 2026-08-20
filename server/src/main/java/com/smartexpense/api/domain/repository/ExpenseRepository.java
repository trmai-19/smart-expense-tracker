package com.smartexpense.api.domain.repository;

import com.smartexpense.api.domain.model.Expense;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseRepository {
    List<Expense> findAllByUserIdOrderByExpenseDateDesc(UUID userId);
    Optional<Expense> findByIdAndUserId(UUID id, UUID userId);
    Expense save(Expense expense);
    void deleteByIdAndUserId(UUID id, UUID userId);
}
