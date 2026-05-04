package com.example.nepsis.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nepsis.presentation.home.HomeScreen
import com.example.nepsis.presentation.login.LoginScreen
import com.example.nepsis.presentation.profile.HistoryScreen
import com.example.nepsis.presentation.profile.ProfileScreen
import com.example.nepsis.presentation.test.TestScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier

) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.LOGIN
    ){
        composable(route = AppDestinations.LOGIN){
            LoginScreen(navController = navController)
        }

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