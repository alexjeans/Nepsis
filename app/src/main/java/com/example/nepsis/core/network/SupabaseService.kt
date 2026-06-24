package com.example.nepsis.core.network

import com.example.nepsis.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface SupabaseService {

    @POST("auth/v1/token?grant_type=id_token")
    suspend fun loginWithGoogle(
        @Body request: SupabaseAuthRequest
    ): Response<SupabaseAuthResponse>

    @POST("rest/v1/daily_moods")
    suspend fun insertDailyMoods(
        @Header("Authorization") bearerToken: String,
        @Body moods: List<DailyMoodDto>
    ): Response<Unit>

    @GET("rest/v1/daily_moods?select=*")
    suspend fun getDailyMoods(
        @Header("Authorization") bearerToken: String,
        @Query("user_id") userId: String
    ): Response<List<DailyMoodDto>>

    @GET("rest/v1/tests?select=*")
    suspend fun getTests(
        @Header("Authorization") bearerToken: String
    ): Response<List<TestDto>>

    @POST("rest/v1/test_results")
    suspend fun insertTestResults(
        @Header("Authorization") bearerToken: String,
        @Body results: List<TestResultDto>
    ): Response<Unit>
}