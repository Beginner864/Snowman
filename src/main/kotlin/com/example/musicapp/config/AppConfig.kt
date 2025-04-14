package com.example.musicapp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:.env")  // .env 파일에서 환경변수 읽기
class AppConfig {

    @Value("\${openai.api-key}")
    private lateinit var apiKey: String

    @Bean
    fun openAiApiKey(): String {
        return apiKey
    }
}
