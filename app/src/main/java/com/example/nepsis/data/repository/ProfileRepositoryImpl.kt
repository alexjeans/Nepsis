package com.example.nepsis.data.repository

import com.example.nepsis.core.network.SupabaseService
import com.example.nepsis.core.utils.Resource
import com.example.nepsis.data.local.dao.NepsisDao
import com.example.nepsis.data.local.entity.ProfileEntity
import com.example.nepsis.data.mapper.toEntity
import com.example.nepsis.data.remote.dto.ProfileUpdateDto
import com.example.nepsis.domain.repository.ProfileRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

class ProfileRepositoryImpl(
    private val api: SupabaseService,
    private val dao: NepsisDao
) : ProfileRepository {

    override fun getLocalProfile(): Flow<ProfileEntity?> = dao.getProfile()

    override suspend fun fetchAndSaveProfile(userId: String, token: String): Resource<Unit> {
        return try {
            // CORRECCIÓN: Dar 1.5 segundos para que el Trigger de Supabase termine
            delay(1500)

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

    override suspend fun updateRemoteProfile(userId: String, token: String, age: Int, gender: String, goal: String): Resource<Unit> {
        return try {
            // CORRECCIÓN: Eliminamos el bloqueo infinito provocado por Flow.firstOrNull()
            // Se actualiza directamente a Supabase.
            val dto = ProfileUpdateDto(age, gender, goal)
            val response = api.updateProfile("Bearer $token", "eq.$userId", dto)

            if (response.isSuccessful) {
                // Al tener éxito, el ViewModel inmediatamente llama a fetchAndSaveProfile()
                // lo que mantendrá la consistencia de Room de forma segura (Offline-First garantizado).
                Resource.Success(Unit)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }
}