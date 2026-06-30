package com.example.nepsis.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nepsis.navigation.AppDestinations
import com.example.nepsis.ui.components.NepsiaFab

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController,
    onNavigateToDailyCheckIn: () -> Unit
) {
    // Escuchamos los datos de Room reactivamente
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = onNavigateToDailyCheckIn) {
                    Icon(Icons.Default.Add, contentDescription = "Nuevo Check-In")
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Hola de nuevo 👋",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                // Tarjeta de llamada a la acción (Call to Action)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("¿Cómo va tu día?", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onNavigateToDailyCheckIn) {
                            Text("Registrar Bienestar")
                        }
                    }
                }

                Text(
                    text = "Tus últimos registros",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 8.dp)
                )

                if (state.moods.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Aún no hay registros. ¡Haz tu primer check-in!")
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(state.moods) { mood ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = mood.emotionalState, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                        Text(text = mood.date, style = MaterialTheme.typography.bodySmall)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(text = "⚡ Energía: ${mood.energyLevel}/5", style = MaterialTheme.typography.bodyMedium)
                                        Text(text = "🤯 Estrés: ${mood.stressLevel}/5", style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // EL BOTÓN DE NEPSIA
        NepsiaFab(
            onClick = { navController.navigate(AppDestinations.NepsiaChat.route) },
            modifier = Modifier.align(Alignment.BottomStart).padding(bottom = 80.dp) // Ajustado para no tapar el BottomBar si existe
        )
    }
}
