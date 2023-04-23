package com.example.vidasalud2.data.model

import com.google.gson.JsonObject

data class ResponseHttp(
    val dataResult: JsonObject? = null,
    val error: String? = null
)
