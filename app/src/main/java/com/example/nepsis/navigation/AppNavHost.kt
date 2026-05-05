package com.example.nepsis.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nepsis.data.VocacionalRepository
import com.example.nepsis.model.OpcionVocacional
import com.example.nepsis.model.PerfilEstudiante
import com.example.nepsis.model.ResultadoVocacional
import com.example.nepsis.presentation.history.HistoryScreen
import com.example.nepsis.presentation.home.HomeScreen
import com.example.nepsis.presentation.login.LoginScreen
import com.example.nepsis.presentation.profile.ProfileScreen
import com.example.nepsis.presentation.result.ResultScreen
import com.example.nepsis.presentation.test.TestScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    perfil: PerfilEstudiante?,
    historial: List<ResultadoVocacional>,
    resultadoActual: ResultadoVocacional?,
    respuestas: Map<Int, OpcionVocacional>,
    onLogin: (PerfilEstudiante) -> Unit,
    onLogout: () -> Unit,
    onClearAnswers: () -> Unit,
    onAnswer: (Int, OpcionVocacional) -> Unit,
    onFinishTest: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.LOGIN,
        modifier = modifier
    ) {
        composable(route = AppDestinations.LOGIN) {
            LoginScreen(
                onIngresar = { nuevoPerfil ->
                    onLogin(nuevoPerfil)

                    navController.navigate(AppDestinations.HOME) {
                        popUpTo(AppDestinations.LOGIN) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = AppDestinations.HOME) {
            HomeScreen(
                perfil = perfil,
                historial = historial,
                onIniciarTest = {
                    onClearAnswers()
                    navController.navigate(AppDestinations.TEST)
                },
                onCerrarSesion = {
                    onLogout()

                    navController.navigate(AppDestinations.LOGIN) {
                        popUpTo(AppDestinations.HOME) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = AppDestinations.TEST) {
            TestScreen(
                test = VocacionalRepository.testVocacional,
                respuestas = respuestas,
                onResponder = { indice, opcion ->
                    onAnswer(indice, opcion)
                },
                onVolver = {
                    navController.popBackStack()
                },
                onFinalizar = {
                    onFinishTest()

                    navController.navigate(AppDestinations.RESULT)
                }
            )
        }

        composable(route = AppDestinations.RESULT) {
            ResultScreen(
                perfil = perfil,
                resultado = resultadoActual,
                historial = historial,
                onRepetirTest = {
                    onClearAnswers()

                    navController.navigate(AppDestinations.TEST) {
                        popUpTo(AppDestinations.RESULT) {
                            inclusive = true
                        }
                    }
                },
                onVolverInicio = {
                    navController.navigate(AppDestinations.HOME) {
                        popUpTo(AppDestinations.RESULT) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = AppDestinations.HISTORY) {
            HistoryScreen(
                historial = historial
            )
        }

        composable(route = AppDestinations.PROFILE) {
            ProfileScreen(
                perfil = perfil,
                historial = historial
            )
        }
    }
}