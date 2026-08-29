package com.smartexpense.android.data.remote

import com.smartexpense.android.data.remote.api.AuthApi
import com.smartexpense.android.data.remote.dto.request.RefreshTokenRequestDto
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TokenAuthenticator(private val tokenManager: TokenManager) : Authenticator {

    @Synchronized
    override fun authenticate(route: Route?, response: Response): Request? {
        // If the request itself was a refresh token request, and it failed with 401, don't try to refresh again
        if (response.request.url.encodedPath.endsWith("/api/auth/refresh")) {
            tokenManager.clearToken()
            return null
        }

        val refreshToken = tokenManager.getRefreshToken()
        if (refreshToken.isNullOrEmpty()) {
            return null
        }

        // Use a separate Retrofit instance to avoid circular dependency and interceptor loops
        val retrofit = Retrofit.Builder()
            .baseUrl(RetrofitClient.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val authApi = retrofit.create(AuthApi::class.java)

        try {
            val refreshResponse = authApi.refreshToken(RefreshTokenRequestDto(refreshToken)).execute()

            if (refreshResponse.isSuccessful) {
                val newTokens = refreshResponse.body()
                if (newTokens != null) {
                    tokenManager.saveToken(newTokens.token)
                    tokenManager.saveRefreshToken(newTokens.refreshToken)
                    
                    return response.request.newBuilder()
                        .header("Authorization", "Bearer ${newTokens.token}")
                        .build()
                }
            } else {
                // Refresh token is invalid/expired
                tokenManager.clearToken()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            tokenManager.clearToken()
        }
        
        return null
    }
}
