package com.example.nepsis

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.nepsis.navigation.AppNavHost
import com.example.nepsis.ui.components.NepsisBottomBar

// Este archivo es como el visual de la app, No representa pantalla especifica
// Se arma la estructura global del UI

@Composable
fun NepsisApp(){
    val navController = rememberNavController()
    AppNavHost(navController = navController)

    Scaffold(
        bottomBar = {
            NepsisBottomBar(navController = navController)
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}


