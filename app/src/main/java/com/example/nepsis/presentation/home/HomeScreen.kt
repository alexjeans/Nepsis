package com.example.nepsis.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.nepsis.navigation.AppDestinations

val dailyTestTitle = "Check-in emocional diario"
val dailyTestDescription = "Un test corto para registrar cómo te sientes hoy."
val dailyTestDuration = "2 min"

val recommendedTests = listOf(
    Triple(
        "Estrés académico",
        "Evalúa tu nivel de presión y carga universitaria actual.",
        "5 min"
    ),
    Triple(
        "Balance personal",
        "Identifica cómo estás distribuyendo tu energía entre estudio, descanso y vida personal.",
        "4 min"
    ),
    Triple(
        "Rasgos de personalidad",
        "Explora patrones de comportamiento, toma de decisiones y preferencias.",
        "6 min"
    ),
    Triple(
        "Nivel de motivación",
        "Detecta qué tan motivado te sientes para cumplir tus metas esta semana.",
        "3 min"
    )
)

@Composable
fun HomeScreen(
    navController: NavHostController
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pantalla de Inicio")

        Button(
            onClick = {
                navController.navigate(AppDestinations.TEST)
            }
        ) {
            Text(text = "Ir al Test")
        }

        Button(onClick = {navController.navigate(AppDestinations.LOGIN)})
        {
            Text(text = "Cerrar Sesion")
        }
    }
}