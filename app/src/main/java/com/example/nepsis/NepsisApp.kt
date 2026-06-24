package com.example.nepsis

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nepsis.navigation.AppDestinations
import com.example.nepsis.navigation.AppNavHost
import com.example.nepsis.ui.components.NepsisBottomBar
import com.example.nepsis.ui.theme.NepsisTheme

@Composable
fun NepsisApp() {
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

    NepsisTheme {
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
