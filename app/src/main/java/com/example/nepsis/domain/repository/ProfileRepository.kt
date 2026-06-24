package com.example.nepsis.domain.repository

import com.example.nepsis.core.utils.Resource
import com.example.nepsis.data.local.entity.ProfileEntity
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getLocalProfile(): Flow<ProfileEntity?>
    suspend fun fetchAndSaveProfile(userId: String, token: String): Resource<Unit>
}