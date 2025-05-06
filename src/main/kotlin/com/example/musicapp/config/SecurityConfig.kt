package com.example.musicapp.config

import com.example.musicapp.security.JwtAuthenticationFilter
import com.example.musicapp.service.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customUserDetailsService: CustomUserDetailsService
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // JWT 필터 추가
            .addFilter(JwtAuthenticationFilter())  // JWT 인증 필터 추가
            .authorizeHttpRequests { authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/login", "/register").permitAll() // 로그인과 회원가입은 모두 허용
                    .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
            }
            .formLogin { formLogin ->
                formLogin
                    .loginPage("/login") // 로그인 페이지
                    .defaultSuccessUrl("/home", true) // 로그인 성공 후 이동할 페이지
                    .permitAll() // 로그인 페이지는 모든 사용자에게 허용
            }
            .logout { logout ->
                logout
                    .permitAll() // 로그아웃은 모든 사용자에게 허용
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









