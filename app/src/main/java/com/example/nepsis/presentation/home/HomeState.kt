package com.example.nepsis.presentation.home

import com.example.nepsis.data.local.entity.DailyMoodEntity

data class HomeState(
    val isLoading: Boolean = false,
    val moods: List<DailyMoodEntity> = emptyList(),
    val error: String? = null
)