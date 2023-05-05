package com.example.vidasalud2.data.model

import com.google.gson.annotations.SerializedName

data class ActualizarUserName(
    @SerializedName("id") val id: String,
    @SerializedName("userName") val userName: String
)
