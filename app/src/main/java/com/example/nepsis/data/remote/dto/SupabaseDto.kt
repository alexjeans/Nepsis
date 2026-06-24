package com.example.nepsis.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SupabaseAuthRequest(
    @SerializedName("id_token") val idToken: String,
    @SerializedName("provider") val provider: String = "google"
)

data class SupabaseAuthResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("user") val user: SupabaseUser
)

data class SupabaseUser(
    @SerializedName("id") val id: String
)

data class DailyMoodDto(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("energy_level") val energyLevel: Int,
    @SerializedName("stress_level") val stressLevel: Int,
    @SerializedName("emotional_state") val emotionalState: String,
    @SerializedName("date") val date: String
)

data class TestDto(
    @SerializedName("id") val id: String,
    @SerializedName("category") val category: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String
)

data class TestResultDto(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("test_id") val testId: String,
    @SerializedName("total_score") val totalScore: Int,
    @SerializedName("result_text") val resultText: String
)