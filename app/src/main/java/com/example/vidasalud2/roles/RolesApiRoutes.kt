package com.example.vidasalud2.roles

import com.example.vidasalud2.data.model.Rol
import retrofit2.Response
import retrofit2.http.GET

interface RolesApiRoutes {

    @GET("roles/Roles")
    suspend fun gerRolesDropdown(): Response<List<Rol>>

}