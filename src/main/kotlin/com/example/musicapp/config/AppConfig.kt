package com.example.musicapp.config

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {

    @Bean
    fun getOpenAiApiKey(): String {
        val dotenv = Dotenv.configure().load()
        return dotenv["OPENAI_API_KEY"] ?: throw IllegalArgumentException("API key not found")
    }
}


