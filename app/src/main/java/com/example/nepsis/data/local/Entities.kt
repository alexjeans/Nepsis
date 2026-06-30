package com.example.nepsis.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey val id: String,
    val email: String,
    val fullName: String,
    val avatarUrl: String?,
    val level: Int,
    val points: Int,
    // NUEVOS CAMPOS AÑADIDOS:
    val age: Int? = null,
    val gender: String? = null,
    val goal: String? = null
)

@Entity(tableName = "daily_moods")
data class DailyMoodEntity(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val userId: String,
    val energyLevel: Int,
    val stressLevel: Int,
    val emotionalState: String,
    val date: String,
    val isSynced: Boolean = false // Offline-first flag
)

@Entity(tableName = "tests")
data class TestEntity(
    @PrimaryKey val id: String,
    val category: String,
    val title: String,
    val description: String
)

@Entity(tableName = "test_results")
data class TestResultEntity(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val userId: String,
    val testId: String,
    val totalScore: Int,
    val resultText: String,
    val answersJson: String = "{}", // NUEVO: Guarda las respuestas
    val createdAt: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
)