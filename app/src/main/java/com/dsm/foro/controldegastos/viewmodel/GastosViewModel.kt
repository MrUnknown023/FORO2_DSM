package com.dsm.foro.controldegastos.viewmodel
import com.dsm.foro.controldegastos.model.Gasto
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dsm.foro.controldegastos.repository.GastosRepository

class GastosViewModel : ViewModel() {
    private val repository = GastosRepository()

    // Estados para los campos del formulario
    var nombre = mutableStateOf("")
    var monto = mutableStateOf("")
    var categoria = mutableStateOf("")
    var fecha = mutableStateOf("")

    // Estados para la interfaz
    var isLoading = mutableStateOf(false)
    var mensaje = mutableStateOf<String?>(null)



    var listaGastos = mutableStateOf<List<Gasto>>(emptyList())

    var totalMensual = mutableStateOf(0.0)

    fun registrarGasto() {
        // Validaciones básicas que pide la rúbrica
        if (nombre.value.isEmpty() || monto.value.isEmpty() || categoria.value.isEmpty() || fecha.value.isEmpty()) {
            mensaje.value = "Por favor, completa todos los campos."
            return
        }

        val montoDouble = monto.value.toDoubleOrNull()
        if (montoDouble == null || montoDouble <= 0) {
            mensaje.value = "Ingresa un monto válido mayor a 0."
            return
        }

        isLoading.value = true

        repository.guardarGasto(
            nombre.value,
            montoDouble,
            categoria.value,
            fecha.value
        ) { exito, msj ->
            isLoading.value = false
            mensaje.value = msj

            if (exito) {
                // Si se guardó bien, limpiamos el formulario
                nombre.value = ""
                monto.value = ""
                categoria.value = ""
                fecha.value = ""
            }
        }
    }

    fun cargarGastos() {

        repository.obtenerGastos { gastos ->

            listaGastos.value = gastos

            val mesActual = java.text.SimpleDateFormat("MM/yyyy")
                .format(java.util.Date())

            totalMensual.value = gastos
                .filter { it.fecha.contains(mesActual) }
                .sumOf { it.monto }
        }
    }

    fun eliminarGasto(id: String) {

        repository.eliminarGasto(id) {

            cargarGastos()
        }
    }

    fun editarGasto(gasto: Gasto) {

        repository.editarGasto(gasto) {

            cargarGastos()
        }
    }
}