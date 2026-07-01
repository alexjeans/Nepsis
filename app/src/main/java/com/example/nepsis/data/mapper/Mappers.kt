package com.example.nepsis.data.mapper

import com.example.nepsis.data.local.entity.*
import com.example.nepsis.data.remote.dto.*
import com.google.gson.Gson

// --- Daily Mood ---
fun DailyMoodEntity.toDto() = DailyMoodDto(
    id = id,
    userId = userId,
    energyLevel = energyLevel,
    stressLevel = stressLevel,
    emotionalState = emotionalState,
    date = date
)

fun DailyMoodDto.toEntity() = DailyMoodEntity(
    id = id,
    userId = userId,
    energyLevel = energyLevel,
    stressLevel = stressLevel,
    emotionalState = emotionalState,
    date = date,
    isSynced = true // Si viene de Supabase, ya está sincronizado
)

// --- Tests ---
fun TestDto.toEntity() = TestEntity(
    id = id,
    title = title,
    description = description ?: "",
    questionsJson = Gson().toJson(this.questionsJson)
)

// --- Test Results ---
fun TestResultEntity.toDto() = TestResultDto(
    id = id,
    userId = userId,
    testId = testId,
    totalScore = totalScore,
    resultText = resultText,
    answersJson = answersJson
)

fun TestResultDto.toEntity() = TestResultEntity(
    id = id,
    userId = userId,
    testId = testId,
    totalScore = totalScore,
    resultText = resultText,
    answersJson = answersJson,
    isSynced = true
)

// --- Profile ---
fun ProfileDto.toEntity() = ProfileEntity(
    id = id,
    email = email,
    fullName = fullName ?: "Usuario",
    avatarUrl = avatarUrl,
    level = level,
    points = points
)
