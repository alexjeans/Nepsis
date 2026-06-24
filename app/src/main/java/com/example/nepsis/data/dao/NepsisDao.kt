package com.example.nepsis.data.local.dao

import androidx.room.*
import com.example.nepsis.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NepsisDao {
    // Perfil
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity): Long

    @Query("SELECT * FROM profiles LIMIT 1")
    fun getProfile(): Flow<ProfileEntity?>

    // Daily Moods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyMood(mood: DailyMoodEntity): Long

    @Query("SELECT * FROM daily_moods ORDER BY date DESC")
    fun getAllDailyMoods(): Flow<List<DailyMoodEntity>>

    @Query("SELECT * FROM daily_moods WHERE isSynced = 0")
    suspend fun getUnsyncedMoods(): List<DailyMoodEntity>

    @Update
    suspend fun updateDailyMood(mood: DailyMoodEntity): Int

    // Tests & Resultados
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTest(test: TestEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestResult(result: TestResultEntity): Long

    @Query("SELECT * FROM test_results ORDER BY createdAt DESC")
    fun getAllTestResults(): Flow<List<TestResultEntity>>

    @Query("SELECT * FROM test_results WHERE isSynced = 0")
    suspend fun getUnsyncedResults(): List<TestResultEntity>

    @Update
    suspend fun updateTestResult(result: TestResultEntity): Int
}