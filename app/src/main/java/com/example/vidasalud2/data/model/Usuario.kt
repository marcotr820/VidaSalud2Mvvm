package com.example.vidasalud2.data.model

data class Usuario(
    val id: String,
    val userName: String,
    val email: String,
    val isBlocked: Boolean = false,
    val rol: List<String>? = emptyList(),
)
