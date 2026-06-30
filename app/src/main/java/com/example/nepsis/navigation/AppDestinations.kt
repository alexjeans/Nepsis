package com.example.nepsis.navigation

sealed class AppDestinations(val route: String) {
    // Auth Flow
    object Splash : AppDestinations("splash")
    object Login : AppDestinations("login")
    object Onboarding : AppDestinations("onboarding")
    
    // Main Flow (Bottom Nav)
    object Home : AppDestinations("home")
    object TestLibrary : AppDestinations("test_library")
    object History : AppDestinations("history")
    object Profile : AppDestinations("profile")
    
    // Test & Check-In Flow (Sin Bottom Nav)
    object DailyCheckIn : AppDestinations("daily_check_in")
    object TestDetail : AppDestinations("test_detail/{testId}") {
        fun createRoute(testId: String) = "test_detail/$testId"
    }
    object TestQuestions : AppDestinations("test_questions/{testId}") {
        fun createRoute(testId: String) = "test_questions/$testId"
    }
    object TestResult : AppDestinations("test_result/{score}/{resultText}") {
        fun createRoute(score: Int, text: String) = "test_result/$score/$text"
    }
}
