package com.dsm.foro.controldegastos.model

data class Gasto(

    val id: String = "",

    val nombre: String = "",
    val monto: Double = 0.0,
    val categoria: String = "",
    val fecha: String = "",
    val userId: String = ""
)