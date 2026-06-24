package com.example.nepsis.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.nepsis.presentation.history.HistoryScreen
import com.example.nepsis.presentation.home.HomeScreen
import com.example.nepsis.presentation.login.LoginScreen
import com.example.nepsis.presentation.profile.ProfileScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.Splash.route,
        modifier = modifier
    ) {
        // --- 1. AUTH FLOW ---
        composable(AppDestinations.Splash.route) {
            LaunchedEffect(Unit) {
                navController.navigate(AppDestinations.Login.route) {
                    popUpTo(AppDestinations.Splash.route) { inclusive = true }
                }
            }
        }

        composable(AppDestinations.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppDestinations.Home.route) {
                        popUpTo(AppDestinations.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // --- 2. MAIN FLOW (BOTTOM NAV) ---
        composable(AppDestinations.Home.route) {
            HomeScreen(
                onIniciarTest = {
                    // Por ahora redirigimos a la librería de tests o directamente al test vocacional
                    navController.navigate(AppDestinations.TestLibrary.route)
                },
                onCerrarSesion = {
                    navController.navigate(AppDestinations.Login.route) {
                        popUpTo(AppDestinations.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AppDestinations.TestLibrary.route) {
            // Placeholder hasta implementar la librería de tests
            Text(text = "Librería de Tests (Próximamente)")
        }

        composable(AppDestinations.History.route) {
            HistoryScreen()
        }

        composable(AppDestinations.Profile.route) {
            ProfileScreen()
        }

        // --- 3. SUB-FLOWS (PANTALLA COMPLETA) ---
        composable(AppDestinations.DailyCheckIn.route) {
            Text(text = "Pantalla Check-In Diario (Próximo paso)")
        }

        composable(
            route = AppDestinations.TestDetail.route,
            arguments = listOf(navArgument("testId") { type = NavType.StringType })
        ) { backStackEntry ->
            val testId = backStackEntry.arguments?.getString("testId") ?: ""
            Text(text = "Detalle del Test ID: $testId")
        }

        composable(
            route = AppDestinations.TestQuestions.route,
            arguments = listOf(navArgument("testId") { type = NavType.StringType })
        ) { backStackEntry ->
            val testId = backStackEntry.arguments?.getString("testId") ?: ""
            Text(text = "Preguntas del Test ID: $testId")
        }

        composable(
            route = AppDestinations.TestResult.route,
            arguments = listOf(
                navArgument("score") { type = NavType.IntType },
                navArgument("resultText") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val text = backStackEntry.arguments?.getString("resultText") ?: ""
            Text(text = "Resultado: $text - Puntuación: $score")
        }
    }
}
