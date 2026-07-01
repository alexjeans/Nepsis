package com.example.nepsis.domain.repository

import com.example.nepsis.core.utils.Resource
import com.example.nepsis.data.local.entity.DailyMoodEntity
import com.example.nepsis.data.local.entity.TestEntity
import com.example.nepsis.data.local.entity.TestResultEntity
import kotlinx.coroutines.flow.Flow

interface NepsisRepository {
    // Lectura local
    fun getLocalMoods(userId: String): Flow<List<DailyMoodEntity>>
    fun getLocalTestResults(): Flow<List<TestResultEntity>>
    fun getTestById(testId: String): Flow<TestEntity?>
    
    // Guardado local e intento de sincronización
    suspend fun saveDailyMood(mood: DailyMoodEntity, token: String): Resource<Unit>
    suspend fun saveTestResult(result: TestResultEntity, token: String): Resource<Unit>
    
    // Sincronización maestra (Llama a Supabase y actualiza Room)
    suspend fun syncData(userId: String, token: String): Resource<Unit>
}