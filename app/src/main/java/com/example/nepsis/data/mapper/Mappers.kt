package com.example.nepsis.data.mapper

import com.example.nepsis.data.local.entity.DailyMoodEntity
import com.example.nepsis.data.local.entity.ProfileEntity
import com.example.nepsis.data.local.entity.TestEntity
import com.example.nepsis.data.local.entity.TestResultEntity


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
    category = category ?: "General",
    title = title,
    description = description ?: "",
    questionsJson = com.google.gson.Gson().toJson(this.questionsJson ?: emptyList<Any>())
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
    answersJson = answersJson ?: "{}",
    isSynced = true
)

// --- Profile ---
fun ProfileDto.toEntity() = ProfileEntity(
    id = id,
    email = email ?: "Sin correo",
    fullName = fullName ?: "Usuario",
    avatarUrl = avatarUrl,
    level = level,
    points = points
)