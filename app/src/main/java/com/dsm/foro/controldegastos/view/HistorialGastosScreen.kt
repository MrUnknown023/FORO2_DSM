package com.dsm.foro.controldegastos.view

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Label
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dsm.foro.controldegastos.model.Gasto
import com.dsm.foro.controldegastos.viewmodel.GastosViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// ── Pantalla principal ───────────────────────────────────────────────────────
@Composable
fun HistorialGastosScreen(
    navController: NavController,
    viewModel: GastosViewModel = viewModel()
) {
    var gastoEditando by remember { mutableStateOf<Gasto?>(null) }

    LaunchedEffect(Unit) { viewModel.cargarGastos() }

    val meses = listOf(
        "01/2026","02/2026","03/2026","04/2026",
        "05/2026","06/2026","07/2026","08/2026",
        "09/2026","10/2026","11/2026","12/2026"
    )

    var expandedMes by remember { mutableStateOf(false) }
    var mesSeleccionado by remember {
        mutableStateOf(SimpleDateFormat("MM/yyyy", Locale.getDefault()).format(Date()))
    }

    val gastosFiltrados = viewModel.listaGastos.value.filter { it.fecha.contains(mesSeleccionado) }
    val totalFiltrado   = gastosFiltrados.sumOf { it.monto }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // ── Top bar ──────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón volver
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(BgField, CircleShape)
                        .clickable { navController.popBackStack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBackIosNew,
                        contentDescription = "Volver",
                        tint = TextSecondary,
                        modifier = Modifier.size(15.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Historial",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.3).sp
                    )
                    Text(
                        text = "Tus movimientos registrados",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
                // Ícono decorativo
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(AccentGreenDim, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.History,
                        contentDescription = null,
                        tint = AccentGreen,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Tarjeta resumen del mes ──────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = BgCard),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "TOTAL DEL MES",
                            color = TextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$${"%.2f".format(totalFiltrado)}",
                            color = AccentGreen,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = mesSeleccionado,
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${gastosFiltrados.size}",
                            color = TextPrimary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "gastos",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ── Selector de mes ──────────────────────────────────────────────
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(BgCard)
                        .clickable { expandedMes = true }
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CalendarMonth,
                        contentDescription = null,
                        tint = AccentGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Mes: $mesSeleccionado",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Rounded.ExpandMore,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                DropdownMenu(
                    expanded = expandedMes,
                    onDismissRequest = { expandedMes = false },
                    modifier = Modifier.background(BgCard)
                ) {
                    meses.forEach { mes ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = mes,
                                    color = if (mes == mesSeleccionado) AccentGreen else TextPrimary,
                                    fontWeight = if (mes == mesSeleccionado) FontWeight.Bold
                                    else FontWeight.Normal,
                                    fontSize = 13.sp
                                )
                            },
                            onClick = { mesSeleccionado = mes; expandedMes = false }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── Lista de gastos ──────────────────────────────────────────────
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                if (gastosFiltrados.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Rounded.ReceiptLong,
                                    contentDescription = null,
                                    tint = TextHint,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Sin gastos este mes",
                                    color = TextSecondary,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                } else {
                    items(gastosFiltrados) { gasto ->
                        GastoCard(
                            gasto = gasto,
                            onEditar = { gastoEditando = gasto },
                            onEliminar = { viewModel.eliminarGasto(gasto.id) }
                        )
                    }
                }
            }
        }
    }

    // ── Dialog editar ────────────────────────────────────────────────────────
    gastoEditando?.let { gasto ->
        EditarGastoDialog(
            gasto = gasto,
            navController = navController,
            onDismiss = { gastoEditando = null },
            onGuardar = { gastoActualizado ->
                viewModel.editarGasto(gastoActualizado)
                gastoEditando = null
            }
        )
    }
}

// ── Tarjeta de gasto ─────────────────────────────────────────────────────────
@Composable
private fun GastoCard(
    gasto: Gasto,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícono categoría
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(AccentGreenDim, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = categoryIcons[gasto.categoria] ?: Icons.Rounded.Category,
                    contentDescription = null,
                    tint = AccentGreen,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = gasto.nombre,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                Text(
                    text = "${gasto.categoria} · ${gasto.fecha}",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            // Monto + acciones
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${"%.2f".format(gasto.monto)}",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Editar
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(AccentGreenDim, RoundedCornerShape(7.dp))
                            .clickable { onEditar() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "Editar",
                            tint = AccentGreen,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    // Eliminar
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(Color(0x26FF5C7A), RoundedCornerShape(7.dp))
                            .clickable { onEliminar() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Eliminar",
                            tint = ErrorRed,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

// ── Dialog editar gasto ──────────────────────────────────────────────────────
@Composable
private fun EditarGastoDialog(
    gasto: Gasto,
    navController: NavController,
    onDismiss: () -> Unit,
    onGuardar: (Gasto) -> Unit
) {
    var nombre    by remember { mutableStateOf(gasto.nombre) }
    var monto     by remember { mutableStateOf(gasto.monto.toString()) }
    var categoria by remember { mutableStateOf(gasto.categoria) }
    var fecha     by remember { mutableStateOf(gasto.fecha) }

    val categorias = expenseCategories

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        navController.context,
        { _, year, month, dayOfMonth ->
            fecha = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = BgCard,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(AccentGreenDim, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = null,
                        tint = AccentGreen,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Editar Gasto",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column {
                // Nombre
                ExpenseFieldLabel("Nombre", Icons.Rounded.Label)
                Spacer(modifier = Modifier.height(5.dp))
                ExpenseTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    placeholder = "Nombre del gasto"
                )

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = DividerColor)
                Spacer(modifier = Modifier.height(12.dp))

                // Monto
                ExpenseFieldLabel("Monto", Icons.Rounded.AttachMoney)
                Spacer(modifier = Modifier.height(5.dp))
                ExpenseTextField(
                    value = monto,
                    onValueChange = {
                        if (it.matches(Regex("^\\d*\\.?\\d*$"))) monto = it
                    },
                    placeholder = "0.00",
                    keyboardType = KeyboardType.Decimal,
                    prefix = "$"
                )

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = DividerColor)
                Spacer(modifier = Modifier.height(12.dp))

                // Categorías como chips
                ExpenseFieldLabel("Categoría", Icons.Rounded.GridView)
                Spacer(modifier = Modifier.height(8.dp))
                categorias.chunked(3).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        row.forEach { cat ->
                            val selected = categoria == cat
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (selected) AccentGreenDim else BgField)
                                    .border(
                                        1.5.dp,
                                        if (selected) AccentGreen else Color.Transparent,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { categoria = cat }
                                    .padding(vertical = 7.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = categoryIcons[cat] ?: Icons.Rounded.Category,
                                    contentDescription = cat,
                                    tint = if (selected) AccentGreen else TextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = cat,
                                    color = if (selected) AccentGreen else TextSecondary,
                                    fontSize = 9.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                    maxLines = 1
                                )
                            }
                        }
                        repeat(3 - row.size) { Spacer(modifier = Modifier.weight(1f)) }
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }

                Spacer(modifier = Modifier.height(4.dp))
                Divider(color = DividerColor)
                Spacer(modifier = Modifier.height(12.dp))

                // Fecha
                ExpenseFieldLabel("Fecha", Icons.Rounded.CalendarMonth)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(BgField)
                        .clickable { datePickerDialog.show() }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Event,
                        contentDescription = null,
                        tint = AccentGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = fecha.ifBlank { "Seleccionar fecha" },
                        color = if (fecha.isBlank()) TextHint else TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Rounded.ChevronRight,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onGuardar(
                        gasto.copy(
                            nombre = nombre,
                            monto = monto.toDoubleOrNull() ?: 0.0,
                            categoria = categoria,
                            fecha = fecha
                        )
                    )
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentGreen,
                    contentColor = BgDark
                ),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text("Guardar", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        },
        dismissButton = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(BgField)
                    .clickable { onDismiss() }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text("Cancelar", color = TextSecondary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }
    )
}

