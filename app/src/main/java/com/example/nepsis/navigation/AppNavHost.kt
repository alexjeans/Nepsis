package com.example.nepsis.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.nepsis.core.di.ServiceLocator
import com.example.nepsis.presentation.history.HistoryScreen
import com.example.nepsis.presentation.home.DailyCheckInScreen
import com.example.nepsis.presentation.home.HomeScreen
import com.example.nepsis.presentation.home.HomeViewModel
import com.example.nepsis.presentation.home.HomeViewModelFactory
import com.example.nepsis.presentation.login.LoginScreen
import com.example.nepsis.presentation.login.LoginViewModel
import com.example.nepsis.presentation.login.LoginViewModelFactory
import com.example.nepsis.presentation.profile.ProfileScreen
import com.example.nepsis.presentation.test.TestScreen

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
            val context = LocalContext.current
            val sessionManager = ServiceLocator.provideSessionManager(context)
            
            LaunchedEffect(Unit) {
                if (sessionManager.isLoggedIn()) {
                    navController.navigate(AppDestinations.Home.route) {
                        popUpTo(AppDestinations.Splash.route) { inclusive = true }
                    }
                } else {
                    navController.navigate(AppDestinations.Login.route) {
                        popUpTo(AppDestinations.Splash.route) { inclusive = true }
                    }
                }
            }
        }

        composable(AppDestinations.Login.route) {
            val context = LocalContext.current
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(
                    repository = ServiceLocator.provideAuthRepository(),
                    sessionManager = ServiceLocator.provideSessionManager(context)
                )
            )
            
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(AppDestinations.Home.route) {
                        popUpTo(AppDestinations.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // --- 2. MAIN FLOW (BOTTOM NAV) ---
        composable(AppDestinations.Home.route) {
            val context = LocalContext.current
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(
                    repository = ServiceLocator.provideNepsisRepository(context),
                    sessionManager = ServiceLocator.provideSessionManager(context)
                )
            )

            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToDailyCheckIn = {
                    navController.navigate(AppDestinations.DailyCheckIn.route)
                }
            )
        }

        composable(AppDestinations.TestLibrary.route) {
            TestScreen(
                onNavigateToDetail = { testId ->
                    navController.navigate(AppDestinations.TestDetail.createRoute(testId))
                }
            )
        }

        composable(AppDestinations.History.route) {
            HistoryScreen()
        }

        composable(AppDestinations.Profile.route) {
            val context = LocalContext.current
            val profileViewModel: com.example.nepsis.presentation.profile.ProfileViewModel = viewModel(
                factory = com.example.nepsis.presentation.profile.ProfileViewModelFactory(
                    repository = ServiceLocator.provideProfileRepository(context),
                    sessionManager = ServiceLocator.provideSessionManager(context)
                )
            )
            ProfileScreen(viewModel = profileViewModel)
        }

        // --- 3. SUB-FLOWS (PANTALLA COMPLETA) ---
        composable(AppDestinations.DailyCheckIn.route) {
            val context = LocalContext.current
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(
                    repository = ServiceLocator.provideNepsisRepository(context),
                    sessionManager = ServiceLocator.provideSessionManager(context)
                )
            )
            
            DailyCheckInScreen(
                viewModel = homeViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
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
