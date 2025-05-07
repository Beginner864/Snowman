package com.example.musicapp.config

import com.example.musicapp.security.JwtAuthenticationFilter
import com.example.musicapp.service.CustomUserDetailsService
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class SecurityConfig(
    private val customUserDetailsService: CustomUserDetailsService,
    private val jwtProperties: JwtProperties  // JwtProperties 클래스 주입
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { csrf -> csrf.disable() }
            .addFilterBefore(JwtAuthenticationFilter(jwtProperties.secret, customUserDetailsService), UsernamePasswordAuthenticationFilter::class.java)  // jwtProperties.secret 전달
            .authorizeHttpRequests { authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/auth/register", "/auth/login").permitAll()
                    .anyRequest().authenticated()
            }
        return http.build()
    }

    @Throws(Exception::class)
    @Bean
    fun authenticationManager(http: HttpSecurity): AuthenticationManager {
        val authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder())
        return authenticationManagerBuilder.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder() // 비밀번호 암호화
    }
}













