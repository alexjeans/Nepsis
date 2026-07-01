package com.example.nepsis.data.mapper

import com.example.nepsis.data.local.entity.DailyMoodEntity
import com.example.nepsis.data.local.entity.ProfileEntity
import com.example.nepsis.data.local.entity.TestEntity
import com.example.nepsis.data.local.entity.TestResultEntity
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
    isSynced = true 
)

// --- Tests ---
fun TestDto.toEntity(): TestEntity {
    return TestEntity(
        id = this.id,
        category = this.category,
        title = this.title,
        description = this.description ?: "",
        questionsJson = this.questionsJson.toString() 
    )
}

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