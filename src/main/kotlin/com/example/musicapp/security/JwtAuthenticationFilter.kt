package com.example.musicapp.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.filter.OncePerRequestFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value

class JwtAuthenticationFilter : OncePerRequestFilter() {

    @Value("\${JWT_SECRET}")
    private lateinit var secretKey: String  // 비밀 키

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {

        // Authorization 헤더에서 JWT 토큰 추출
        val token = request.getHeader("Authorization")?.takeIf { it.startsWith("Bearer ") }?.substring(7)

        if (token != null) {
            try {
                // JWT 토큰 검증
                val algorithm = Algorithm.HMAC256(secretKey) // HMAC256 알고리즘 사용
                val decodedJWT = JWT.require(algorithm) // 서명 검증을 위한 알고리즘 설정
                    .build()
                    .verify(token) // 토큰 검증

                val username = decodedJWT.subject  // 토큰의 subject (username)

                // 사용자 인증 처리 (UserDetailsService 사용)
                val authorities = listOf(SimpleGrantedAuthority("ROLE_USER")) // 예시로 ROLE_USER 권한 부여
                val authentication = UsernamePasswordAuthenticationToken(username, null, authorities)
                SecurityContextHolder.getContext().authentication = authentication

            } catch (e: Exception) {
                // JWT 토큰 검증 실패 시 처리
                logger.error("JWT validation failed: ${e.message}") // 에러 메시지 로그
                SecurityContextHolder.clearContext() // 인증 실패 시 SecurityContext 초기화
            }
        }

        // 필터 체인 실행 (다음 필터로 요청을 전달)
        chain.doFilter(request, response)
    }
}



