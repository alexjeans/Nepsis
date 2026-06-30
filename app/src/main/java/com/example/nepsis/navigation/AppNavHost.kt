package com.example.nepsis.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
import com.example.nepsis.presentation.onboarding.OnboardingScreen
import com.example.nepsis.presentation.onboarding.OnboardingViewModel
import com.example.nepsis.presentation.profile.ProfileScreen
import com.example.nepsis.presentation.test.TestDetailScreen
import com.example.nepsis.presentation.test.TestQuestionsScreen
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
        // --- 1. AUTH & ONBOARDING FLOW ---
        composable(AppDestinations.Splash.route) {
            val context = LocalContext.current
            val sessionManager = remember { ServiceLocator.provideSessionManager(context) }
            val userPrefs = remember { ServiceLocator.provideUserPreferences(context) }
            
            // Recolectamos el estado, initial=null para esperar que DataStore lea el disco
            val isOnboardingCompleted by userPrefs.isOnboardingCompleted.collectAsState(initial = null)
            
            LaunchedEffect(isOnboardingCompleted) {
                if (isOnboardingCompleted != null) {
                    if (sessionManager.isLoggedIn()) {
                        if (isOnboardingCompleted == true) {
                            navController.navigate(AppDestinations.Home.route) {
                                popUpTo(AppDestinations.Splash.route) { inclusive = true }
                            }
                        } else {
                            navController.navigate(AppDestinations.Onboarding.route) {
                                popUpTo(AppDestinations.Splash.route) { inclusive = true }
                            }
                        }
                    } else {
                        navController.navigate(AppDestinations.Login.route) {
                            popUpTo(AppDestinations.Splash.route) { inclusive = true }
                        }
                    }
                }
            }
        }

        composable(AppDestinations.Login.route) {
            val context = LocalContext.current
            val userPrefs = remember { ServiceLocator.provideUserPreferences(context) }
            val isOnboardingCompleted by userPrefs.isOnboardingCompleted.collectAsState(initial = false)

            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(
                    repository = ServiceLocator.provideAuthRepository(),
                    sessionManager = ServiceLocator.provideSessionManager(context)
                )
            )
            
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    if (isOnboardingCompleted) {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Login.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(AppDestinations.Onboarding.route) {
                            popUpTo(AppDestinations.Login.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(AppDestinations.Onboarding.route) {
            val context = LocalContext.current
            val userPrefs = remember { ServiceLocator.provideUserPreferences(context) }
            
            // Inyección manual del ViewModel
            val factory = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return OnboardingViewModel(userPrefs) as T
                }
            }
            val viewModel: OnboardingViewModel = viewModel(factory = factory)

            OnboardingScreen(
                viewModel = viewModel,
                onNavigateToHome = {
                    navController.navigate(AppDestinations.Home.route) {
                        popUpTo(0) { inclusive = true } // Limpiamos todo para que no vuelva atrás
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
            
            TestDetailScreen(
                testId = testId,
                onNavigateBack = { navController.popBackStack() },
                onStartTest = { id ->
                    navController.navigate(AppDestinations.TestQuestions.createRoute(id))
                }
            )
        }

        composable(
            route = AppDestinations.TestQuestions.route,
            arguments = listOf(navArgument("testId") { type = NavType.StringType })
        ) { backStackEntry ->
            val testId = backStackEntry.arguments?.getString("testId") ?: ""
            
            TestQuestionsScreen(
                testId = testId,
                onNavigateBack = { navController.popBackStack() },
                onTestFinished = { score, resultText ->
                    navController.navigate(AppDestinations.TestResult.createRoute(score, resultText)) {
                        popUpTo(AppDestinations.TestDetail.route) { inclusive = true }
                    }
                }
            )
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
            
            com.example.nepsis.presentation.result.ResultScreen(
                score = score,
                resultText = text,
                onNavigateHome = {
                    navController.navigate(AppDestinations.Home.route) {
                        popUpTo(AppDestinations.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}