package com.example.nepsis

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nepsis.data.VocacionalRepository
import com.example.nepsis.model.OpcionVocacional
import com.example.nepsis.model.PerfilEstudiante
import com.example.nepsis.model.ResultadoVocacional
import com.example.nepsis.navigation.AppDestinations
import com.example.nepsis.navigation.AppNavHost
import com.example.nepsis.ui.components.NepsisBottomBar
import com.example.nepsis.ui.theme.NepsisTheme

@Composable
fun NepsisApp() {
    val navController = rememberNavController()

    var perfil by remember { mutableStateOf<PerfilEstudiante?>(null) }
    var resultadoActual by remember { mutableStateOf<ResultadoVocacional?>(null) }

    val respuestas = remember { mutableStateMapOf<Int, OpcionVocacional>() }
    val historial = remember { mutableStateListOf<ResultadoVocacional>() }

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val ocultarBottomBar = currentRoute in listOf(
        AppDestinations.LOGIN,
        AppDestinations.TEST,
        AppDestinations.RESULT
    )

    NepsisTheme {
        Scaffold(
            bottomBar = {
                if (!ocultarBottomBar) {
                    NepsisBottomBar(navController = navController)
                }
            }
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
                perfil = perfil,
                historial = historial,
                resultadoActual = resultadoActual,
                respuestas = respuestas,
                onLogin = { nuevoPerfil ->
                    perfil = nuevoPerfil
                },
                onLogout = {
                    perfil = null
                    respuestas.clear()
                    resultadoActual = null
                },
                onClearAnswers = {
                    respuestas.clear()
                },
                onAnswer = { indice, opcion ->
                    respuestas[indice] = opcion
                },
                onFinishTest = {
                    val resultado = VocacionalRepository.testVocacional
                        .calcularResultado(respuestas.values)

                    resultadoActual = resultado
                    historial.add(resultado)
                }
            )
        }
    }
}