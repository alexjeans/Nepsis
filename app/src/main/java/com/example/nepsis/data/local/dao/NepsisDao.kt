package com.example.nepsis.data.local.dao

import androidx.room.*
import com.example.nepsis.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
public interface NepsisDao {
    // Perfil
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity): Long

    @Query("SELECT * FROM profiles LIMIT 1")
    fun getProfile(): Flow<ProfileEntity?>

    @Query("SELECT * FROM profiles WHERE id = :userId LIMIT 1")
    fun getProfileByUserId(userId: String): Flow<ProfileEntity?>

    // Daily Moods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyMood(mood: DailyMoodEntity): Long

    @Query("SELECT * FROM daily_moods WHERE userId = :userId ORDER BY date DESC")
    fun getDailyMoodsByUserId(userId: String): Flow<List<DailyMoodEntity>>

    @Query("SELECT * FROM daily_moods WHERE isSynced = 0")
    suspend fun getUnsyncedMoods(): List<DailyMoodEntity>

    @Update
    suspend fun updateDailyMood(mood: DailyMoodEntity): Int

    // Tests & Resultados
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTests(tests: List<TestEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTest(test: TestEntity): Long

    @Query("SELECT * FROM tests WHERE id = :testId LIMIT 1")
    fun getTestById(testId: String): Flow<TestEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestResult(result: TestResultEntity): Long

    @Query("SELECT * FROM test_results ORDER BY date DESC")
    fun getAllTestResults(): Flow<List<TestResultEntity>>

    @Query("SELECT * FROM test_results WHERE userId = :userId ORDER BY date DESC")
    fun getTestResultsByUserId(userId: String): Flow<List<TestResultEntity>>

    @Query("SELECT * FROM test_results WHERE isSynced = 0")
    suspend fun getUnsyncedResults(): List<TestResultEntity>

    @Update
    suspend fun updateTestResult(result: TestResultEntity): Int
}