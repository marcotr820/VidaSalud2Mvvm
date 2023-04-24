package com.example.vidasalud2.core

interface TokenRepository {

    suspend fun saveToken(key: String, value: String)
    suspend fun getToken(key: String): String?

}