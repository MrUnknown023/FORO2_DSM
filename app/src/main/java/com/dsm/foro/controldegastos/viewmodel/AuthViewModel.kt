package com.dsm.foro.controldegastos.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dsm.foro.controldegastos.repository.AuthRepository

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    var email = mutableStateOf("")
    var password = mutableStateOf("")

    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)
    var isLogged = mutableStateOf(false)

    var isLoginMode = mutableStateOf(true)

    fun login() {
        if (email.value.isEmpty() || password.value.isEmpty()) {
            errorMessage.value = "Completa todos los campos"
            return
        }

        isLoading.value = true
        repository.loginConEmail(email.value, password.value) { exito, error ->
            isLoading.value = false
            if (exito) isLogged.value = true else errorMessage.value = error
        }
    }

    fun onGoogleSignInResult(idToken: String) {
        isLoading.value = true
        repository.firebaseAuthWithGoogle(idToken) { exito, error ->
            isLoading.value = false
            if (exito) isLogged.value = true else errorMessage.value = error
        }
    }

    fun autenticar() {
        if (email.value.isEmpty() || password.value.isEmpty()) {
            errorMessage.value = "Completa todos los campos"
            return
        }

        // Firebase exige contraseñas de mínimo 6 caracteres
        if (password.value.length < 6) {
            errorMessage.value = "La contraseña debe tener al menos 6 caracteres"
            return
        }

        isLoading.value = true

        if (isLoginMode.value) {
            // Modo: Iniciar Sesión
            repository.loginConEmail(email.value, password.value) { exito, error ->
                isLoading.value = false
                if (exito) isLogged.value = true else errorMessage.value = error
            }
        } else {
            // Modo: Registrarse
            repository.registrarConEmail(email.value, password.value) { exito, error ->
                isLoading.value = false
                if (exito) isLogged.value = true else errorMessage.value = error
            }
        }
    }
}