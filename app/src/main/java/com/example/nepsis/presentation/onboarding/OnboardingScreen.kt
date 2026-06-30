package com.example.nepsis.presentation.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    onNavigateToHome: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val coroutineScope = rememberCoroutineScope()
    val isCompleted by viewModel.isCompleted.collectAsState()

    // Observar cuando se guarde el DataStore para navegar
    LaunchedEffect(isCompleted) {
        if (isCompleted) {
            onNavigateToHome()
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                userScrollEnabled = false // Bloqueamos el scroll manual para forzar a usar los botones
            ) { page ->
                when (page) {
                    0 -> WelcomePage()
                    1 -> BasicInfoPage(viewModel)
                    2 -> GoalPage(viewModel)
                    3 -> FinalPage()
                }
            }

            // Barra inferior con los botones
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón Atrás (Oculto en la primera y última página)
                if (pagerState.currentPage in 1..2) {
                    TextButton(onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }) {
                        Text("Atrás")
                    }
                } else {
                    Spacer(modifier = Modifier.width(64.dp))
                }

                // Botón Siguiente / Finalizar
                Button(
                    onClick = {
                        coroutineScope.launch {
                            if (pagerState.currentPage < 3) {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            } else {
                                viewModel.completeOnboarding()
                            }
                        }
                    }
                ) {
                    Text(if (pagerState.currentPage == 3) "Entrar a la app" else "Continuar")
                }
            }
        }
    }
}

@Composable
fun WelcomePage() {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bienvenido a Nepsis", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Queremos conocerte un poco mejor para personalizar tu experiencia desde el primer momento.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BasicInfoPage(viewModel: OnboardingViewModel) {
    val name by viewModel.name.collectAsState()
    val age by viewModel.age.collectAsState()
    val gender by viewModel.gender.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Información Básica", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { viewModel.name.value = it },
            label = { Text("¿Cómo te llamas?") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = age,
            onValueChange = { viewModel.age.value = it },
            label = { Text("Edad") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Selector simple de género
        Text("Género", modifier = Modifier.align(Alignment.Start), style = MaterialTheme.typography.labelLarge)
        val genders = listOf("Hombre", "Mujer", "Otro", "Prefiero no decirlo")
        genders.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.gender.value = option }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = gender == option, onClick = { viewModel.gender.value = option })
                Spacer(modifier = Modifier.width(8.dp))
                Text(option)
            }
        }
    }
}

@Composable
fun GoalPage(viewModel: OnboardingViewModel) {
    val goal by viewModel.goal.collectAsState()
    val goals = listOf(
        "Conocerme mejor", 
        "Mejorar mis hábitos", 
        "Ser más productivo", 
        "Descubrir mis fortalezas"
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Tu Objetivo Principal", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("¿Qué esperas lograr con Nepsis?", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(32.dp))

        goals.forEach { option ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { viewModel.goal.value = option },
                colors = CardDefaults.cardColors(
                    containerColor = if (goal == option) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = option,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun FinalPage() {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("¡Todo listo!", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Ya podemos comenzar a personalizar tu experiencia.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}