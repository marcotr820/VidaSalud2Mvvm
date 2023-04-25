package com.example.vidasalud2.data.DataStore

import com.example.vidasalud2.data.model.Usuario

interface DataStoreRepository {

    suspend fun saveToken(key: String, value: String)
    suspend fun getToken(key: String): String?

    suspend fun saveUser(key: String, usuario: Usuario)
    suspend fun getUser(key: String): Usuario?

}