package com.example.vidasalud2.data.model

import com.google.gson.annotations.SerializedName

data class ActualizarPassword(
    @SerializedName("id") val id: String,
    @SerializedName("actualPassword") val actualPassword: String,
    @SerializedName("nuevoPassword") val nuevoPassword: String,
    @SerializedName("confirmarPassword") val confirmarPassword: String,
)

