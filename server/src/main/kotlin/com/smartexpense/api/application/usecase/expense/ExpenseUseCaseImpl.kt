package com.smartexpense.api.application.usecase.expense

import com.fasterxml.jackson.databind.ObjectMapper
import com.smartexpense.api.application.dto.request.ExpenseRequestDto
import com.smartexpense.api.application.dto.response.*
import com.smartexpense.api.application.port.`in`.ExpenseUseCase
import com.smartexpense.api.application.port.`in`.FileUseCase
import com.smartexpense.api.domain.model.Expense
import com.smartexpense.api.domain.model.User
import com.smartexpense.api.domain.repository.ExpenseRepository
import com.smartexpense.api.domain.repository.UserRepository
import com.smartexpense.api.infrastructure.ai.GeminiClient
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.DayOfWeek

@Service
class ExpenseUseCaseImpl(
    private val expenseRepository: ExpenseRepository,
    private val userRepository: UserRepository,
    private val fileUseCase: FileUseCase,
    private val geminiClient: GeminiClient,
    private val objectMapper: ObjectMapper
) : ExpenseUseCase {

    override fun getExpenses(email: String): List<ExpenseResponseDto> {
        val user = getUserByEmail(email)
        return expenseRepository.findAllByUserIdOrderByExpenseDateDesc(user.id!!)
            .map { mapToDto(it) }
    }

    override fun createExpense(email: String, request: ExpenseRequestDto): ExpenseResponseDto {
        val user = getUserByEmail(email)

        val newExpense = Expense(
            userId = user.id!!,
            amount = request.amount,
            category = request.category,
            photoUrl = request.photoUrl,
            caption = request.caption,
            expenseDate = request.expenseDate
        )

        return mapToDto(expenseRepository.save(newExpense))
    }

    override fun analyzeExpense(email: String, file: MultipartFile, caption: String?): AnalyzeExpenseResponseDto {
        val user = getUserByEmail(email)
        val photoUrl = fileUseCase.uploadFile(file)
        
        val jsonString = geminiClient.analyzeReceipt(file.bytes, file.contentType ?: "image/jpeg", caption)
        
        var amount = 0L
        var category = "Khác"
        
        try {
            val node = objectMapper.readTree(jsonString)
            amount = node.get("amount")?.asLong() ?: 0L
            category = node.get("category")?.asText() ?: "Khác"
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return AnalyzeExpenseResponseDto(amount, category, photoUrl)
    }

    override fun getStatistics(email: String, period: String, fromDate: String?, toDate: String?): StatisticsResponseDto {
        val user = getUserByEmail(email)
        val expenses = expenseRepository.findAllByUserIdOrderByExpenseDateDesc(user.id!!)
        
        val filtered = expenses.filter { 
            when (period.uppercase()) {
                "WEEK" -> {
                    val cutoff = LocalDateTime.now().minusDays(7)
                    it.expenseDate.isAfter(cutoff)
                }
                "MONTH" -> {
                    val cutoff = LocalDateTime.now().minusDays(30)
                    it.expenseDate.isAfter(cutoff)
                }
                "YEAR" -> {
                    val currentYear = LocalDateTime.now().year
                    it.expenseDate.year == currentYear
                }
                "CUSTOM" -> {
                    if (fromDate != null && toDate != null) {
                        val from = LocalDateTime.parse(fromDate)
                        val to = LocalDateTime.parse(toDate)
                        val d = it.expenseDate
                        d in from..to
                    } else true
                }
                else -> true
            }
        }

        val totalAmount = filtered.sumOf { it.amount }.toDouble()
        
        val categories = filtered.groupBy { it.category }
            .map { (cat, list) -> CategoryBreakdownDto(cat, list.sumOf { it.amount }.toDouble()) }
            .sortedByDescending { it.amount }

        // Simplify bars: 7 days for WEEK, 4 weeks for MONTH, 12 months for YEAR
        val bars = mutableListOf<BarEntryDto>()
        val now = LocalDateTime.now()
        when (period.uppercase()) {
            "WEEK" -> {
                for (i in 6 downTo 0) {
                    val d = now.minusDays(i.toLong())
                    val sum = filtered.filter { it.expenseDate.toLocalDate() == d.toLocalDate() }.sumOf { it.amount }.toDouble()
                    bars.add(BarEntryDto(d.dayOfWeek.name.take(3), sum))
                }
            }
            "MONTH" -> {
                for (i in 3 downTo 0) {
                    val start = now.minusDays((i * 7 + 7).toLong())
                    val end = now.minusDays((i * 7).toLong())
                    val sum = filtered.filter { 
                        val d = it.expenseDate
                        d.isAfter(start) && !d.isAfter(end)
                    }.sumOf { it.amount }.toDouble()
                    bars.add(BarEntryDto("W${4-i}", sum))
                }
            }
            "YEAR" -> {
                for (i in 1..12) {
                    val sum = filtered.filter { it.expenseDate.monthValue == i }.sumOf { it.amount }.toDouble()
                    bars.add(BarEntryDto("T$i", sum))
                }
            }
        }

        return StatisticsResponseDto(totalAmount, categories, bars)
    }


    private fun getUserByEmail(email: String): User {
        return userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")
    }

    private fun mapToDto(expense: Expense) = ExpenseResponseDto(
        id = expense.id!!,
        amount = expense.amount,
        category = expense.category,
        photoUrl = expense.photoUrl,
        caption = expense.caption,
        expenseDate = expense.expenseDate
    )
}
