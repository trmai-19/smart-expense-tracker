package com.smartexpense.api.infrastructure.config

import com.smartexpense.api.domain.model.Expense
import com.smartexpense.api.domain.model.Notification
import com.smartexpense.api.domain.model.User
import com.smartexpense.api.domain.repository.ExpenseRepository
import com.smartexpense.api.domain.repository.NotificationRepository
import com.smartexpense.api.domain.repository.UserRepository
import com.smartexpense.api.application.port.out.PasswordEncoderPort
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDateTime

@Component
class DataSeeder(
    private val userRepository: UserRepository,
    private val expenseRepository: ExpenseRepository,
    private val notificationRepository: NotificationRepository,
    private val passwordEncoderPort: PasswordEncoderPort
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        if (!userRepository.existsByEmail("test@gmail.com")) {
            println("Seeding initial data for testing...")

            // Create Mock User
            val user = User(
                email = "test@gmail.com",
                passwordHash = passwordEncoderPort.encode("123456"),
                displayName = "Test User",
                monthlyBudget = BigDecimal.valueOf(5000000),
                streakDays = 5,
                themeColor = "#FFE600",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )

            val savedUser = userRepository.save(user)

            // Create Mock Expenses
            val expense1 = Expense(
                userId = savedUser.id!!,
                amount = BigDecimal.valueOf(50000),
                category = "Ăn uống",
                photoUrl = "https://images.unsplash.com/photo-1582878826629-29b7ad1cb438?w=500&q=80",
                caption = "Ăn phở sáng",
                expenseDate = LocalDateTime.now().minusDays(1),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )

            val expense2 = Expense(
                userId = savedUser.id!!,
                amount = BigDecimal.valueOf(150000),
                category = "Mua sắm",
                photoUrl = "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=500&q=80",
                caption = "Mua áo thun",
                expenseDate = LocalDateTime.now().minusHours(5),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )

            val expense3 = Expense(
                userId = savedUser.id!!,
                amount = BigDecimal.valueOf(30000),
                category = "Di chuyển",
                photoUrl = "https://images.unsplash.com/photo-1549317661-bd32c8ce0db2?w=500&q=80",
                caption = "Đổ xăng",
                expenseDate = LocalDateTime.now().minusDays(2),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )

            val expense4 = Expense(
                userId = savedUser.id!!,
                amount = BigDecimal.valueOf(500000),
                category = "Nhà cửa",
                photoUrl = "https://images.unsplash.com/photo-1584622650111-993a426fbf0a?w=500&q=80",
                caption = "Đóng tiền điện",
                expenseDate = LocalDateTime.now().minusDays(3),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )

            val expense5 = Expense(
                userId = savedUser.id!!,
                amount = BigDecimal.valueOf(80000),
                category = "Giải trí",
                photoUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=500&q=80",
                caption = "Xem phim",
                expenseDate = LocalDateTime.now().minusDays(4),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )

            val expense6 = Expense(
                userId = savedUser.id!!,
                amount = BigDecimal.valueOf(120000),
                category = "Ăn uống",
                photoUrl = "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=500&q=80",
                caption = "Ăn lẩu",
                expenseDate = LocalDateTime.now().minusDays(5),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )

            val expense7 = Expense(
                userId = savedUser.id!!,
                amount = BigDecimal.valueOf(25000),
                category = "Ăn uống",
                photoUrl = "https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=500&q=80",
                caption = "Uống cafe",
                expenseDate = LocalDateTime.now().minusDays(6),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )

            val expense8 = Expense(
                userId = savedUser.id!!,
                amount = BigDecimal.valueOf(60000),
                category = "Sức khỏe",
                photoUrl = "https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?w=500&q=80",
                caption = "Mua thuốc",
                expenseDate = LocalDateTime.now().minusDays(7),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )

            val expense9 = Expense(
                userId = savedUser.id!!,
                amount = BigDecimal.valueOf(200000),
                category = "Giáo dục",
                photoUrl = "https://images.unsplash.com/photo-1497633762265-9d179a990aa6?w=500&q=80",
                caption = "Mua sách",
                expenseDate = LocalDateTime.now().minusDays(8),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )

            expenseRepository.save(expense1)
            expenseRepository.save(expense2)
            expenseRepository.save(expense3)
            expenseRepository.save(expense4)
            expenseRepository.save(expense5)
            expenseRepository.save(expense6)
            expenseRepository.save(expense7)
            expenseRepository.save(expense8)
            expenseRepository.save(expense9)

            // Create Mock Notifications
            val noti1 = Notification(
                userId = savedUser.id!!,
                type = "SYSTEM",
                content = "Chào mừng bạn đến với Smart Expense Tracker!",
                isRead = false,
                createdAt = LocalDateTime.now().minusDays(1)
            )

            val noti2 = Notification(
                userId = savedUser.id!!,
                type = "ALERT",
                content = "Bạn đã vượt quá 80% ngân sách tháng này.",
                isRead = false,
                createdAt = LocalDateTime.now().minusHours(5)
            )

            val noti3 = Notification(
                userId = savedUser.id!!,
                type = "REMINDER",
                content = "Đừng quên ghi chép chi tiêu hôm nay nhé!",
                isRead = true,
                createdAt = LocalDateTime.now().minusMinutes(30)
            )

            notificationRepository.save(noti1)
            notificationRepository.save(noti2)
            notificationRepository.save(noti3)

            println("Data seeding completed successfully!")
        }
    }
}
