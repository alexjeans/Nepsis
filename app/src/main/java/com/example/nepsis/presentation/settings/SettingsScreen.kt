package com.example.nepsis.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Regresar") }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).verticalScroll(rememberScrollState())
        ) {
            SettingsSectionTitle("Preferencias")
            SettingsSwitchItem(icon = Icons.Default.ColorLens, title = "Modo Oscuro", isChecked = false, onCheckedChange = { /* Requiere DataStore + CompositionLocal en Fase 3 */ })
            SettingsClickableItem(icon = Icons.Default.Language, title = "Idioma", subtitle = "Español", onClick = { })
            SettingsSwitchItem(icon = Icons.Default.Notifications, title = "Notificaciones Diarias", isChecked = true, onCheckedChange = { /* Implementación base para WorkManager a futuro */ })

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            SettingsSectionTitle("Acerca de")
            SettingsClickableItem(icon = Icons.Default.Policy, title = "Política de Privacidad", onClick = { showPrivacyDialog = true })
            SettingsClickableItem(icon = Icons.Default.Policy, title = "Términos y Condiciones", onClick = { showTermsDialog = true })

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            SettingsSectionTitle("Zona de Peligro", color = MaterialTheme.colorScheme.error)
            SettingsClickableItem(
                icon = Icons.Default.DeleteForever,
                title = "Eliminar Cuenta",
                color = MaterialTheme.colorScheme.error,
                onClick = { showDeleteDialog = true }
            )
        }
    }

    // DIÁLOGO: POLÍTICA DE PRIVACIDAD
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text("Política de Privacidad") },
            text = { Text("Uso de datos:\nRecopilamos tus respuestas para generar tu perfil. Nepsis NO VENDE tus datos a terceros. Toda tu información se utiliza estrictamente para el funcionamiento interno de la app.\n\nResponsabilidad:\nTus datos están protegidos, pero mantenemos una responsabilidad limitada ante interrupciones de servicios de terceros (Google/Supabase).") },
            confirmButton = { TextButton(onClick = { showPrivacyDialog = false }) { Text("Entendido") } }
        )
    }

    // DIÁLOGO: TÉRMINOS Y CONDICIONES
    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            title = { Text("Términos y Condiciones") },
            text = { Text("Al usar Nepsis, aceptas hacer un uso responsable de la plataforma.\n\nAviso Importante:\nLos resultados de los tests NO SUSTITUYEN el asesoramiento, diagnóstico o tratamiento de un profesional de la salud mental o psicólogo.\n\nEl uso de esta app y la interpretación de sus resultados es bajo tu propia responsabilidad.") },
            confirmButton = { TextButton(onClick = { showTermsDialog = false }) { Text("Aceptar") } }
        )
    }

    // DIÁLOGO: ELIMINAR CUENTA
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Si borras mis datos") },
            text = { Text("Esta acción es permanente. Se eliminará tu cuenta, tu historial, tus estados de ánimo y todas tus preferencias en Nepsis para siempre.") },
            confirmButton = {
                TextButton(onClick = { 
                    showDeleteDialog = false 
                    // Aquí en el futuro llamarás a: authRepository.deleteUserSupabase()
                    // Y luego ejecutarás la limpieza local (onLogout)
                }) {
                    Text("Borrar datos 😥", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun SettingsSectionTitle(title: String, color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary) {
    Text(text = title, style = MaterialTheme.typography.labelLarge, color = color, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 24.dp, top = 16.dp, bottom = 8.dp))
}

@Composable
fun SettingsClickableItem(icon: ImageVector, title: String, subtitle: String? = null, color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface, onClick: () -> Unit) {
    Surface(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = color)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.bodyLarge, color = color)
                if (subtitle != null) Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun SettingsSwitchItem(icon: ImageVector, title: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
        }
        Switch(checked = isChecked, onCheckedChange = onCheckedChange)
    }
}