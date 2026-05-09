package com.dsm.foro.controldegastos.view

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    // El controlador que maneja los viajes entre pantallas
    val navController = rememberNavController()

    // Definimos el host y la pantalla de inicio ("login")
    NavHost(navController = navController, startDestination = "login") {

        // Ruta 1: Pantalla de Login
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    // Si el login es exitoso, navegamos a "gastos"
                    navController.navigate("gastos") {
                        // Esto evita que al darle "Atrás" el usuario regrese al login
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // Ruta 2: Pantalla de Registro de Gastos
        composable("gastos") {
            IngresoGastoScreen()
        }
    }
}