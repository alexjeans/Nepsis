package com.example.nepsis.domain.repository

import com.example.nepsis.core.utils.Resource
import com.example.nepsis.data.remote.dto.SupabaseAuthResponse

interface AuthRepository {
    suspend fun loginWithGoogle(idToken: String): Resource<SupabaseAuthResponse>
}