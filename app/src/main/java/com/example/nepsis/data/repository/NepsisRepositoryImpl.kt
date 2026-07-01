package com.example.nepsis.data.repository

import com.example.nepsis.core.network.SupabaseService
import com.example.nepsis.core.utils.Resource
import com.example.nepsis.data.local.dao.NepsisDao
import com.example.nepsis.data.local.entity.DailyMoodEntity
import com.example.nepsis.data.local.entity.TestEntity
import com.example.nepsis.data.local.entity.TestResultEntity
import com.example.nepsis.data.mapper.toDto
import com.example.nepsis.data.mapper.toEntity
import com.example.nepsis.domain.repository.NepsisRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

class NepsisRepositoryImpl(
    private val api: SupabaseService,
    private val dao: NepsisDao
) : NepsisRepository {

    override fun getLocalMoods(userId: String): Flow<List<DailyMoodEntity>> = dao.getDailyMoodsByUserId(userId)

    override fun getLocalTestResults(): Flow<List<TestResultEntity>> = dao.getAllTestResults()

    override fun getTestById(testId: String): Flow<TestEntity?> = dao.getTestById(testId)

    override suspend fun saveDailyMood(mood: DailyMoodEntity, token: String): Resource<Unit> {
        return try {
            dao.insertDailyMood(mood.copy(isSynced = false))
            val response = api.insertDailyMoods("Bearer $token", listOf(mood.toDto()))
            if (response.isSuccessful) {
                dao.updateDailyMood(mood.copy(isSynced = true))
            }
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Success(Unit) 
        }
    }

    override suspend fun saveTestResult(result: TestResultEntity, token: String): Resource<Unit> {
        return try {
            dao.insertTestResult(result.copy(isSynced = false))
            val response = api.insertTestResults("Bearer $token", listOf(result.toDto()))
            if (response.isSuccessful) {
                dao.updateTestResult(result.copy(isSynced = true))
            }
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Success(Unit)
        }
    }

    override suspend fun syncData(userId: String, token: String): Resource<Unit> {
        return try {
            // --- 1. SUBIR LO PENDIENTE (Local -> Nube) ---
            val unsyncedMoods = dao.getUnsyncedMoods()
            if (unsyncedMoods.isNotEmpty()) {
                val response = api.insertDailyMoods("Bearer $token", unsyncedMoods.map { it.toDto() })
                if (response.isSuccessful) {
                    unsyncedMoods.forEach { dao.updateDailyMood(it.copy(isSynced = true)) }
                }
            }

            val unsyncedResults = dao.getUnsyncedResults()
            if (unsyncedResults.isNotEmpty()) {
                val response = api.insertTestResults("Bearer $token", unsyncedResults.map { it.toDto() })
                if (response.isSuccessful) {
                    unsyncedResults.forEach { dao.updateTestResult(it.copy(isSynced = true)) }
                }
            }

            // --- 2. DESCARGAR LO NUEVO (Nube -> Local) ---
            val remoteMoods = api.getDailyMoods("Bearer $token", userId)
            if (remoteMoods.isSuccessful) {
                remoteMoods.body()?.forEach { dao.insertDailyMood(it.toEntity()) }
            }

            // Descarga de tests dinámicos
            val testsResponse = api.getTests("Bearer $token")
            if (testsResponse.isSuccessful) {
                val tests = testsResponse.body()?.map { it.toEntity() } ?: emptyList()
                dao.insertTests(tests)
            }

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de sincronización")
        }
    }
}
