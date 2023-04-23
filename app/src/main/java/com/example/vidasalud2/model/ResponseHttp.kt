package com.example.vidasalud2.model

import com.google.gson.JsonObject

data class ResponseHttp(
    val dataResult: JsonObject? = null,
    val error: String? = null
)
