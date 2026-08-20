package com.smartexpense.api.infrastructure.persistence.repository;

import com.smartexpense.api.infrastructure.persistence.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpenseJpaRepository extends JpaRepository<ExpenseEntity, UUID> {
    List<ExpenseEntity> findAllByUserIdOrderByExpenseDateDesc(UUID userId);
    
    @Query("SELECT e FROM ExpenseEntity e WHERE e.id = :id AND e.user.id = :userId")
    Optional<ExpenseEntity> findByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);

    void deleteByIdAndUserId(UUID id, UUID userId);
}
