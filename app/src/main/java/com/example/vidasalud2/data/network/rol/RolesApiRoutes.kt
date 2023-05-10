package com.example.vidasalud2.data.network.rol

import com.example.vidasalud2.data.model.Rol
import retrofit2.Response
import retrofit2.http.GET

interface RolesApiRoutes {

    @GET("roles/Roles")
    suspend fun getRoles(): Response<List<Rol>>

}