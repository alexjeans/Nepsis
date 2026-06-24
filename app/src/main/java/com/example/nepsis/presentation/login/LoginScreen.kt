package com.example.nepsis.presentation.login

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nepsis.BuildConfig
import com.example.nepsis.core.di.ServiceLocator
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel
)
{
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Navegar automáticamente cuando el authData no es nulo
    LaunchedEffect(state.authData) {
        if (state.authData != null) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Bienvenido a Nepsis", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(32.dp))

        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = {
                // Extraemos el Activity de forma segura
                val activityContext = context.findActivity()
                if (activityContext != null) {
                    coroutineScope.launch {
                        launchGoogleLogin(activityContext) { idToken ->
                            if (idToken != null) {
                                viewModel.loginWithSupabase(idToken)
                            } else {
                                // Opcional: Manejar si el token es null (ej. el usuario canceló)
                            }
                        }
                    }
                }
            }) {
                Text("Iniciar sesión con Google")
            }
        }

        if (state.error != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Error: ${state.error}", color = MaterialTheme.colorScheme.error)
        }
    }
}

private suspend fun launchGoogleLogin(context: Context, onResult: (String?) -> Unit) {
    try {
        val credentialManager = CredentialManager.create(context)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(context, request)
        val credential = result.credential

        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            onResult(googleIdTokenCredential.idToken)
        } else {
            onResult(null)
        }
    } catch (e: Exception) {
        onResult(null)
    }
}

// Busca recursivamente el Activity subyacente
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}