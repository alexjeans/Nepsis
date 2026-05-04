package com.example.nepsis

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nepsis.navigation.AppDestinations
import com.example.nepsis.navigation.AppNavHost
import com.example.nepsis.ui.components.NepsisBottomBar


// Este archivo es como el visual de la app, No representa pantalla especifica
// Se arma la estructura global del UI

@Composable
fun NepsisApp(){
    val navController = rememberNavController()
    AppNavHost(navController = navController)

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute!= AppDestinations.LOGIN)
            NepsisBottomBar(navController = navController)
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}


