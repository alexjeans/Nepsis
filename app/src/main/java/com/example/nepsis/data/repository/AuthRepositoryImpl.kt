package com.example.nepsis.data.repository

import com.example.nepsis.core.network.SupabaseService
import com.example.nepsis.core.utils.Resource
import com.example.nepsis.data.remote.dto.SupabaseAuthRequest
import com.example.nepsis.data.remote.dto.SupabaseAuthResponse
import com.example.nepsis.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: SupabaseService
) : AuthRepository {
    override suspend fun loginWithGoogle(idToken: String): Resource<SupabaseAuthResponse> {
        return try {
            val response = api.loginWithGoogle(SupabaseAuthRequest(idToken = idToken))
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }
}