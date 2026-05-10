package com.dsm.foro.controldegastos.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.dsm.foro.controldegastos.model.Gasto

class GastosRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun guardarGasto(nombre: String, monto: Double, categoria: String, fecha: String, onResult: (Boolean, String) -> Unit) {
        val currentUser = auth.currentUser

        // Verificamos que el usuario realmente esté autenticado
        if (currentUser == null) {
            onResult(false, "Error: No hay un usuario autenticado.")
            return
        }

        // Creamos el objeto Gasto y le inyectamos el ID del usuario actual (uid)
        val nuevoGasto = Gasto(
            nombre = nombre,
            monto = monto,
            categoria = categoria,
            fecha = fecha,
            userId = currentUser.uid
        )

        // Guardamos en la colección "gastos" de Firestore
        db.collection("gastos")
            .add(nuevoGasto)
            .addOnSuccessListener {
                onResult(true, "¡Gasto guardado exitosamente!")
            }
            .addOnFailureListener { e ->
                onResult(false, "Error al guardar: ${e.message}")
            }
    }
    fun obtenerGastos(onResult: (List<Gasto>) -> Unit) {

        val currentUser = auth.currentUser

        if (currentUser == null) {
            onResult(emptyList())
            return
        }

        db.collection("gastos")
            .whereEqualTo("userId", currentUser.uid)
            .get()
            .addOnSuccessListener { result ->

                val lista = result.documents.mapNotNull { document ->

                    val gasto = document.toObject(Gasto::class.java)

                    gasto?.copy(id = document.id)
                }

                onResult(lista)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun eliminarGasto(
        id: String,
        onResult: (Boolean) -> Unit
    ) {

        db.collection("gastos")
            .document(id)
            .delete()
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    fun editarGasto(
        gasto: Gasto,
        onResult: (Boolean) -> Unit
    ) {

        db.collection("gastos")
            .document(gasto.id)
            .set(gasto)
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

}