package com.example.nepsis.presentation.test

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nepsis.navigation.AppDestinations
import com.example.nepsis.ui.components.ModernCard
import com.example.nepsis.ui.components.NepsiaFab
import com.example.nepsis.ui.components.UamBackground
import com.example.nepsis.ui.theme.NepsisTextSecondary

@Composable
fun TestScreen(
    navController: NavController,
    onNavigateToDetail: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        UamBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = "Biblioteca de Tests",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Descubre más sobre ti realizando estas evaluaciones.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = NepsisTextSecondary
                )

                // 1. Test de Personalidad (ID corregido)
                TestCard(
                    title = "Test de Personalidad (MBTI)",
                    category = "Psicología",
                    description = "Descubre tu tipo de personalidad y cómo interactúas con el mundo.",
                    onClick = { onNavigateToDetail("personalidad") } 
                )

                // 2. Lenguajes del Amor (ID corregido)
                TestCard(
                    title = "Lenguajes del Amor",
                    category = "Relaciones",
                    description = "Identifica cómo prefieres dar y recibir aprecio.",
                    onClick = { onNavigateToDetail("lenguaje_amor") }
                )

                // 3. Test Vocacional (ID corregido)
                TestCard(
                    title = "Test Vocacional",
                    category = "Desarrollo Personal",
                    description = "Descubre qué áreas profesionales se alinean mejor con tus habilidades.",
                    onClick = { onNavigateToDetail("vocacional") } 
                )

                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // EL BOTÓN DE NEPSIA
        NepsiaFab(
            onClick = { navController.navigate(AppDestinations.NepsiaChat.route) },
            modifier = Modifier.align(Alignment.BottomStart).padding(bottom = 80.dp)
        )
    }
}

@Composable
fun TestCard(title: String, category: String, description: String, onClick: () -> Unit) {
    ModernCard(onClick = onClick) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                text = category,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = NepsisTextSecondary
            )
        }
    }
}
