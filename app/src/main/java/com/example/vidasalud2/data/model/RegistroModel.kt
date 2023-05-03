package com.example.vidasalud2.data.model

import com.google.gson.annotations.SerializedName

data class RegistroModel(
    @SerializedName("userName") val userName: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
)
