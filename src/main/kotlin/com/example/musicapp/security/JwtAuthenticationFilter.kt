package com.example.musicapp.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import jakarta.annotation.PostConstruct
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.filter.OncePerRequestFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(private val secretKey: String) : OncePerRequestFilter() {

    @PostConstruct
    fun init() {
        println("JWT_SECRET: $secretKey")
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {

        val token = request.getHeader("Authorization")?.takeIf { it.startsWith("Bearer ") }?.substring(7)

        if (token != null) {
            try {
                val algorithm = Algorithm.HMAC256(secretKey)
                val decodedJWT = JWT.require(algorithm)
                    .build()
                    .verify(token)

                val username = decodedJWT.subject

                val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
                val authentication = UsernamePasswordAuthenticationToken(username, null, authorities)
                SecurityContextHolder.getContext().authentication = authentication

            } catch (e: Exception) {
                logger.error("JWT validation failed: ${e.message}")
                SecurityContextHolder.clearContext()
            }
        }

        chain.doFilter(request, response)
    }
}





