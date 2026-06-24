package com.example.nepsis.data.mapper

import com.example.nepsis.data.local.entity.*
import com.example.nepsis.data.remote.dto.*

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
    category = category,
    title = title,
    description = description
)

// --- Test Results ---
fun TestResultEntity.toDto() = TestResultDto(
    id = id,
    userId = userId,
    testId = testId,
    totalScore = totalScore,
    resultText = resultText
)

fun TestResultDto.toEntity() = TestResultEntity(
    id = id,
    userId = userId,
    testId = testId,
    totalScore = totalScore,
    resultText = resultText,
    isSynced = true
)