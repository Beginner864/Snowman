package com.example.musicapp.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    var secret: String = ""  // jwt.secret 값을 받는 변수
)
