package com.example.nepsis.domain.repository

import com.example.nepsis.core.utils.Resource
import com.example.nepsis.data.local.entity.DailyMoodEntity
import com.example.nepsis.data.local.entity.TestResultEntity
import kotlinx.coroutines.flow.Flow

interface NepsisRepository {
    // Lectura local (siempre rápida, sin internet)
    fun getLocalMoods(): Flow<List<DailyMoodEntity>>
    
    // Guardado local e intento de sincronización
    suspend fun saveDailyMood(mood: DailyMoodEntity, token: String): Resource<Unit>
    suspend fun saveTestResult(result: TestResultEntity, token: String): Resource<Unit>
    
    // Sincronización maestra (Llama a Supabase y actualiza Room)
    suspend fun syncData(userId: String, token: String): Resource<Unit>
}