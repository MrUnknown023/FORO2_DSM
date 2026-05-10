package com.dsm.foro.controldegastos.view

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.dsm.foro.controldegastos.viewmodel.AuthViewModel

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, viewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current

    // Lanzador para el resultado de Google
    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { viewModel.onGoogleSignInResult(it) }
        } catch (e: ApiException) {
            viewModel.errorMessage.value = "Error de Google: ${e.statusCode}"
        }
    }

    // Navegar si el login es exitoso
    LaunchedEffect(viewModel.isLogged.value) {
        if (viewModel.isLogged.value) onLoginSuccess()
    }

    Column(
    modifier = Modifier.fillMaxSize().padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
    ) {
        Text("Control de Gastos", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = viewModel.email.value,
            onValueChange = { viewModel.email.value = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.password.value,
            onValueChange = { viewModel.password.value = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (viewModel.errorMessage.value != null) {
            Text(viewModel.errorMessage.value!!, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.isLoading.value) {
            CircularProgressIndicator()
        } else {
            // Botón principal (Cambia su texto dinámicamente)
            Button(
                onClick = { viewModel.autenticar() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (viewModel.isLoginMode.value) "Iniciar Sesión" else "Registrar Cuenta")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón de Google (Se mantiene igual)
            OutlinedButton(
                onClick = {
                    val repo = com.dsm.foro.controldegastos.repository.AuthRepository()
                    googleLauncher.launch(repo.getGoogleSignInClient(context).signInIntent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Entrar con Google")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para alternar entre Login y Registro
            TextButton(
                onClick = {
                    viewModel.isLoginMode.value = !viewModel.isLoginMode.value
                    viewModel.errorMessage.value = null // Limpiar errores al cambiar de modo
                }
            ) {
                Text(
                    if (viewModel.isLoginMode.value)
                        "¿No tienes cuenta? Regístrate aquí"
                    else
                        "¿Ya tienes cuenta? Inicia sesión"
                )
            }
        }
    }
}