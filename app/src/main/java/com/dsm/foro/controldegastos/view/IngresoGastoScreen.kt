package com.dsm.foro.controldegastos.view

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dsm.foro.controldegastos.viewmodel.GastosViewModel
import java.util.Calendar

@Composable
fun IngresoGastoScreen(
    navController: NavController,
    viewModel: GastosViewModel = viewModel()
) {

    // Categorías
    val categorias = listOf(
        "Alimentación",
        "Transporte",
        "Salud",
        "Educación",
        "Entretenimiento",
        "Otros"
    )

    // Control dropdown
    var expanded by remember {
        mutableStateOf(false)
    }

    // Fecha actual
    val calendar = Calendar.getInstance()

    // DatePicker
    val datePickerDialog = DatePickerDialog(
        navController.context,
        { _, year, month, dayOfMonth ->

            val fechaSeleccionada = String.format(
                "%02d/%02d/%04d",
                dayOfMonth,
                month + 1,
                year
            )

            viewModel.fecha.value = fechaSeleccionada
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Registrar Nuevo Gasto",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Nombre
        OutlinedTextField(
            value = viewModel.nombre.value,
            onValueChange = {
                viewModel.nombre.value = it
            },
            label = {
                Text("Nombre del gasto")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Monto SOLO números
        OutlinedTextField(
            value = viewModel.monto.value,
            onValueChange = {

                if (it.matches(Regex("^\\d*\\.?\\d*$"))) {
                    viewModel.monto.value = it
                }
            },
            label = {
                Text("Monto ($)")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Categorías dropdown
        Box {

            OutlinedTextField(
                value = viewModel.categoria.value,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text("Categoría")
                },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {

                categorias.forEach { categoria ->

                    DropdownMenuItem(
                        text = {
                            Text(categoria)
                        },
                        onClick = {

                            viewModel.categoria.value = categoria
                            expanded = false
                        }
                    )
                }
            }

            Button(
                onClick = {
                    expanded = true
                },
                modifier = Modifier
                    .padding(top = 70.dp)
                    .fillMaxWidth()
            ) {
                Text("Seleccionar Categoría")
            }
        }

        Spacer(modifier = Modifier.height(80.dp))

        // Fecha
        OutlinedTextField(
            value = viewModel.fecha.value,
            onValueChange = {},
            readOnly = true,
            label = {
                Text("Fecha")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                datePickerDialog.show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Seleccionar Fecha")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mensajes
        viewModel.mensaje.value?.let { msj ->

            Text(
                text = msj,
                color =
                    if (
                        msj.contains("Error") ||
                        msj.contains("completa")
                    )
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Guardar gasto
        if (viewModel.isLoading.value) {

            CircularProgressIndicator()

        } else {

            Button(
                onClick = {
                    viewModel.registrarGasto()
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Guardar Gasto")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Ver historial
        Button(
            onClick = {
                navController.navigate("historial")
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Ver Historial")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Cerrar sesión
        Button(
            onClick = {

                com.google.firebase.auth.FirebaseAuth
                    .getInstance()
                    .signOut()

                navController.navigate("login") {

                    popUpTo(0)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Cerrar Sesión")
        }
    }
}