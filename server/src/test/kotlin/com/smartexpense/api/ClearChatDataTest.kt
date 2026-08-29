package com.smartexpense.api

import com.smartexpense.api.domain.repository.ChatRepository
import com.smartexpense.api.domain.repository.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import org.springframework.test.annotation.Commit
import com.smartexpense.api.infrastructure.persistence.repository.ChatMessageJpaRepository
import jakarta.persistence.EntityManager

@SpringBootTest
class ClearChatDataTest {

    @Autowired
    lateinit var userRepository: UserRepository
    
    @Autowired
    lateinit var em: EntityManager

    @Test
    @Transactional
    @Commit
    fun clearTestUserChat() {
        val user = userRepository.findByEmail("test@gmail.com")
        if (user != null) {
            em.createNativeQuery("DELETE FROM chat_messages WHERE user_id = :userId")
              .setParameter("userId", user.id)
              .executeUpdate()
            println("Successfully deleted chat messages for test@gmail.com")
        } else {
            println("User test@gmail.com not found")
        }
    }

    @org.springframework.beans.factory.annotation.Autowired
    lateinit var chatUseCase: com.smartexpense.api.application.port.`in`.ChatUseCase

    @Test
    fun testAiResponse() {
        val request = com.smartexpense.api.application.dto.request.ChatRequestDto("Gợi ý tiết kiệm chi tiết, dài 1000 chữ, liệt kê 20 cách")
        val response = chatUseCase.sendMessage("test@gmail.com", request)
        println("AI RESPONSE: \n${response.reply}")
    }
}
