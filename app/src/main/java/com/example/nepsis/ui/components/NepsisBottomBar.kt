package com.example.nepsis.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.test.espresso.base.Default
import com.example.nepsis.navigation.AppDestinations
import com.example.nepsis.navigation.BottomNavItem

@Composable
fun NepsisBottomBar(
    navController: NavHostController
){
    val items = listOf(
        BottomNavItem(
            title = "Inicio",
            icon = Icons.Default.Home,
            route = AppDestinations.HOME
        ),
        BottomNavItem(
            title = "Historial",
            icon = Icons.Default.List,
            route = AppDestinations.HISTORY
        ),
        BottomNavItem(
            title = "Perfil",
            icon = Icons.Default.Person,
            route = AppDestinations.PROFILE
        ),
    )
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar{
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route){
                        popUpTo(navController.graph.startDestinationId){
                            saveState = true
                        }
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

