package com.example.vidasalud2.usuarios

import com.example.vidasalud2.data.model.ResponseHttp
import com.example.vidasalud2.data.model.Usuario
import retrofit2.Response
import retrofit2.http.GET

interface UsuarioApiRoutes {

    @GET("cuentas/GetUsuarios")
    suspend fun getUsuarios(): Response<ResponseHttp<List<Usuario>>>
}