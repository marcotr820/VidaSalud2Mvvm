package com.example.vidasalud2.data.model

data class Rol(
    val id: String,
    val name: String
){
    override fun toString(): String {
        return name
    }
}
