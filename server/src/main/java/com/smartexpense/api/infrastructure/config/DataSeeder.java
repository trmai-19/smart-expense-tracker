package com.smartexpense.api.infrastructure.config;

import com.smartexpense.api.domain.model.Expense;
import com.smartexpense.api.domain.model.User;
import com.smartexpense.api.domain.repository.ExpenseRepository;
import com.smartexpense.api.domain.repository.UserRepository;
import com.smartexpense.api.application.port.out.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final PasswordEncoderPort passwordEncoderPort;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByEmail("test@gmail.com")) {
            System.out.println("Seeding initial data for testing...");

            // Create Mock User
            User user = User.builder()
                    .email("test@gmail.com")
                    .passwordHash(passwordEncoderPort.encode("123456"))
                    .displayName("Test User")
                    .monthlyBudget(BigDecimal.valueOf(5000000))
                    .streakDays(5)
                    .themeColor("#FFE600")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            User savedUser = userRepository.save(user);

            // Create Mock Expenses
            Expense expense1 = Expense.builder()
                    .userId(savedUser.getId())
                    .amount(BigDecimal.valueOf(50000))
                    .category("Ăn uống")
                    .photoUrl("https://images.unsplash.com/photo-1582878826629-29b7ad1cb438?w=500&q=80")
                    .caption("Ăn phở sáng")
                    .expenseDate(LocalDateTime.now().minusDays(1))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Expense expense2 = Expense.builder()
                    .userId(savedUser.getId())
                    .amount(BigDecimal.valueOf(150000))
                    .category("Mua sắm")
                    .photoUrl("https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=500&q=80")
                    .caption("Mua áo thun")
                    .expenseDate(LocalDateTime.now().minusHours(5))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            expenseRepository.save(expense1);
            expenseRepository.save(expense2);

            System.out.println("Data seeding completed successfully!");
        }
    }
}
