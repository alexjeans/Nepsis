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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nepsis.core.di.ServiceLocator
import com.example.nepsis.ui.components.ModernCard
import com.example.nepsis.ui.components.UamBackground
import com.example.nepsis.ui.theme.UamAccent
import com.example.nepsis.ui.theme.UamPrimary
import com.example.nepsis.ui.theme.UamPrimaryDark
import com.example.nepsis.ui.theme.UamTextSecondary

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(
            repository = ServiceLocator.provideProfileRepository(LocalContext.current),
            sessionManager = ServiceLocator.provideSessionManager(LocalContext.current)
        )
    ),
    onNavigateToSettings: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val perfil = state.profile
    
    var showLogoutDialog by remember { mutableStateOf(false) }

    val iniciales = perfil?.fullName
        ?.trim()?.split(" ")?.mapNotNull { it.firstOrNull()?.uppercaseChar() }?.take(2)?.joinToString("") ?: "U"

    UamBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            
            // 1. CABECERA DEL PERFIL
            ModernCard {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(UamAccent)
                            .padding(28.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = iniciales,
                            color = UamPrimaryDark,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = perfil?.fullName ?: "Usuario Nepsis",
                        color = UamPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = perfil?.email ?: "Sin correo",
                        color = UamTextSecondary,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Chips de Información Dinámicos
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        perfil?.age?.let { siTieneEdad -> 
                            if (siTieneEdad > 0) BadgeInfo(text = "$siTieneEdad años") 
                        }
                        
                        perfil?.gender?.let { siTieneGenero ->
                            if (siTieneGenero.isNotBlank()) BadgeInfo(text = siTieneGenero)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    perfil?.goal?.let { siTieneObjetivo ->
                        if (siTieneObjetivo.isNotBlank()) {
                            BadgeInfo(text = "Objetivo: $siTieneObjetivo", modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            }

            // 2. MÓDULOS DE TESTS
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = "Tus Módulos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                
                // Módulo completado (Simulado)
                ModuleCard(
                    title = "Personalidad (MBTI)",
                    statusText = "Arquitecto (INTJ)",
                    isCompleted = true,
                    onClick = { /* TODO: Ver detalle */ }
                )
                
                // Módulo pendiente (Simulado)
                ModuleCard(
                    title = "Lenguaje del Amor",
                    statusText = "No encontrado",
                    isCompleted = false,
                    onClick = { /* TODO: Ir a test */ }
                )
            }

            // 3. HISTORIAL INTEGRADO
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = "Historial Reciente", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                
                // Elemento de historial simulado
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Test Vocacional", fontWeight = FontWeight.Bold)
                        Text("Perfil: Ingeniería y Tecnología", style = MaterialTheme.typography.bodyMedium)
                        Text("Hace 2 días", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            // 4. CONFIGURACIÓN
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = "Configuración", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                
                Button(
                    onClick = onNavigateToSettings,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Icon(Icons.Default.Settings, contentDescription = "Ajustes")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ajustes de la App", style = MaterialTheme.typography.titleMedium)
                }
                
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { showLogoutDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cerrar Sesión", style = MaterialTheme.typography.titleMedium)
                }
            }
            
            Spacer(modifier = Modifier.height(80.dp)) // Espacio para la BottomBar
        }
    }

    // Diálogo de confirmación para cerrar sesión
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Estás seguro de que deseas salir de tu cuenta?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text("Sí, salir", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
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
                    color = if (isCompleted) MaterialTheme.colorScheme.primary else UamTextSecondary
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