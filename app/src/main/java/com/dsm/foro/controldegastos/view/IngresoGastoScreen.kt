package com.dsm.foro.controldegastos.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dsm.foro.controldegastos.viewmodel.GastosViewModel

@Composable
fun IngresoGastoScreen(viewModel: GastosViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registrar Nuevo Gasto", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = viewModel.nombre.value,
            onValueChange = { viewModel.nombre.value = it },
            label = { Text("Nombre del gasto (Ej: Supermercado)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.monto.value,
            onValueChange = { viewModel.monto.value = it },
            label = { Text("Monto ($)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.categoria.value,
            onValueChange = { viewModel.categoria.value = it },
            label = { Text("Categoría (Ej: Comida, Transporte)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.fecha.value,
            onValueChange = { viewModel.fecha.value = it },
            label = { Text("Fecha (dd/mm/aaaa)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar mensajes de error o éxito
        viewModel.mensaje.value?.let { msj ->
            Text(msj, color = if (msj.contains("Error") || msj.contains("completa")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (viewModel.isLoading.value) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { viewModel.registrarGasto() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Gasto")
            }
        }
    }
}