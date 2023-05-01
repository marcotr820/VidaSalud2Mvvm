package com.example.vidasalud2.usuarios

import com.example.vidasalud2.data.model.ResponseHttp
import com.example.vidasalud2.data.model.Usuario
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UsuarioApiRoutes {

    @GET("cuentas/GetUsuarios")
    suspend fun getUsuarios(): Response<ResponseHttp<List<Usuario>>>

    @GET("cuentas/UserNameExiste")
    suspend fun userNameExiste(@Query("userName") username: String): Response<Boolean>

    @GET("cuentas/EmailExiste")
    suspend fun emailExiste(@Query("email") email: String): Response<Boolean>

}