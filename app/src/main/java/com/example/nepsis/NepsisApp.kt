package com.example.nepsis

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nepsis.core.di.ServiceLocator
import com.example.nepsis.navigation.AppDestinations
import com.example.nepsis.navigation.AppNavHost
import com.example.nepsis.ui.components.NepsisBottomBar
import com.example.nepsis.ui.theme.NepsisTheme

@Composable
fun NepsisApp() {
    val context = LocalContext.current
    val userPrefs = remember { ServiceLocator.provideUserPreferences(context) }
    
    // Leemos el estado (por defecto usa el del sistema)
    val isDarkMode by userPrefs.isDarkMode.collectAsState(initial = isSystemInDarkTheme())

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Definición estricta de las únicas pantallas que muestran Bottom Navigation
    val showBottomBarRoutes = listOf(
        AppDestinations.Home.route,
        AppDestinations.TestLibrary.route,
        AppDestinations.History.route,
        AppDestinations.Profile.route
    )

    NepsisTheme(darkTheme = isDarkMode) {
        Scaffold(
            bottomBar = {
                if (currentRoute in showBottomBarRoutes) {
                    NepsisBottomBar(navController = navController)
                }
            }
        ) { paddingValues ->
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}
