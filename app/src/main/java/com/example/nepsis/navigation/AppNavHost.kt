package com.example.nepsis.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import com.example.nepsis.presentation.history.HistoryViewModel
import com.example.nepsis.presentation.history.HistoryViewModelFactory
import com.example.nepsis.presentation.home.DailyCheckInScreen
import com.example.nepsis.presentation.home.HomeScreen
import com.example.nepsis.presentation.home.HomeViewModel
import com.example.nepsis.presentation.home.HomeViewModelFactory
import com.example.nepsis.presentation.login.LoginNavigationAction
import com.example.nepsis.presentation.login.LoginScreen
import com.example.nepsis.presentation.login.LoginViewModel
import com.example.nepsis.presentation.login.LoginViewModelFactory
import com.example.nepsis.presentation.onboarding.OnboardingScreen
import com.example.nepsis.presentation.onboarding.OnboardingViewModel
import com.example.nepsis.presentation.profile.ProfileScreen
import com.example.nepsis.presentation.test.TestDetailScreen
import com.example.nepsis.presentation.test.TestQuestionsScreen
import com.example.nepsis.presentation.test.TestScreen
import com.example.nepsis.presentation.test.TestQuestionsViewModel
import com.example.nepsis.presentation.test.TestQuestionsViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.example.nepsis.presentation.settings.SettingsScreen
import com.example.nepsis.presentation.settings.SettingsViewModel
import com.example.nepsis.presentation.settings.SettingsViewModelFactory
import com.example.nepsis.presentation.nepsia.NepsiaChatScreen
import com.example.nepsis.presentation.nepsia.NepsiaViewModel
import com.example.nepsis.presentation.nepsia.NepsiaViewModelFactory

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.Splash.route,
        modifier = modifier,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
        }
    ) {
        // --- 1. AUTH & ONBOARDING FLOW ---
        composable(AppDestinations.Splash.route) {
            val context = LocalContext.current
            val sessionManager = remember { ServiceLocator.provideSessionManager(context) }
            val userPrefs = remember { ServiceLocator.provideUserPreferences(context) }

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

            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(
                    repository = ServiceLocator.provideAuthRepository(),
                    profileRepository = ServiceLocator.provideProfileRepository(context),
                    sessionManager = ServiceLocator.provideSessionManager(context),
                    userPreferences = ServiceLocator.provideUserPreferences(context),
                    dao = ServiceLocator.provideDatabase(context).nepsisDao()
                )
            )

            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    val action = loginViewModel.state.value.navigationAction
                    if (action == LoginNavigationAction.GO_TO_HOME) {
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
            val dao = remember { ServiceLocator.provideDatabase(context).nepsisDao() }
            val profileRepo = remember { ServiceLocator.provideProfileRepository(context) }
            val sessionManager = remember { ServiceLocator.provideSessionManager(context) }

            val factory = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return OnboardingViewModel(userPrefs, dao, profileRepo, sessionManager) as T
                }
            }
            val viewModel: OnboardingViewModel = viewModel(factory = factory)

            OnboardingScreen(
                viewModel = viewModel,
                onNavigateToHome = {
                    navController.navigate(AppDestinations.Home.route) {
                        // CORRECCIÓN: Se reemplaza popUpTo(0)
                        popUpTo(navController.graph.id) { inclusive = true }
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
                navController = navController,
                onNavigateToDailyCheckIn = {
                    navController.navigate(AppDestinations.DailyCheckIn.route)
                }
            )
        }

        composable(AppDestinations.TestLibrary.route) {
            TestScreen(
                navController = navController,
                onNavigateToDetail = { testId ->
                    navController.navigate(AppDestinations.TestDetail.createRoute(testId))
                }
            )
        }

        composable(AppDestinations.History.route) {
            val context = LocalContext.current
            val dao = remember { ServiceLocator.provideDatabase(context).nepsisDao() }
            val historyViewModel: HistoryViewModel = viewModel(
                factory = HistoryViewModelFactory(dao)
            )
            HistoryScreen(
                viewModel = historyViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(AppDestinations.Profile.route) {
            val context = LocalContext.current
            val sessionManager = remember { ServiceLocator.provideSessionManager(context) }
            val userPrefs = remember { ServiceLocator.provideUserPreferences(context) }
            val database = remember { ServiceLocator.provideDatabase(context) }
            val coroutineScope = rememberCoroutineScope()

            val profileViewModel: com.example.nepsis.presentation.profile.ProfileViewModel = viewModel(
                factory = com.example.nepsis.presentation.profile.ProfileViewModelFactory(
                    repository = ServiceLocator.provideProfileRepository(context),
                    sessionManager = sessionManager,
                    dao = database.nepsisDao()
                )
            )
            ProfileScreen(
                viewModel = profileViewModel,
                navController = navController,
                onNavigateToTest = { testId ->
                    navController.navigate(AppDestinations.TestDetail.createRoute(testId))
                },
                onNavigateToSettings = { navController.navigate(AppDestinations.Settings.route) },
                onNavigateToFullHistory = { navController.navigate(AppDestinations.History.route) },
                onLogout = {
                    coroutineScope.launch {
                        sessionManager.clearSession()
                        withContext(Dispatchers.IO) {
                            database.clearAllTables()
                        }
                        userPrefs.saveOnboardingCompleted(false)
                        try {
                            val credentialManager = CredentialManager.create(context)
                            credentialManager.clearCredentialState(ClearCredentialStateRequest())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        withContext(Dispatchers.Main) {
                            navController.navigate(AppDestinations.Login.route) {
                                // CORRECCIÓN: Se reemplaza popUpTo(0)
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                        }
                    }
                }
            )
        }

        composable(AppDestinations.Settings.route) {
            val context = LocalContext.current
            val userPrefs = remember { ServiceLocator.provideUserPreferences(context) }
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(userPrefs)
            )

            SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
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
            val testId = backStackEntry.arguments?.getString("testId") ?: "vocacional"
            val context = LocalContext.current

            val repository = remember { ServiceLocator.provideNepsisRepository(context) }
            val sessionManager = remember { ServiceLocator.provideSessionManager(context) }

            val viewModel: TestQuestionsViewModel = viewModel(
                factory = TestQuestionsViewModelFactory(repository, sessionManager, testId)
            )

            TestQuestionsScreen(
                viewModel = viewModel,
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

        composable(AppDestinations.NepsiaChat.route) {
            val context = LocalContext.current
            val dao = remember { ServiceLocator.provideDatabase(context).nepsisDao() }
            val sessionManager = remember { ServiceLocator.provideSessionManager(context) }

            val nepsiaViewModel: NepsiaViewModel = viewModel(
                factory = NepsiaViewModelFactory(dao, sessionManager)
            )

            NepsiaChatScreen(
                viewModel = nepsiaViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToRoute = { route ->
                    when (route) {
                        "test_detail/personalidad" -> navController.navigate(AppDestinations.TestDetail.createRoute("personalidad"))
                        "test_detail/vocacional" -> navController.navigate(AppDestinations.TestDetail.createRoute("vocacional"))
                        "daily_checkin" -> navController.navigate(AppDestinations.DailyCheckIn.route)
                        "history" -> navController.navigate(AppDestinations.History.route)
                    }
                }
            )
        }
    }
}
