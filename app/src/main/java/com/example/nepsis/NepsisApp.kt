package com.example.nepsis

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.nepsis.navigation.AppNavHost

// Este archivo es como el visual de la app, No representa pantalla especifica
// Se arma la estructura global del UI

@Composable
fun NepsisApp(){
    val navController = rememberNavController()
    AppNavHost(navController = navController)
}

