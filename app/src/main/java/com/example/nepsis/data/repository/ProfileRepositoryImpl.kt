package com.example.nepsis.data.repository

import com.example.nepsis.core.network.SupabaseService
import com.example.nepsis.core.utils.Resource
import com.example.nepsis.data.local.dao.NepsisDao
import com.example.nepsis.data.local.entity.ProfileEntity
import com.example.nepsis.data.mapper.toEntity
import com.example.nepsis.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class ProfileRepositoryImpl(
    private val api: SupabaseService,
    private val dao: NepsisDao
) : ProfileRepository {

    override fun getLocalProfile(): Flow<ProfileEntity?> = dao.getProfile()

    override suspend fun fetchAndSaveProfile(userId: String, token: String): Resource<Unit> {
        return try {
            // Supabase usa formato eq.ID para filtrar
            val response = api.getProfile("Bearer $token", "eq.$userId") 
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                val profileDto = response.body()!!.first()
                dao.insertProfile(profileDto.toEntity())
                Resource.Success(Unit)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }
}