package com.example.vidasalud2.data.model

data class ResponseHttp<T> (
    val dataResult: T? = null,
    val error: String? = null
)
