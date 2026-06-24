package com.example.nepsis.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List // Agregado para Biblioteca
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.nepsis.navigation.AppDestinations
import com.example.nepsis.navigation.BottomNavItem

@Composable
fun NepsisBottomBar(
    navController: NavHostController
) {
    val items = listOf(
        BottomNavItem(
            title = "Inicio",
            icon = Icons.Default.Home,
            route = AppDestinations.Home.route // Corregido
        ),
        BottomNavItem(
            title = "Tests",
            icon = Icons.Default.List,
            route = AppDestinations.TestLibrary.route // Agregado
        ),
        BottomNavItem(
            title = "Historial",
            icon = Icons.Default.History,
            route = AppDestinations.History.route // Corregido
        ),
        BottomNavItem(
            title = "Perfil",
            icon = Icons.Default.Person,
            route = AppDestinations.Profile.route // Corregido
        )
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(text = item.title)
                }
            )
        }
    }
}