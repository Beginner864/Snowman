package com.example.musicapp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        // 모든 URL에 대해 CORS 설정
        registry.addMapping("/**")
            .allowedOrigins("http://localhost", "https://snowman-0yn3.onrender.com")  // 허용할 도메인
            .allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP 메서드
            .allowedHeaders("*")  // 허용할 헤더
            .allowCredentials(true)  // 자격 증명(쿠키 등)을 허용할지 여부
    }
}
