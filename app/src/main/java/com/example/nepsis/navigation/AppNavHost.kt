package com.example.nepsis.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.HOME
    ){
        composable(route = AppDestinations.HOME){
            HomeScreen(navController = navController)
        }
        composable(route = AppDestinations.TEST) {
            TestScreen(navController = navController)
        }
        composable(route = AppDestinations.HISTORY) {
            HistoryScreen()
        }
        composable(route = AppDestinations.PROFILE) {
            ProfileScreen()
        }
    }
}