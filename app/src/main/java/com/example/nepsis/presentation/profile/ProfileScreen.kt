package com.example.nepsis.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.nepsis.navigation.AppDestinations
import com.example.nepsis.ui.components.ModernCard
import com.example.nepsis.ui.components.NepsiaFab
import com.example.nepsis.ui.components.UamBackground
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navController: NavController,
    onNavigateToTest: (String) -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToFullHistory: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val perfil = state.profile
    val recentResults = state.recentResults
    
    var showLogoutDialog by remember { mutableStateOf(false) }

    val iniciales = perfil?.fullName?.trim()?.split(" ")?.mapNotNull { it.firstOrNull()?.uppercaseChar() }?.take(2)?.joinToString("") ?: "U"

    val personalidadResult = recentResults.firstOrNull { it.testId == "personalidad" }
    val lenguajeAmorResult = recentResults.firstOrNull { it.testId == "lenguaje_amor" }
    val vocacionalResult = recentResults.firstOrNull { it.testId == "vocacional" }

    Box(modifier = Modifier.fillMaxSize()) {
        UamBackground {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                
                // 1. CABECERA DEL PERFIL CON IMAGEN DE GOOGLE
                ModernCard {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        if (!perfil?.avatarUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = perfil!!.avatarUrl,
                                contentDescription = "Foto de perfil de Google",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.padding(16.dp).size(80.dp).clip(CircleShape)
                            )
                        } else {
                            Box(
                                modifier = Modifier.padding(16.dp).size(80.dp).clip(CircleShape).background(MaterialTheme.colorScheme.secondary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = iniciales, color = MaterialTheme.colorScheme.onSecondary, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                            }
                        }
                        
                        Text(text = perfil?.fullName ?: "Usuario Nepsis", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        Text(text = perfil?.email ?: "Sin correo", color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            perfil?.age?.let { edad -> if (edad > 0) BadgeInfo(text = "$edad años") }
                            perfil?.gender?.let { gen -> if (gen.isNotBlank()) BadgeInfo(text = gen) }
                        }
                    }
                }

                // 2. MÓDULOS DE TESTS CON BOTÓN REALIZAR FUNCIONAL
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Tus Módulos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    
                    ModuleCard(
                        title = "Personalidad (MBTI)",
                        statusText = personalidadResult?.resultText ?: "No completado",
                        isCompleted = personalidadResult != null,
                        onClick = { onNavigateToTest("personalidad") }
                    )
                    
                    ModuleCard(
                        title = "Lenguaje del Amor",
                        statusText = lenguajeAmorResult?.resultText ?: "No completado",
                        isCompleted = lenguajeAmorResult != null,
                        onClick = { onNavigateToTest("lenguaje_amor") }
                    )

                    ModuleCard(
                        title = "Test Vocacional",
                        statusText = vocacionalResult?.resultText ?: "No completado",
                        isCompleted = vocacionalResult != null,
                        onClick = { onNavigateToTest("vocacional") }
                    )
                }

                // 3. SECCIÓN DE HISTORIAL RECIENTE REDISEÑADO
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Historial Reciente", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    
                    if (recentResults.isEmpty()) {
                        Text(
                            text = "No hay intentos registrados.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        // Mostrar los últimos 3 intentos realizados
                        recentResults.take(3).forEach { result ->
                            val testTitle = when (result.testId) {
                                "personalidad" -> "Test de Personalidad (MBTI)"
                                "lenguaje_amor" -> "Lenguajes del Amor"
                                else -> "Test Vocacional"
                            }
                            val dateStr = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(result.createdAt))

                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(testTitle, fontWeight = FontWeight.Bold)
                                        Text(dateStr, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Text("Resultado: ${result.resultText}", style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        
                        OutlinedButton(
                            onClick = onNavigateToFullHistory,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Ver historial completo")
                        }
                    }
                }

                // 4. CONFIGURACIÓN
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Configuración", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Button(onClick = onNavigateToSettings, modifier = Modifier.fillMaxWidth().height(50.dp)) {
                        Icon(Icons.Default.Settings, contentDescription = "Ajustes")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ajustes de la App")
                    }
                    Button(onClick = { showLogoutDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error), modifier = Modifier.fillMaxWidth().height(50.dp)) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cerrar Sesión")
                    }
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // EL BOTÓN DE NEPSIA
        NepsiaFab(
            onClick = { navController.navigate(AppDestinations.NepsiaChat.route) },
            modifier = Modifier.align(Alignment.BottomStart).padding(bottom = 80.dp)
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Estás seguro de que deseas salir de tu cuenta?") },
            confirmButton = {
                TextButton(onClick = { showLogoutDialog = false; onLogout() }) {
                    Text("Sí, salir", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = { TextButton(onClick = { showLogoutDialog = false }) { Text("Cancelar") } }
        )
    }
}

@Composable
fun BadgeInfo(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
    }
}

@Composable
fun ModuleCard(title: String, statusText: String, isCompleted: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = statusText, 
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (!isCompleted) {
                Button(onClick = onClick, contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)) {
                    Text("Realizar")
                }
            } else {
                Icon(Icons.Default.Lock, contentDescription = "Completado", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
