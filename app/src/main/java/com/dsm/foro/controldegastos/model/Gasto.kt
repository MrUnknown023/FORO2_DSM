package com.dsm.foro.controldegastos.model

data class Gasto(
    val nombre: String = "",
    val monto: Double = 0.0,
    val categoria: String = "",
    val fecha: String = "",
    val userId: String = "" // <- Esto cumple el requisito de asociarlo al usuario
)