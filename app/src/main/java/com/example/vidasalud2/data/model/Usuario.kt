package com.example.vidasalud2.data.model

data class Usuario(
    val id: String,
    var userName: String,
    val email: String,
    val isBlocked: Boolean = false,
    var rol: List<String>? = emptyList(),
)
