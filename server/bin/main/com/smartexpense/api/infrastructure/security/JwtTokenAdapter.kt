package com.smartexpense.api.infrastructure.security

import com.smartexpense.api.application.port.out.JwtTokenPort
import com.smartexpense.api.domain.model.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtTokenAdapter : JwtTokenPort {

    @Value("\${jwt.secret}")
    private lateinit var jwtSecret: String

    @Value("\${jwt.expiration-ms}")
    private var jwtExpirationMs: Int = 0

    @Value("\${jwt.refresh-expiration-ms:2592000000}")
    private var jwtRefreshExpirationMs: Long = 2592000000

    private fun getSigningKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(jwtSecret)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    override fun generateToken(user: User): String {
        return Jwts.builder()
            .subject(user.email)
            .claim("id", user.id)
            .issuedAt(Date())
            .expiration(Date(Date().time + jwtExpirationMs))
            .signWith(getSigningKey())
            .compact()
    }

    override fun generateRefreshToken(user: User): String {
        return Jwts.builder()
            .subject(user.email)
            .issuedAt(Date())
            .expiration(Date(Date().time + jwtRefreshExpirationMs))
            .signWith(getSigningKey())
            .compact()
    }

    override fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun getEmailFromToken(token: String): String {
        val claims = Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .payload
        return claims.subject
    }
}
