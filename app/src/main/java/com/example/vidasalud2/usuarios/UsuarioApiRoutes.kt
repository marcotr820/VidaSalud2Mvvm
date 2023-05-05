package com.example.vidasalud2.usuarios

import com.example.vidasalud2.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface UsuarioApiRoutes {

    @GET("cuentas/GetUsuarios")
    suspend fun getUsuarios(): Response<ResponseHttp<List<Usuario>>>

    @GET("cuentas/UserNameExiste")
    suspend fun userNameExiste(@Query("userName") username: String): Boolean

    @GET("cuentas/EmailExiste")
    suspend fun emailExiste(@Query("email") email: String): Boolean

    @POST("cuentas/CrearUsuarioNormal")
    suspend fun registrarUsuario(@Body registroModel: RegistroModel): Response<ResponseHttp<DataResult>>

    @PUT("cuentas/EditarPassword")
    suspend fun actualizarPassword(@Body actualizarPassword: ActualizarPassword): Response<ResponseHttp<Boolean>>

    @PUT("cuentas/EditarUserName")
    suspend fun actualizarUserName(@Body actualizarUserName: ActualizarUserName): Response<ResponseHttp<Boolean>>
}