package com.smartexpense.api.infrastructure.persistence.mapper;

import com.smartexpense.api.domain.model.Expense;
import com.smartexpense.api.infrastructure.persistence.entity.ExpenseEntity;
import com.smartexpense.api.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {

    public Expense toDomain(ExpenseEntity entity) {
        if (entity == null) {
            return null;
        }
        return Expense.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .amount(entity.getAmount())
                .category(entity.getCategory())
                .photoUrl(entity.getPhotoUrl())
                .caption(entity.getCaption())
                .expenseDate(entity.getExpenseDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public ExpenseEntity toEntity(Expense domain) {
        if (domain == null) {
            return null;
        }
        UserEntity user = new UserEntity();
        user.setId(domain.getUserId());

        return ExpenseEntity.builder()
                .id(domain.getId())
                .user(user)
                .amount(domain.getAmount())
                .category(domain.getCategory())
                .photoUrl(domain.getPhotoUrl())
                .caption(domain.getCaption())
                .expenseDate(domain.getExpenseDate())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
