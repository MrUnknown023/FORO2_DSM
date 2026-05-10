package com.dsm.foro.controldegastos.view

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dsm.foro.controldegastos.model.Gasto
import com.dsm.foro.controldegastos.viewmodel.GastosViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistorialGastosScreen(
    navController: NavController,
    viewModel: GastosViewModel = viewModel()
) {

    var gastoEditando by remember {
        mutableStateOf<Gasto?>(null)
    }

    LaunchedEffect(Unit) {
        viewModel.cargarGastos()
    }

    // =========================
    // FILTRO MES
    // =========================

    val meses = listOf(
        "01/2026",
        "02/2026",
        "03/2026",
        "04/2026",
        "05/2026",
        "06/2026",
        "07/2026",
        "08/2026",
        "09/2026",
        "10/2026",
        "11/2026",
        "12/2026"
    )

    var expandedMes by remember {
        mutableStateOf(false)
    }

    var mesSeleccionado by remember {

        mutableStateOf(
            SimpleDateFormat(
                "MM/yyyy",
                Locale.getDefault()
            ).format(Date())
        )
    }

    // Filtrar gastos
    val gastosFiltrados = viewModel.listaGastos.value.filter {

        it.fecha.contains(mesSeleccionado)
    }

    // Total dinámico
    val totalFiltrado = gastosFiltrados.sumOf {
        it.monto
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Volver
        Button(
            onClick = {
                navController.popBackStack()
            }
        ) {
            Text("Volver")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Historial de Gastos",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TOTAL
        Text(
            text = "Total del mes ($mesSeleccionado): $$totalFiltrado",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // DROPDOWN MES
        Box {

            OutlinedTextField(
                value = mesSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text("Filtrar por mes")
                },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = expandedMes,
                onDismissRequest = {
                    expandedMes = false
                }
            ) {

                meses.forEach { mes ->

                    DropdownMenuItem(
                        text = {
                            Text(mes)
                        },
                        onClick = {

                            mesSeleccionado = mes
                            expandedMes = false
                        }
                    )
                }
            }

            Button(
                onClick = {
                    expandedMes = true
                },
                modifier = Modifier
                    .padding(top = 70.dp)
                    .fillMaxWidth()
            ) {
                Text("Seleccionar Mes")
            }
        }

        Spacer(modifier = Modifier.height(80.dp))

        // =========================
        // LISTA
        // =========================

        LazyColumn {

            items(gastosFiltrados) { gasto ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text("Nombre: ${gasto.nombre}")
                        Text("Monto: $${gasto.monto}")
                        Text("Categoría: ${gasto.categoria}")
                        Text("Fecha: ${gasto.fecha}")

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Button(
                                onClick = {
                                    gastoEditando = gasto
                                }
                            ) {
                                Text("Editar")
                            }

                            Button(
                                onClick = {
                                    viewModel.eliminarGasto(gasto.id)
                                }
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }

    // =========================
    // DIALOG EDITAR
    // =========================

    gastoEditando?.let { gasto ->

        var nombre by remember {
            mutableStateOf(gasto.nombre)
        }

        var monto by remember {
            mutableStateOf(gasto.monto.toString())
        }

        var categoria by remember {
            mutableStateOf(gasto.categoria)
        }

        var fecha by remember {
            mutableStateOf(gasto.fecha)
        }

        // Categorías
        val categorias = listOf(
            "Alimentación",
            "Transporte",
            "Salud",
            "Educación",
            "Entretenimiento",
            "Otros"
        )

        // Dropdown categoría
        var expandedCategoria by remember {
            mutableStateOf(false)
        }

        // Calendar
        val calendar = Calendar.getInstance()

        // DatePicker
        val datePickerDialog = DatePickerDialog(
            navController.context,
            { _, year, month, dayOfMonth ->

                fecha = String.format(
                    "%02d/%02d/%04d",
                    dayOfMonth,
                    month + 1,
                    year
                )
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        AlertDialog(

            onDismissRequest = {
                gastoEditando = null
            },

            title = {
                Text("Editar Gasto")
            },

            text = {

                Column {

                    // Nombre
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = {
                            nombre = it
                        },
                        label = {
                            Text("Nombre")
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Monto solo numérico
                    OutlinedTextField(
                        value = monto,
                        onValueChange = {

                            if (it.matches(Regex("^\\d*\\.?\\d*$"))) {
                                monto = it
                            }
                        },
                        label = {
                            Text("Monto")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Categoría dropdown
                    Box {

                        OutlinedTextField(
                            value = categoria,
                            onValueChange = {},
                            readOnly = true,
                            label = {
                                Text("Categoría")
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        DropdownMenu(
                            expanded = expandedCategoria,
                            onDismissRequest = {
                                expandedCategoria = false
                            }
                        ) {

                            categorias.forEach { item ->

                                DropdownMenuItem(
                                    text = {
                                        Text(item)
                                    },
                                    onClick = {

                                        categoria = item
                                        expandedCategoria = false
                                    }
                                )
                            }
                        }

                        Button(
                            onClick = {
                                expandedCategoria = true
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
                        value = fecha,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Fecha")
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            datePickerDialog.show()
                        }
                    ) {
                        Text("Seleccionar Fecha")
                    }
                }
            },

            confirmButton = {

                Button(
                    onClick = {

                        val gastoActualizado = gasto.copy(
                            nombre = nombre,
                            monto = monto.toDoubleOrNull() ?: 0.0,
                            categoria = categoria,
                            fecha = fecha
                        )

                        viewModel.editarGasto(gastoActualizado)

                        gastoEditando = null
                    }
                ) {
                    Text("Guardar")
                }
            },

            dismissButton = {

                Button(
                    onClick = {
                        gastoEditando = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}