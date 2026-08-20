package com.smartexpense.api.infrastructure.persistence.repository;

import com.smartexpense.api.domain.model.Expense;
import com.smartexpense.api.domain.repository.ExpenseRepository;
import com.smartexpense.api.infrastructure.persistence.entity.ExpenseEntity;
import com.smartexpense.api.infrastructure.persistence.mapper.ExpenseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ExpenseRepositoryImpl implements ExpenseRepository {

    private final ExpenseJpaRepository jpaRepository;
    private final ExpenseMapper mapper;

    @Override
    public List<Expense> findAllByUserIdOrderByExpenseDateDesc(UUID userId) {
        return jpaRepository.findAllByUserIdOrderByExpenseDateDesc(userId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Expense> findByIdAndUserId(UUID id, UUID userId) {
        return jpaRepository.findByIdAndUserId(id, userId)
                .map(mapper::toDomain);
    }

    @Override
    public Expense save(Expense expense) {
        ExpenseEntity entity = mapper.toEntity(expense);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void deleteByIdAndUserId(UUID id, UUID userId) {
        jpaRepository.deleteByIdAndUserId(id, userId);
    }
}
