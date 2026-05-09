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
}