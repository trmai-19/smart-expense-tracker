package com.smartexpense.api.application.port.out

import com.smartexpense.api.domain.model.User

interface JwtTokenPort {
    fun generateToken(user: User): String
    fun generateRefreshToken(user: User): String
    fun validateToken(token: String): Boolean
    fun getEmailFromToken(token: String): String
}
