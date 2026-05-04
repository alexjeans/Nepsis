package com.example.nepsis.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

val historyItems = listOf(
    Triple(
        "Check-in emocional diario",
        "Resultado: Equilibrado",
        "04 mayo 2026"
    ),
    Triple(
        "Estrés académico",
        "Resultado: Moderado",
        "03 mayo 2026"
    ),
    Triple(
        "Balance personal",
        "Resultado: Necesita atención",
        "01 mayo 2026"
    ),
    Triple(
        "Rasgos de personalidad",
        "Resultado: Analítico / Reflexivo",
        "28 abril 2026"
    ),
    Triple(
        "Nivel de motivación",
        "Resultado: Alto",
        "25 abril 2026"
    )
)
@Composable

fun HistoryScreen(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pantalla de Historial")
    }
}