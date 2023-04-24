package com.example.vidasalud2.data.model

import com.google.gson.JsonObject

data class DataResult(
    val token: String? = null,
    val isBlocked: Boolean? = null,
    val usuario: JsonObject? = null
)
