package com.example.musicapp.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.filter.OncePerRequestFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.userdetails.UserDetailsService

class JwtAuthenticationFilter(
    private val secretKey: String,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {

        val token = request.getHeader("Authorization")?.takeIf { it.startsWith("Bearer ") }?.substring(7)

        if (token != null) {
            try {
                val algorithm = Algorithm.HMAC256(secretKey)
                val decodedJWT = JWT.require(algorithm).build().verify(token)

                val username = decodedJWT.subject

                // ✅ 여기서 사용자 정보를 DB에서 조회
                val userDetails = userDetailsService.loadUserByUsername(username)

                val authentication = UsernamePasswordAuthenticationToken(
                    userDetails,  // principal
                    null,
                    userDetails.authorities
                )
                SecurityContextHolder.getContext().authentication = authentication

            } catch (e: Exception) {
                logger.error("JWT validation failed: ${e.message}")
                SecurityContextHolder.clearContext()
            }
        }

        chain.doFilter(request, response)
    }
}






