package com.smartexpense.api.infrastructure.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class AppConfig {

    /**
     * Bean RestTemplate dùng cho HTTP calls ra ngoài (Gemini API, v.v.)
     */
    @Bean
    fun restTemplate(): RestTemplate = RestTemplate()
}
